package tools

import java.io.*
import java.util.*

object properties_settler {
    fun settle() {
        File("./cache/properties").mkdirs()
        val fileName = "./cache/properties/property.properties"
        try {
            val f = File(fileName)
            if (!f.exists()) {
                f.createNewFile()
                val bw = BufferedWriter(FileWriter("./cache/properties/property.properties"))
                bw.write("Log=true\n")
                bw.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun read(path: String?, key: String?): String {
        val `in`: InputStream = BufferedInputStream(FileInputStream(path))
        val properties = Properties()
        properties.load(`in`)
        return properties.getProperty(key)
    }
}