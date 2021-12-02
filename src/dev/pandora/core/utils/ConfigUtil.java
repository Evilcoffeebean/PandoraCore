package dev.pandora.core.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zvijer on 31.3.2017..
 */
public class ConfigUtil {

    class Return<T> {

        private ConfigUtil util;

        public Return(ConfigUtil configUtil) {
            this.util = configUtil;
        }

        @SuppressWarnings("unchecked")
        public T fromConfig(String path) {
            return (T) util.getConfig().get(path);
        }
    }

    private File file;
    private FileConfiguration config;

    public ConfigUtil(File file) {
        this(file, null);
    }

    public ConfigUtil(File file, FileConfiguration config) {
        this.file = file;
        this.config = config != null ? config : YamlConfiguration.loadConfiguration(file);
        init();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public <T> T getValue(String path) {
        return new Return<T>(this).fromConfig(path);
    }

    protected final void save() throws IOException {
        getConfig().save(file);
    }

    protected final void init() {
        try {
            if (!file.exists()) {
                save();
            } else {
                config = YamlConfiguration.loadConfiguration(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void setValue(String path, T value) {
        try {
            if (config != null && file.exists()) {
                getConfig().set(path, value);
                save();
            } else {
                throw new NullPointerException("No valid configuration destination could have been found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
