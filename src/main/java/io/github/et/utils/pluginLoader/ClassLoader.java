package io.github.et.utils.pluginLoader;

import net.mamoe.mirai.event.SimpleListenerHost;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoader {
    public static List<Class<?>> loadClasses() throws Exception {
        //TODO 加载特定子类(父类未写)
        //TODO 使用@useConfig注解向子类中特定的HashMap中添加特定配制的路径以便直接取用
        List<Class<?>> classes = new ArrayList<>();
        java.lang.ClassLoader classLoader = ClassLoader.class.getClassLoader();
        
        File pluginsDir = new File("plugins");
        if (pluginsDir.exists() && pluginsDir.isDirectory()) {
            for (File jarFile : pluginsDir.listFiles()) {
                if (jarFile.getName().endsWith(".jar")) {
                    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, classLoader);
                    try (JarFile jar = new JarFile(jarFile)) {
                        java.util.Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            if (entry.getName().endsWith(".class")) {
                                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                                try {
                                    Class<?> clazz = jarClassLoader.loadClass(className);
                                    if (SimpleListenerHost.class.isAssignableFrom(clazz)) {
                                        classes.add(clazz);
                                    }
                                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return classes;
    }
}
