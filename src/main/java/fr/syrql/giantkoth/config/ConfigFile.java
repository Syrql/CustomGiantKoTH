package fr.syrql.giantkoth.config;

import fr.syrql.giantkoth.CustomGiantKoTH;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile extends YamlConfiguration {

    private final CustomGiantKoTH customGiantKoTH;
    private final File file;
    private final String prefix;

    public ConfigFile(CustomGiantKoTH customGiantKoTH, String name) {
        this.customGiantKoTH = customGiantKoTH;
        this.file = new File(customGiantKoTH.getDataFolder(), name);

        if (!this.file.exists()) {
            customGiantKoTH.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.prefix = ChatColor.translateAlternateColorCodes('&', this.customGiantKoTH.getConfig().getString("PREFIX"));
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return super.getConfigurationSection(name);
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', super.getString(path, "")).replace("%prefix%", prefix);
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(line -> ChatColor.translateAlternateColorCodes('&', line.replace("%prefix%", this.prefix))).collect(Collectors.toList());
    }
}