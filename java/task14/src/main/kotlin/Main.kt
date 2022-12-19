fun main(args: Array<String>) {
    val zavod = Combiner(
        produce = Component.Widget,
        producers = arrayListOf(
            Fabric(Component.C, 10),
            Combiner(
                produce = Component.Module,
                producers = arrayListOf(
                    Fabric(Component.A, 10),
                    Fabric(Component.B, 10)
                ),
                maxItems = 10
            )
        ),
        maxItems = 100
    )

    val zavodThread:Thread = Thread(zavod)
    zavodThread.start()
    zavodThread.join()
}