package io.github.et.tools;

import com.alibaba.fastjson2.JSONArray;
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
    private static ConcurrentHashMap<Long, List<JSONObject>> messageHistory = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, List<JSONObject>> context = new ConcurrentHashMap<>();

    public static String getReply(long groupNum, String question) {
        if (!messageHistory.containsKey(groupNum)) {
            messageHistory.put(groupNum, new ArrayList<>());
        }
        try {
            JSONObject userMessage = new JSONObject();
            String[] a=question.split("~`\\+=");
            StringBuilder sb = new StringBuilder();
            userMessage.put("role", "user");
            if(question.contains("~`+=")) {
                JSONArray ja = new JSONArray();
                for (String i : a) {
                    if (!i.startsWith(":img:")) {
                        sb.append(i);
                    } else {
                        JSONObject jo=new JSONObject();
                        sb.append("[图片]");
                        jo.put("type", "image_url");
                        JSONObject temp = new JSONObject();
                        temp.put("url", i.substring(18, i.length() - 1));
                        jo.put("image_url", temp);
                        ja.add(jo);
                    }
                }
                JSONObject jo = new JSONObject();
                jo.put("type","text");
                jo.put("text",sb.toString());
                ja.add(jo);
                userMessage.put("content", ja);
            }else{
                userMessage.put("content", question);
            }

            messageHistory.get(groupNum).add(userMessage);
            if (messageHistory.get(groupNum).size() > Integer.parseInt((String) Main.JSON_NO_GUIDE.getOrDefault("Max_Message_Count", "16"))) {
                messageHistory.get(groupNum).remove(0);
            }
            JSONObject info = new JSONObject();
            info.put("model", Main.JSON_NO_GUIDE.getJSONObject("Reply").getOrDefault("model", "gpt-4o-mini"));
            info.put("messages", messageHistory.get(groupNum));
            HttpURLConnection connection = (HttpURLConnection) new URL(Main.URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
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

    public static String freeSpeech(Long groupNum) {
        try {
            JSONObject info = new JSONObject();
            info.put("model", Main.JSON_NO_GUIDE.getJSONObject("Reply").getOrDefault("model", "gpt-4o-mini"));
            info.put("messages", context.get(groupNum));
            info.put("max_tokens", 30);
            HttpURLConnection connection = (HttpURLConnection) new URL(Main.URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
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
            } else {
                JSONObject assistantMessage = new JSONObject();
                assistantMessage.put("role", "assistant");
                assistantMessage.put("content", content);
                context.get(groupNum).add(assistantMessage);
            }
            return content;

        } catch (Exception e) {
            context.get(groupNum).clear();
            return null;
        }
    }
}
