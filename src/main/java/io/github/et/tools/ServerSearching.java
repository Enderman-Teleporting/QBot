package io.github.et.tools;

import me.dilley.MineStat;

public class ServerSearching {
    static String result;
    public static String search(String ip,int port){
        try {
            MineStat mineStat = new MineStat(ip, port);
            if (mineStat.isServerUp()) {
                result =
                        "服务器在线!\n" +
                                "IP: " + ip + "\n" +
                                "端口: " + port + "\n" +
                                "版本: " + mineStat.getVersion() + "\n" +
                                "在线人数: " + mineStat.getCurrentPlayers() + "\n" +
                                "最大玩家数: " + mineStat.getMaximumPlayers() + "\n" +
                                "模式: " + mineStat.getGameMode() + "\n" +
                                "Motd: " + mineStat.getMotd() + "\n" +
                                "延迟: " + mineStat.getLatency() + "ms\n" +
                                "协议: " + mineStat.getRequestType()+"\n";
            } else {
                result = "服务器不在线或者不存在,或许检查一下?";
            }
        }
        catch(Exception e){
            result = "出了点小错...有没有一种可能,服务器不在线或者不存在?";
        }
        return result;
    }

    public static String search(String ip){
        try {
            MineStat mineStat = new MineStat(ip);
            if (mineStat.isServerUp()) {
                result =
                        "服务器在线!\n" +
                                "IP: " + ip + "\n" +
                                "版本: " + mineStat.getVersion() + "\n" +
                                "在线人数: " + mineStat.getCurrentPlayers() + "\n" +
                                "最大玩家数: " + mineStat.getMaximumPlayers() + "\n" +
                                "模式: " + mineStat.getGameMode() + "\n" +
                                "Motd: " + mineStat.getStrippedMotd() + "\n" +
                                "延迟: " + mineStat.getLatency() + "ms\n" +
                                "协议: " + mineStat.getRequestType()+"\n";
            } else {
                result = "服务器不在线或者不存在,或许检查一下?";
            }
        }
        catch(Exception e){
            result = "出了点小错...有没有一种可能,服务器不在线或者不存在?";
        }
        return result;
    }
}
