package love.simbot.example.tools;


import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class API {
    public static String getApi(String url) throws IOException {
        URL url1=new URL (url);
        InputStream is =url1.openStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String b,result="";
        while ((b=br.readLine()) != null){
            result+=b;
        }
        JSONObject obj=JSONObject.fromObject(result);
        JSONObject result0=obj.getJSONObject("data");
        JSONObject result1=result0.getJSONObject("info");
        result = result1.getString("text");
        return result;
    }


}
