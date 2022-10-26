package calculator
import java.math.BigInteger

const val CHECK_DIGIT_OPERATOR_REGEX_STR = "^((\\d+|[-+]+ *\\d+)(\\s*(-+|\\++|\\*|/)\\s*\\d+\\s*)*)\$"
const val CHECK_COMMAND_REGEX_STR = "^/[a-zA-Z]+"
const val CHECK_EMPTY_INPUT_REGEX_STR = "^\\s*\$"
const val CHECK_VAR_INPUT_LOOSE_REX_STR = "^\\w+$"
const val CHECK_VAR_INPUT_STRICT_REX_STR = "^[a-zA-Z]+$"
const val CHECK_VAR_ASSIGNMENT_LOOSE_REX_STR = "^\\w+(\\s*=\\s*-*\\s*\\w+)+"
const val CHECK_FIRST_VAR_IN_ASSIGNMENT_REX_STR = "^[a-zA-Z]+ *=.*"
const val CHECK_VAR_ASSIGNMENT_STRICT_REX_STR = "^[a-zA-Z]+ *= *-* *([a-zA-Z]+|[0-9]+)\$"
const val CHECK_VAR_OPERATION_LOOSE_REX_STR = "^\\w+( *(-+|\\++|\\*|/) *\\w+)+"
const val SEARCH_INVALID_VARIABLE_PATTERN_REX_STR = "[a-zA-Z]\\d|\\d[a-zA-Z]"
var vMap = mutableMapOf<String, BigInteger?>()

fun replParOp(inputStr: String): String{
    val digitReg = "\\(((\\d+|[-+]+ *\\d+)(\\s*(-+|\\++|\\*|/)\\s*\\d+\\s*)*)?\\)".toRegex()
    val varReg = "\\((([a-zA-Z]+|[-+]+ *\\w+)(\\s*(-+|\\++|\\*|/)\\s*[a-zA-Z]+\\s*)*)?\\)".toRegex()
    var str = inputStr
    if(digitReg.containsMatchIn(inputStr)){
        val newStr = inputStr.replace(digitReg, "1")
        str = replParOp(newStr)
    }
    if(varReg.containsMatchIn(inputStr)){
        val newStr = inputStr.replace(varReg, "a")
        str = replParOp(newStr)
    }
    return str
}

fun formatPlusMin(inputStr: String): String{
    val dupOp = "\\+\\+|\\-\\-|\\-\\+|\\+\\-".toRegex()
    var str = inputStr
    if(dupOp.containsMatchIn(inputStr)){
        var newStr = str.replace("\\+\\+".toRegex(), "+")
        newStr = newStr.replace("(\\-\\+)|(\\+\\-)".toRegex(), "-")
        newStr = newStr.replace("\\-\\-".toRegex(), "+")
        str = formatPlusMin(newStr)
    }
    return str
}

fun validateInput (inputStr: String): String {
    var error = ""

    val regexStrArray = listOf(
        CHECK_DIGIT_OPERATOR_REGEX_STR,
        CHECK_COMMAND_REGEX_STR,
        CHECK_EMPTY_INPUT_REGEX_STR,
        CHECK_VAR_ASSIGNMENT_LOOSE_REX_STR,
        CHECK_VAR_INPUT_LOOSE_REX_STR,
        CHECK_VAR_OPERATION_LOOSE_REX_STR
    )

    val regex = regexStrArray.joinToString("|").toRegex()

    if(!inputStr.matches(regex)){
        error = "Invalid expression"
    }

    if(
        (
                inputStr.matches(CHECK_VAR_INPUT_LOOSE_REX_STR.toRegex()) &&
                        !inputStr.matches(CHECK_VAR_INPUT_STRICT_REX_STR.toRegex()))
        ||
        (inputStr.matches(CHECK_VAR_ASSIGNMENT_LOOSE_REX_STR.toRegex()) &&
                !inputStr.matches(CHECK_FIRST_VAR_IN_ASSIGNMENT_REX_STR.toRegex()))
    )
    {
        error = "Invalid identifier"
    }


    if(
        inputStr.matches(CHECK_VAR_ASSIGNMENT_LOOSE_REX_STR.toRegex()) &&
        !inputStr.matches(CHECK_VAR_ASSIGNMENT_STRICT_REX_STR.toRegex())
    )
    {
        error = "Invalid assignment"
    }

    val acceptedStrList = listOf("/exit", "/help")
    if(inputStr.matches(CHECK_COMMAND_REGEX_STR.toRegex()) && !acceptedStrList.contains(inputStr)){
        error = "Unknown command"
    }

    return error
}


fun assignVar(inputStr: String){
    val (key, value) = inputStr.split("=")

    if(value.matches("\\d+".toRegex())){
        vMap[key] = BigInteger(value)
    } else {
        if(!vMap.containsKey(value)){
            println("Unknown variable")
        }

        val v = vMap[value]
        vMap[key] = v
    }
}

fun getVarVal(inputStr: String){
    if(vMap.containsKey(inputStr)){
        println(vMap[inputStr])
    } else {
        println("Unknown variable")
    }
}
val priorityMap = mutableMapOf(
    "+" to 1,
    "-" to 1,
    "*" to 2,
    "/" to 2,
    "(" to 2,
    ")" to 2
)

class Stack<T>(){

    private val _stack = mutableListOf<T>()

    fun isStackEmpty(): Boolean{
        return _stack.size == 0
    }

    fun getStackTopElement(): T? {
        return if(isStackEmpty()) null else _stack[_stack.size-1]
    }

    fun popStack(): T?{
        if(isStackEmpty()){
            return null
        }

        val ele = _stack[_stack.size-1]
        _stack.removeAt(_stack.size-1)

        return ele
    }

    fun pushToStack(ele : T){
        _stack.add(ele)
    }
}

fun genPostfix(infix: List<String>): MutableList<String>{
    val postfix = mutableListOf<String>()
    val stack = Stack<String>()

    for(ele in infix){

        val isEleOperand = ele.matches("^\\w+\$".toRegex())

        if(isEleOperand){
            postfix.add(ele)
            continue
        } else {

            if(stack.isStackEmpty() || stack.getStackTopElement() == "("){
                stack.pushToStack(ele)
                continue
            }

            if(ele == "(") {
                stack.pushToStack(ele)
                continue
            }

            if(ele == ")"){
                val shouldConc = true
                while(shouldConc){
                    val popEle = stack.popStack() ?: break
                    if(popEle == "("){
                        break;
                    }
                    postfix.add(popEle)
                }
                continue
            }

            if(stack.isStackEmpty()){
               continue
            }

            if(priorityMap[ele]!! > priorityMap[stack.getStackTopElement()]!!){
                stack.pushToStack(ele)
                continue
            }

            while(priorityMap[ele]!! <= priorityMap[stack.getStackTopElement()]!!){
                val popEle = stack.popStack() ?: break
                postfix.add(popEle)

                if(stack.isStackEmpty()){
                    stack.pushToStack(ele)
                    break
                }

                if(priorityMap[ele]!! > priorityMap[stack.getStackTopElement()]!! || stack.getStackTopElement() == "("){
                    stack.pushToStack(ele)
                    break;
                }
            }
        }

    }

    // after the iteration of infix, pop the rest of the stack to Postfix
    while(true){
        val ele = stack.popStack() ?: break
        postfix.add(ele)
    }

    return postfix
}

fun computeResult(postfix: MutableList<String>) {
    val stack = Stack<BigInteger>()

    val _postfix = mutableListOf<String>()
    for (ele in postfix) {
        if(ele.matches("[a-zA-Z]+".toRegex())){
            if(vMap.containsKey(ele)){
                _postfix.add(vMap[ele].toString())
            } else {
                println("Unknown variable")
                return
            }
        } else {
            _postfix.add(ele)
        }
    }



    for(ele in _postfix){
        if(ele.matches("\\w+".toRegex())){
            val bigNum = BigInteger(ele)
            stack.pushToStack(bigNum)
        } else {
            val b = stack.popStack() ?: 0.toBigInteger()
            val a = stack.popStack() ?: 0.toBigInteger()

            when(ele){
                "+" -> stack.pushToStack(a+b)
                "-" -> stack.pushToStack(a-b)
                "*" -> stack.pushToStack(a*b)
                "/" -> stack.pushToStack(a/b)
            }
        }
    }

    val result = stack.popStack()!!
    println(result)
    return
}

fun genInfix(inputStr: String): List<String>{
    return inputStr.replace(" ","")
        .replace("(\\w)(\\W)".toRegex(), "$1|$2")
        .replace("(\\W)(\\w)".toRegex(), "$1|$2")
        .replace("([()])(\\w|[+-/*])".toRegex(), "$1|$2")
        .replace("(\\w|[+-/*])([()])".toRegex(), "$1|$2")
        .replace("(\\))(\\))".toRegex(), "$1|$2")
        .replace("(\\()(\\()".toRegex(), "$1|$2")
        .split("|")
}



fun main() {

    loop@while(true){
        var inputStr = readln().replace(" ","")

        val checkInputStr = replParOp(inputStr)
        val error = validateInput(checkInputStr)

        if( error != "" ){
            println(error)
            continue
        }

        inputStr = formatPlusMin(inputStr)

        when {
            checkInputStr == "/exit" -> {
                println("Bye!\n")
                break
            }
            checkInputStr == "/help" -> {
                println("The program calculates the sum of numbers\n")
                continue
            }
            checkInputStr.matches(CHECK_DIGIT_OPERATOR_REGEX_STR.toRegex()) ||
                    checkInputStr.matches(CHECK_VAR_OPERATION_LOOSE_REX_STR.toRegex()) -> {

                val infix = genInfix(inputStr)
                val postfix = genPostfix(infix)
                computeResult(postfix)

                continue
            }
            checkInputStr.matches(CHECK_VAR_ASSIGNMENT_STRICT_REX_STR.toRegex()) -> {
                assignVar(inputStr)
                continue
            }
            checkInputStr.matches(CHECK_VAR_INPUT_STRICT_REX_STR.toRegex()) -> {
                getVarVal(inputStr)
                continue
            }
            checkInputStr == "" -> continue
        }
    }

}

