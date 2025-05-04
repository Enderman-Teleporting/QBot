package io.github.et.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import io.github.et.exceptions.BotInfoNotFoundException;

import java.io.*;

public class JsonReader {
    public static JSONObject jsonObject;
    private static BufferedReader br;
    private static BufferedWriter bw;
    public static void init() throws BotInfoNotFoundException {
        try {
            br = new BufferedReader(new FileReader("./botInfo.json"));
            bw = new BufferedWriter(new FileWriter("./botInfo.json"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = JSONObject.parseObject(sb.toString());
            update();
            System.out.println("Successfully loaded bot info");
        }catch (IOException e){
            throw new BotInfoNotFoundException("Cannot load bot info from file: ./botInfo.json");
        }
    }

    public static void update() throws IOException {
        String str= JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        bw.write(str);
        bw.flush();
    }
    //TODO JSONArray
}
