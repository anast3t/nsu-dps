enum class Component() {
    A { override fun getTime(): Long {return 1000} },
    B { override fun getTime(): Long {return 2000} },
    C { override fun getTime(): Long {return 3000} },
    Module { override fun getTime(): Long {return 0} },
    Widget { override fun getTime(): Long {return 0} };

    abstract fun getTime(): Long
}