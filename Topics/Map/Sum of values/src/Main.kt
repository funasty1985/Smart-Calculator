fun summator(map: Map<Int, Int>): Int {
    // put your code here

    var r = 0
    for((k, v) in map){
        if ( k % 2 == 0 ) {
            r += v
        }
    }
    return r
}