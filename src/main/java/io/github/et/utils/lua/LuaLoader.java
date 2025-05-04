package io.github.et.utils.lua;

import io.github.et.Main;
import io.github.et.exceptions.BotInfoNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

@Getter
@Setter
public class LuaLoader {
    private LuaValue lua;
    private String luaPath;
    private String luaName;
    private boolean isGame;
    private ArrayList<String> guide= new ArrayList<>();
    private String help;
    private ArrayList<Item> items= new ArrayList<>();

    public LuaLoader(String luaPath) throws BotInfoNotFoundException, ClassNotFoundException {
        this.luaPath=luaPath;
        load();
    }
    private void load() throws BotInfoNotFoundException, ClassNotFoundException {
        this.lua= Main.globals.loadfile(this.luaPath).call();
        if(this.lua.get("feature").isnil()){
            this.isGame=false;
            luaName=this.lua.get("feature").tojstring();
            help=this.lua.get("help").tojstring();
        } else if (this.lua.get("game").isnil()) {
            this.isGame=true;
            luaName=this.lua.get("game").tojstring();
            help=this.lua.get("rule").tojstring();
        }else{
            throw new BotInfoNotFoundException("Error occurred when loading "+this.luaPath+": feature or game name not found");
        }
        for (int i = 0; i < this.lua.get("guide").length(); i++) {
            this.guide.add(this.lua.get("guide").get(i).tojstring());
        }
        for (int i = 1; i <= this.lua.get("config").length(); i++) {
            LuaValue l=this.lua.get("config").get(i);
            ArrayList<String> a = new ArrayList<>();
            for(int j=1;j<=l.length();j++){
                a.add(l.get("type").get(j).tojstring());
            }
            this.items.add(new Item(l.get("name").tojstring(),l.get("nullable").checkboolean(),a.toArray(new String[0])));
        }
    }
}
