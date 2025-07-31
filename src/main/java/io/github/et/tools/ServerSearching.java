package io.github.et.tools;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerSearching {
    static String result;
    public static MessageChain search(String ip, int port, Contact subject) throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://tzdtwsj.top/api/get_mc_server_status_v2.php?host=" + URLEncoder.encode(ip, StandardCharsets.UTF_8) + "&port=" + port).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            for (String a : br.lines().toList()) {
                sb.append(a);
            }
            JSONObject jo = JSONObject.parseObject(sb.toString());
            if (jo.getString("status").equals("fail")) {
                return new MessageChainBuilder().append(new PlainText("服务器不在线或者不存在,或许检查一下?")).build();
            } else {
                JSONObject data=jo.getJSONObject("data");
                JSONArray be = data.getJSONArray("ipv4_be");
                JSONArray je=data.getJSONArray("ipv4_je");
                MessageChainBuilder mcb = new MessageChainBuilder();
                for (Object a : be) {
                    if(a instanceof JSONObject b) {
                        mcb.append(new PlainText("[基岩版]\n"));
                        mcb.append(new PlainText("地址:"+b.get("address")+"\n"));
                        mcb.append(new PlainText("IP:"+b.get("ip")+"\n"));
                        mcb.append(new PlainText("端口:"+b.get("port")+"\n"));
                        mcb.append(new PlainText("延迟:"+b.get("latency")+"\n"));
                        mcb.append(new PlainText("协议版本:"+b.get("protocol_version")+"\n"));
                        mcb.append(new PlainText("游戏版本" + b.get("version") + "\n"));
                        mcb.append(new PlainText("在线人数:"+b.get("current_players")+"/"+b.get("max_players")+"\n"));
                        mcb.append(new PlainText("MOTD:" + convertUnicode(b.get("stripped_motd") + "\n")));
                        if (Objects.equals(b.getInteger("current_players"), 0)) {
                            mcb.append(new PlainText("玩家列表:无\n"));
                        }else if(b.getInteger("current_players")<=5) {
                            mcb.append(new PlainText("玩家列表:" + convertUnicode(b.getJSONArray("player_list").toString() + "\n")));
                        }else{
                            mcb.append(new PlainText("玩家列表:" + convertUnicode(b.getJSONArray("player_list").subList(0, 5).toString() + "等\n")));
                        }
                        if(b.getString("favicon_b64")!=null){
                            mcb.append(ExternalResource.uploadAsImage(ExternalResource.create(Base64.getDecoder().decode(b.getString("favicon_b64").replaceFirst(".+,", ""))),subject));
                        }

                    }else{
                        return new MessageChainBuilder().append(new PlainText("服务器查询失败，请稍后再试")).build();
                    }
                }
                for (Object a : je) {
                    if(a instanceof JSONObject b) {
                        mcb.append(new PlainText("[Java版]\n"));
                        mcb.append(new PlainText("地址:"+b.get("address")+"\n"));
                        mcb.append(new PlainText("IP:"+b.get("ip")+"\n"));
                        mcb.append(new PlainText("端口:"+b.get("port")+"\n"));
                        mcb.append(new PlainText("延迟:"+b.get("latency")+"\n"));
                        mcb.append(new PlainText("协议版本:"+b.get("protocol_version")+"\n"));
                        mcb.append(new PlainText("游戏版本" + b.get("version") + "\n"));
                        mcb.append(new PlainText("在线人数:"+b.get("current_players")+"/"+b.get("max_players")+"\n"));
                        mcb.append(new PlainText("MOTD:" + convertUnicode(b.get("stripped_motd") + "\n")));
                        if (Objects.equals(b.getInteger("current_players"), 0)) {
                            mcb.append(new PlainText("玩家列表:无\n"));
                        }else if(b.getInteger("current_players")<=5) {
                            mcb.append(new PlainText("玩家列表:" + convertUnicode(b.getJSONArray("player_list").toString() + "\n")));
                        }else{
                            mcb.append(new PlainText("玩家列表:" + convertUnicode(b.getJSONArray("player_list").subList(0, 5).toString() + "等\n")));
                        }
                        if(b.getString("favicon_b64")!=null){
                            mcb.append(ExternalResource.uploadAsImage(ExternalResource.create(Base64.getDecoder().decode(b.getString("favicon_b64").replaceFirst(".+,", ""))),subject));
                        }

                    }else{
                        return new MessageChainBuilder().append(new PlainText("服务器查询失败，请稍后再试")).build();
                    }
                }
                return mcb.build();
            }
        }catch (Exception e) {
            throw e;
        }
    }


    private static String convertUnicode(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\w{4}))");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String unicodeFull = matcher.group(1);
            String unicodeNum = matcher.group(2);
            char singleChar = (char) Integer.parseInt(unicodeNum, 16);
            str = str.replace(unicodeFull, singleChar + "");
        }
        return str;
    }
}
