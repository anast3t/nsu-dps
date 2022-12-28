import java.util.concurrent.locks.ReentrantLock

class MySLList {
    private var head: Node? = null
    private val lock: ReentrantLock = ReentrantLock()
    fun push(str: String){
        lock.lock()
        try {
            println("Locked for adding")
            head = Node(str, head)
            println("Added")
        } finally {
            lock.unlock()
        }
    }

    fun print(){
        lock.lock()
        try {
            println("Locked for printing")
            head?.print()
            println("Printed!")
        } finally {
            lock.unlock()
        }
    }

    fun sort(){
        lock.lock()
        try {
            println("Locked for sort")
            var f = head
            while(f != null){
                var s = f.next
                while(s != null){
                    if(s > f){
                        println("${s.value} > ${f.value}")
                        f.swap(s)
                    }
                    s=s.next
                }
                f=f.next
            }
            println("Sorted!")
        } finally {
            lock.unlock()
        }
    }
}