package tools

import me.dilley.MineStat

object serverSearching {
    var result: String? = null
    fun search(ip: String, port: Int): String? {
        try {
            val mineStat = MineStat(ip, port)
            if (mineStat.isServerUp) {
                result = """
                    服务器在线!
                    IP: $ip
                    端口: $port
                    版本: ${mineStat.version}
                    在线人数: ${mineStat.currentPlayers}
                    最大玩家数: ${mineStat.maximumPlayers}
                    模式: ${mineStat.gameMode}
                    Motd: ${mineStat.motd}
                    延迟: ${mineStat.latency}ms
                    协议: ${mineStat.requestType}
                    
                    """.trimIndent()
            } else {
                result = "服务器不在线或者不存在,或许检查一下?"
            }
        } catch (e: Exception) {
            result = "出了点小错...有没有一种可能,服务器不在线或者不存在?"
        }
        return result
    }

    fun searchImg(ip: String?, port: Int): ByteArray? {
        return ImageUtil.toImage(MineStat(ip, port).faviconB64)
    }

    fun search(ip: String): String? {
        try {
            val mineStat = MineStat(ip)
            if (mineStat.isServerUp) {
                result = """
                    服务器在线!
                    IP: $ip
                    版本: ${mineStat.version}
                    在线人数: ${mineStat.currentPlayers}
                    最大玩家数: ${mineStat.maximumPlayers}
                    模式: ${mineStat.gameMode}
                    Motd: ${mineStat.strippedMotd}
                    延迟: ${mineStat.latency}ms
                    协议: ${mineStat.requestType}
                    
                    """.trimIndent()
            } else {
                result = "服务器不在线或者不存在,或许检查一下?"
            }
        } catch (e: Exception) {
            result = "出了点小错...有没有一种可能,服务器不在线或者不存在?"
        }
        return result
    }

    fun searchImg(ip: String?): ByteArray? {
        return ImageUtil.toImage(MineStat(ip).faviconB64)
    }
}