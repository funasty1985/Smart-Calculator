fun makeMyWishList(wishList: Map<String, Int>, limit: Int): MutableMap<String, Int> {
    // write here
    val result = wishList.toMutableMap()
    for((k, v) in wishList){
        if(v > limit){
            result.remove(k)
        }
    }

    return result
}