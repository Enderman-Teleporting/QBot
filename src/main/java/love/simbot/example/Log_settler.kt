package love.simbot.example

import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.*

class Log_settler {
    val mylog: Logger
        get() {
            val time = ZonedDateTime.now()
            val date1 = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val logger = Logger.getLogger(date1)
            logger.level = Level.ALL
            for (h in logger.handlers) {
                h.close()
            }
            try {
                val fileHandler = FileHandler("./cache/logs/$date1.log", true)
                fileHandler.formatter = myFormat()
                logger.addHandler(fileHandler)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return logger
        }

    companion object {
        @JvmStatic
        fun settle() {
            val file = File("./cache/logs")
            file.mkdirs()
        }

        @JvmStatic
        fun writelog(content: String?) {
            val lt = Log_settler()
            val log = lt.mylog
            log.info(content)
        }
    }
}

internal class myFormat : Formatter() {
    override fun format(record: LogRecord): String {
        val zdf = ZonedDateTime.now()
        val sDate = zdf.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        return """
            [$sDate]: ${record.message}
            
            """.trimIndent()
    }
}