package io.github.et.utils.pluginLoader.lua;

import io.github.et.utils.pluginLoader.exceptions.BotLuaConfigException;
import lombok.Getter;
import org.luaj.vm2.lib.jse.LuajavaLib;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class LuaConfig {
    private String packageName;
    private String featureName;
    private boolean isGame;
    private HashMap<String, Object> config=new HashMap<>();
    public static ArrayList<LuaConfig> luaConfigArrayList = new ArrayList<>();

    public LuaConfig(boolean isGame,String packageName,String name) throws BotLuaConfigException {
        if(!name.matches("^[A-Z][a-zA-Z_]+$")){
            throw new BotLuaConfigException("LuaConfig name must start with a capital letter and contain only letters and underscores");
        }
        this.packageName=packageName;
        this.featureName=name;
        this.isGame=isGame;
        config.put(isGame ? "game" : "feature", name);
        config.put("cn", LuajavaLib.NIL);
        config.put("guide", new ArrayList<String>());
        config.put(isGame?"rule":"help", LuajavaLib.NIL);
        config.put("config",new ArrayList<HashMap<String,Object>>());
        HashMap<String,Object> defaultConfig=new HashMap<>();
        defaultConfig.put("name",String.valueOf(name.charAt(0)).toLowerCase()+name.substring(1));
        ArrayList<String> type=new ArrayList<>();
        type.add("java.lang.Boolean");
        defaultConfig.put("type",type);
        defaultConfig.put("nullable",false);
        ((ArrayList<HashMap<String,Object>>)config.get("config")).add(defaultConfig);
    }

    public void setCn(String cn_name){
        config.put("cn", cn_name);
    }
    public void addGuide(String guide){
        ((ArrayList<String>)config.get("guide")).add(guide);
    }

    public void setRuleOrHelp(String ruleOrHelp){
        if(isGame){
            config.put("rule", ruleOrHelp);
        }else{
            config.put("help", ruleOrHelp);
        }
    }

    public void addConfig(String name, ArrayList<String> type,boolean nullable) throws BotLuaConfigException {
        if(type.isEmpty()){
            throw new BotLuaConfigException("Config type must have at least one element");
        }
        for (String i:type){
            if(!(i.equals("java.lang.String")||i.equals("java.lang.Boolean")||i.equals("java.lang.Integer")||i.equals("java.lang.Double")||i.equals("java.lang.Long")||i.equals("com.alibaba.fastjson.JSONArray"))){
                throw new BotLuaConfigException("Config type must be java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Double, java.lang.Long or com.alibaba.fastjson.JSONArray");
            }
        }
        HashMap<String, Object> configItem = new HashMap<>();
        configItem.put("name", name);
        configItem.put("type", type);
        configItem.put("nullable", nullable);


    }
    public void register(){
        luaConfigArrayList.add(this);
    }

    @Override
    public String toString() {
        return this.getPackageName()+"."+featureName;
    }


}
