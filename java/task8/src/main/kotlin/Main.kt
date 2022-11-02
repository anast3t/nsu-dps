fun main(args: Array<String>) {
    val threadNumber = if(args.isEmpty()) 10 else args[0].toInt() //10 for m1
    val summarizer = SummarizerFraction(threadNumber)
    val time = System.currentTimeMillis()
    summarizer.start()
    Runtime.getRuntime().addShutdownHook(
        Thread {
            summarizer.finish()
            summarizer.join()

            println("----------${summarizer.getSum() * 4}--------")
            println("Time: ${(System.currentTimeMillis() - time).toDouble()/3600}")
        }
    )
}