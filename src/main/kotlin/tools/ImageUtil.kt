package tools

import cn.hutool.core.codec.Base64Decoder

object ImageUtil {
    fun toImage(base64: String?): ByteArray? {
        return try {
            val data = Base64Decoder.decode(base64)
            for (i in data.indices) {
                if (data[i] < 0) {
                    data[i]=(data[i].toInt() + 256).toByte()
                }
            }
            data
        } catch (e: Exception) {
            null
        }
    }
}