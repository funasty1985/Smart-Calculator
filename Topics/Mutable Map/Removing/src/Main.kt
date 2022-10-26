fun removing(currentMap: MutableMap<Int, String>, value: String): MutableMap<Int, String> {
    // Write your code here. Do not print the result of the function! 
    val newMap = mutableMapOf<Int, String>()

    for((k,v) in currentMap){
        if( v != value ) {
            newMap[k] = v
        }
    }
    return newMap
}