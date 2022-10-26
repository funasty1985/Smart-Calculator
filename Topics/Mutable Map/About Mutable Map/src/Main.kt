// You can experiment here, it won’t be checked

fun main() {
    val map1 = mutableMapOf<String, Int>()
    val map2 = mapOf<String, Int>()
    map1["Apple"] = 300
    map1["Banana"] = 400
    map1["Lemon"] = 250
    map2["Melon"] = 400
    map2["Mango"] = 600
    map1.putAll(map2)
    println(map1)
}