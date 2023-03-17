package tools

import net.sf.json.JSONObject
import java.io.*
import java.net.URL

object API {
    @Throws(IOException::class)
    fun getApi(url: String?, key: String?): String {
        val url1 = URL(url)
        val `is` = url1.openStream()
        val br = BufferedReader(InputStreamReader(`is`))
        var result = br.readLine()
        val obj = JSONObject.fromObject(result)
        result = obj.getString(key)
        result = result.replace("{br}", "\n")
        return result
    }
}