fun main() {
    // write your code here
    val a = readln().toBigInteger()
    val b = readln().toBigInteger()

    var sum = a + b
    sum += (a-b).abs()
    val (result, _) = sum.divideAndRemainder(2.toBigInteger())

    println(result) 
}
