import java.util.concurrent.Semaphore

class Combiner0 (
//Тут оставлю на память, как я блокировки не в ту сторону вёл, с идеями, что можно прокидывать тип производимой компоненты наверх
    private val receive: ArrayList<Component>,
    private val produce: Component
):Thread() {
    private lateinit var typeSemaMap: HashMap<Component, Semaphore>
    init {
        receive.forEach{ typeSemaMap[it] = Semaphore(0) }
    }
    fun passComponent(componentType: Component){
        typeSemaMap[componentType]?.acquire()
    }
    override fun run() {

    }
}