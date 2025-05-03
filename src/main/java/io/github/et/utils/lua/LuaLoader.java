package io.github.et.utils.lua;

import io.github.et.Main;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

public class LuaLoader {
    private LuaValue lua;
    private String luaPath;
    private String luaName;
    private boolean isGame;
    private ArrayList<String> guide=new ArrayList<String>();
    private String help;
    private ArrayList<Item> items=new ArrayList<Item>();

    public LuaLoader(String luaPath) {
        this.luaPath=luaPath;
        load();
    }
    private void load(){
        this.lua= Main.globals.loadfile(this.luaPath).call();
        this.luaName=this.lua.get("name").tojstring();
    }
}
