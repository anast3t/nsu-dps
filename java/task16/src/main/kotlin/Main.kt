import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue

private data class Line(val content:String?)

fun main() {
    val url = URL("https://www.google.com/")

    val blockingQueue = LinkedBlockingQueue<Line>()

    val client = Thread {
        val socket = Socket(url.host, if(url.port!=-1) url.port else 80)
        val request = "GET ${url.path} HTTP/1.1\r\nHost: ${url.host}\r\nConnection: close\r\n\r\n"

        socket.getOutputStream().write(request.toByteArray())
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

        var skippedHeaders = false
        reader.lines().forEach {
            if(skippedHeaders){
                blockingQueue.add(Line(it))
            }else if(it.isEmpty()){
                skippedHeaders = true
            }
            Thread.sleep(200) //
        }
        blockingQueue.add(Line(null))
        socket.close()
    }
    client.start()

    var lineNumber = 0
    val lineLimit = 5
    while (true){

        val str = blockingQueue.take().content ?: break
        println(str)
        lineNumber += 1
        if(lineNumber >= lineLimit){
            println("[Press space to scroll down]")
            readln()
            lineNumber = 0
        }
    }

}