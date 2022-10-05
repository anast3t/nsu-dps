fun main(args: Array<String>) {
    var testStrs: Array<Array<String>> = arrayOf(
        arrayOf("123", "456", "789"),
        arrayOf("1", "2", "3", "4", "5"),
        arrayOf("qwe", "rty"),
        arrayOf("asd", "asd2", "asd3", "asd4")
    )
    for(i in 0..3){
        PrintStringsThread(testStrs[i]).start()
    }
}