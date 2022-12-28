data class Node(var value: String, val next: Node?) : Comparable<Node>{

    override fun compareTo(other: Node): Int = when {
        this.value != other.value -> this.value compareTo other.value
        else -> 0
    }

    fun swap(other: Node){
        val buf = other.value
        other.value = value
        value = buf
    }
    fun print(){
        println(value)
        next?.print()
    }
}
