package io.github.et.utils.bilibili;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliVideoInfoFetcher {

    public static JSONObject getVideoInfo(String input) {
        try {
            String bvid = extractBVID(input);
            if (bvid == null || bvid.isEmpty()) {
                throw new BilibiliRequestException("无法提取BVID");
            }

            String apiUrl = "https://api.bilibili.com/x/web-interface/view?bvid=" + bvid;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            conn.setRequestProperty("Referer", "https://www.bilibili.com/");
            conn.connect();

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new BilibiliRequestException("请求失败，状态码：" + code);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();

            String jsonStr = sb.toString();
            return JSON.parseObject(jsonStr);

        } catch (Exception e) {
            throw new BilibiliRequestException("出错：" + e.getMessage());
        }
    }
    private static String extractBVID(String input) throws Exception {
        Pattern bvPattern = Pattern.compile("(BV[0-9a-zA-Z]+)");
        Matcher bvMatcher = bvPattern.matcher(input);
        if (bvMatcher.find()) {
            return bvMatcher.group(1);
        }else if (input.contains("\"qqdocurl\":")) {
            int start = input.indexOf("{");
            int end = input.lastIndexOf("}");
            if (start != -1 && end != -1 && end > start) {
                String jsonPart = input.substring(start, end + 1);
                JSONObject cardJson = JSON.parseObject(jsonPart);
                JSONObject meta = cardJson.getJSONObject("meta");
                if (meta != null) {
                    JSONObject detail1 = meta.getJSONObject("detail_1");
                    if (detail1 != null) {
                        String qqdocurl = detail1.getString("qqdocurl");
                        if ((qqdocurl != null) && (!qqdocurl.isEmpty())) {
                            Matcher m = bvPattern.matcher(qqdocurl);
                            if (m.find()) {
                                return m.group(1);
                            }
                            if (qqdocurl.contains("b23.tv")) {
                                String realUrl = followRedirect(qqdocurl);
                                if (realUrl != null) {
                                    Matcher m2 = bvPattern.matcher(realUrl);
                                    if (m2.find()) {
                                        return m2.group(1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (input.contains("b23.tv")) {
            String realUrl = followRedirect(input);
            if (realUrl != null) {
                Matcher m = bvPattern.matcher(realUrl);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        if (input.contains("video/")) {
            Pattern pattern = Pattern.compile("video/(BV[0-9a-zA-Z]+)");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    private static String followRedirect(String shortUrl) throws Exception {
        URL url = new URL(shortUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode == 301 || responseCode == 302 || responseCode == 303 ||
                responseCode == 307 || responseCode == 308) {
            String location = conn.getHeaderField("Location");
            conn.disconnect();
            if (location != null && !location.isEmpty()) {
                if (location.startsWith("/")) {
                    String host = url.getProtocol() + "://" + url.getHost();
                    location = host + location;
                }
                return location;
            }
        }
        conn.disconnect();
        return null;
    }

}