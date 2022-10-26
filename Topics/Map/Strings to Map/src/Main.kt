fun stringToMap(first: String, second: String, third: String): Map<String, Int> {
    // put your code here
    return mapOf(
        first to first.length,
        second to second.length,
        third to third.length,
    )
}