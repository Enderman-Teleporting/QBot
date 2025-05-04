package io.github.et.utils.json;

import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.utils.lua.LuaLoader;

import java.io.File;
import java.util.ArrayList;

public class JsonBuilder {
    public static ArrayList<LuaLoader> luas= new ArrayList<>();

    public static void initAll() throws BotInfoNotFoundException, ClassNotFoundException {
        ArrayList<String> luaFiles = findLuaFiles("./configs/addonConfigs");
        for (String luaFile : luaFiles) {
            luas.add(new LuaLoader(luaFile));
        }
    }








    public static ArrayList<String> findLuaFiles(String folderPath) {
        ArrayList<String> luaFiles = new ArrayList<>();
        File directory = new File(folderPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return luaFiles;
        }
        traverseDirectory(directory, luaFiles);
        return luaFiles;
    }

    private static void traverseDirectory(File dir, ArrayList<String> result) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".lua")) {
                    result.add(file.getAbsolutePath());
                }
            } else if (file.isDirectory()) {
                traverseDirectory(file, result);
            }
        }
    }

}
