package love.simbot.example.tools;


import catcode.CatCodeUtil;
import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class API {
    public static String getApi(String url,String key) throws IOException {
        String a="",b="";
        URL url1=new URL (url);
        InputStream is =url1.openStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String result =br.readLine();
        JSONObject obj=JSONObject.fromObject(result);
        result=obj.getString(key);
        result = result.replace("{br}", "\n");
        char[] msgArray=result.toCharArray();
        for (int i=1;i<=msgArray.length;i++){
            if (msgArray[i]=='{'){
                for (int j=i;msgArray[j-1]!='}';j++){
                    a+=msgArray[j];
                    if (msgArray[j-1]==':'){
                        for (int k=j;msgArray[k]!='}';k++){
                            b+=msgArray[k];
                        }
                    }
                }
                final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
                String faceString = catUtil.toCat("face", true, "id="+b);
                result.replace(a,faceString);
            }
        }
        return result;
    }

}
