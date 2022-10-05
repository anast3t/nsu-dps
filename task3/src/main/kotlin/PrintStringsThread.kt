class PrintStringsThread(var strings: Array<String>) : Thread() {
    override fun run() {
        strings.forEach {println("${this.name}: $it")}
    }
}