package io.github.et.tools;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GPT {
    private static ConcurrentHashMap<Long,List<JSONObject>> messageHistory = new ConcurrentHashMap<>();
    public static String getReply(long groupNum,String question) {
        if(!messageHistory.containsKey(groupNum)) {
            messageHistory.put(groupNum,new ArrayList<>());
        }
        try {
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messageHistory.get(groupNum).add(userMessage);
            /*TODO if(messageHistory.get(groupNum).size() > Integer.parseInt((String) Main.botInfo.getOrDefault("Max_Message_Count","16"))) {
                messageHistory.get(groupNum).remove(0);
            }*/
            JSONObject info = new JSONObject();
            //TODO info.put("model", Main.botInfo.getOrDefault("model", "gpt-4o-mini"));
            info.put("messages", messageHistory.get(groupNum));
            HttpURLConnection connection = (HttpURLConnection) new URL(Main.URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + Main.APIKEY);
            connection.setRequestProperty("x-foo", "true");
            connection.setDoOutput(true);
            String jsonInputString = info.toJSONString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JSONObject jsonObject = JSONObject.parseObject(response.toString());
            String content = jsonObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
            if(content==null) {
                throw new NullPointerException("content is null");
            } else if(content.equals("null")){
                throw new NullPointerException("content is null");
            } else {
                JSONObject assistantMessage = new JSONObject();
                assistantMessage.put("role", "assistant");
                assistantMessage.put("content", content);
                messageHistory.get(groupNum).add(assistantMessage);
            }
            return content;

        } catch (Exception e) {
            messageHistory.get(groupNum).clear();
            return "出错: " + e.getMessage();
        }
    }

    public static String freeSpeech(ArrayList<String> context, String name) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String prompt="你叫"+name+"，你是一个性格温和的人。在日常生活里，你是"+/* TODO Main.botInfo.getOrDefault("role","一名在江苏省常熟中学学习的中学生")+*/"。生日随便编。\n" +
                "你在一个qq群中，我会以\"A:xxx B:xxx 你:?\"的格式输入不全的聊天记录，你需要想想在这个情境下你接下来会说什么，然后补充\"你:?\"中你说的话，你补全的话题应该符合你的人设，不要讨论别的话题或试图结束聊天，你补全聊天的时候应该尽量附和之前的人说的话。\n" +
                "你补全的风格应该较为温和，简短且口语化，切忌把话说的完整或太长，切忌使用书面语言。还有，你补全的长度应该与其他人发信息的长度差不多，尽量简短，要尽可能口语化。\n" +
                "不要随意透露你的个人信息。记住用简体中文回答。\n" +
                "请注意现在的北京时间是" +dateFormat.format(date)+"\n" +
                "最后，请把你的回答精简到20字以内，并输出。";
        for (String s : context) {
            prompt+="\n"+s;
        }
        prompt+="\n你:?";
        try {
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            JSONObject info = new JSONObject();
            /*TODO info.put("model", Main.botInfo.getOrDefault("model", "gpt-4o-mini"));*/
            info.put("messages", message);
            HttpURLConnection connection = (HttpURLConnection) new URL(Main.URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + Main.APIKEY);
            connection.setRequestProperty("x-foo", "true");
            connection.setDoOutput(true);
            String jsonInputString = info.toJSONString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JSONObject jsonObject = JSONObject.parseObject(response.toString());
            String content = jsonObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
            if (content == null) {
                throw new NullPointerException("content is null");
            } else if (content.equals("null")) {
                throw new NullPointerException("content is null");
            }  else {
                return content;
            }
        } catch (Exception ignored) {
            return null;
        }
    }
}
