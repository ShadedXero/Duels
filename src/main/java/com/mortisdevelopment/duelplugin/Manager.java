package com.mortisdevelopment.duelplugin;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Manager<T extends Nameable> {

    private final File dataFolder;
    private final Map<String, T> objectByName;

    public Manager(File dataFolder) {
        this.dataFolder = dataFolder;
        dataFolder.mkdirs();
        this.objectByName = new HashMap<>();
        loadObjects();
    }

    public abstract void saveObject(T object);

    public abstract T getObject(FileConfiguration config);

    private void loadObjects() {
        File[] files = dataFolder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            T object = getObject(YamlConfiguration.loadConfiguration(file));
            objectByName.put(object.getName(), object);
        }
    }

    public T getObject(String name) {
        return objectByName.get(name);
    }

    public void addObject(T object) {
        objectByName.put(object.getName(), object);
        saveObject(object);
    }

    public void deleteObject(String name) {
        if (objectByName.remove(name) != null) {
            getFile(name).delete();
        }
    }

    public File getFile(String name) {
        return new File(dataFolder, name + ".yml");
    }

    public List<T> getObjects() {
        return new ArrayList<>(objectByName.values());
    }
}
