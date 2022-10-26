fun containsKeyAndValue(map: Map<String, String>, value: String): Boolean {
    // put your code here
    return map.containsValue(value) && map.containsKey(value)
}