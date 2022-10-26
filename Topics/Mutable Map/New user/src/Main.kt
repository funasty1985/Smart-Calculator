fun addUser(userMap: Map<String, String>, login: String, password: String): MutableMap<String, String>? {
    // write your code here
    val newMap = mutableMapOf<String, String>()

    if(userMap.containsKey(login)){
        println("User with this login is already registered!")
    }

    newMap.putAll(userMap)
    newMap.putIfAbsent(login, password)

    return newMap
}