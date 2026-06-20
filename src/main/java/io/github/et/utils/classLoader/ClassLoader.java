package io.github.et.utils.classLoader;

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
