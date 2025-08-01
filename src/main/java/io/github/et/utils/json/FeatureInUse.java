package io.github.et.utils.json;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;

public class FeatureInUse {
    public static boolean isInUse(String name,long subjectId,String...parents){
        JSONObject j= Main.JSON_NO_GUIDE;
        for(String i:parents){
            j=j.getJSONObject(i);
        }
        return pre(j, name, subjectId);
    }
    private static boolean pre(JSONObject a,String name,long subjectId) {
        JSONObject b = a.getJSONObject(name);
        Boolean c = b.getBoolean(name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(name.charAt(0)).toLowerCase()));
        JSONArray d = b.getJSONArray("include");
        JSONArray e = b.getJSONArray("exclude");
        if (c && (!(e.contains((int)subjectId))||e.contains(subjectId))) {
            return true;
        } else if ((!c) && (d.contains((int)subjectId)||d.contains(subjectId))) {
            return true;
        }else{
            return false;
        }

    }
}
