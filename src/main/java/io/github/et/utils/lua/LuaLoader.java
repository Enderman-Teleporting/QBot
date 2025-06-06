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
    private ArrayList<String> guide = new ArrayList<>();
    private String help;
    private ArrayList<Item> items = new ArrayList<>();

    public LuaLoader(String luaPath) throws BotInfoNotFoundException, ClassNotFoundException {
        this.luaPath = luaPath;
        load();
    }

    private void load() throws BotInfoNotFoundException, ClassNotFoundException {
        this.lua = Main.globals.loadfile(this.luaPath).call();
        if (!this.lua.get("feature").isnil()) {
            this.isGame = false;
            luaName = this.lua.get("feature").tojstring();
            help = this.lua.get("help").isnil() ? null : this.lua.get("help").tojstring();
        } else if (!this.lua.get("game").isnil()) {
            this.isGame = true;
            luaName = this.lua.get("game").tojstring();
            help = this.lua.get("rule").isnil() ? null : this.lua.get("rule").tojstring();
        } else {
            throw new BotInfoNotFoundException("Error occurred when loading " + this.luaPath + ": feature or game name not found");
        }

        LuaValue guideTable = this.lua.get("guide");
        if (!guideTable.isnil()) {
            for (int i = 1; i <= guideTable.length(); i++) {
                LuaValue guideValue = guideTable.get(i);
                if (!guideValue.isnil()) {
                    this.guide.add(guideValue.tojstring());
                }
            }
        }

        LuaValue configTable = this.lua.get("config");
        if (!configTable.isnil()) {
            for (int i = 1; i <= configTable.length(); i++) {
                LuaValue configItem = configTable.get(i);
                if (!configItem.isnil()) {
                    String name = configItem.get("name").tojstring();
                    boolean nullable = configItem.get("nullable").checkboolean();
                    LuaValue typeTable = configItem.get("type");
                    ArrayList<String> types = new ArrayList<>();
                    for (int j = 1; j <= typeTable.length(); j++) {
                        types.add(typeTable.get(j).tojstring());
                    }
                    this.items.add(new Item(name, nullable, types.toArray(new String[0])));
                }
            }
        }
    }
}
