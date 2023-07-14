package tools;


import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class API {
    public static String getApi(String url,String key) throws IOException {
        URL url1=new URL (url);
        InputStream is =url1.openStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String result =br.readLine();
        JSONObject obj=JSONObject.fromObject(result);
        result=obj.getString(key);
        result = result.replace("{br}", "\n");
        return result;
    }


}
