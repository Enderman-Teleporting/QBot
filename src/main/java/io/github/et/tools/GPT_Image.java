package io.github.et.tools;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GPT_Image {
    public static String generateImage(String prompt) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Main.Image_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + Main.APIKEY);
            connection.setDoOutput(true);
            String jsonInputString = "{"
                    + "\"model\": \"" + Main.JSON_NO_GUIDE.getJSONObject("Reply").getJSONObject("Image").getString("imageModel") + "\","
                    + "\"prompt\": \"" + prompt.replace("\n",", ") + "\","
                    + "\"n\": 1,"
                    + "\"size\": \"1024x1024\""
                    + "}";
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
            String imageUrl = jsonObject.getJSONArray("data")
                    .getJSONObject(0)
                    .getString("url");

            return imageUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "生成图片失败: " + e.getMessage();
        }
    }

    public static byte[] getUrlByByte(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(60000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Connection", "keep-alive");
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Request failed with response code: " + responseCode);
        }
        try (InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
