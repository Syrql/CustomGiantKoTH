package fr.syrql.giantkoth;

import fr.syrql.giantkoth.commands.GiantKoTHCommand;
import fr.syrql.giantkoth.config.ConfigFile;
import fr.syrql.giantkoth.io.IOUtil;
import fr.syrql.giantkoth.manager.KothPointsManager;
import fr.syrql.giantkoth.placeholder.GiantKoTHPlaceholder;
import fr.syrql.giantkoth.task.KothPointsTask;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomGiantKoTH extends JavaPlugin {

    private IOUtil ioUtil;
    private ConfigFile configManager;
    private KothPointsManager kothPointsManager;
    private GiantKoTHPlaceholder citadelPlaceholder;

    @Override
    public void onEnable() {

        this.registerProvider();
        this.registerManager();
        this.registerCommands();
        this.registerListener();
        this.registerTask();

        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.citadelPlaceholder = new GiantKoTHPlaceholder(this);
            this.citadelPlaceholder.register();
        }
    }

    @Override
    public void onDisable() {

        this.kothPointsManager.disable();
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.citadelPlaceholder.unregister();
        }
    }

    private void registerManager() {
        this.configManager = new ConfigFile(this, "config.yml");
        this.kothPointsManager = new KothPointsManager(this);
    }

    private void registerProvider() {
        this.ioUtil = new IOUtil();
    }

    private void registerListener() {

    }

    private void registerCommands() {
        new GiantKoTHCommand(this);
    }

    private void registerTask() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new KothPointsTask(this), 1L, 20L);
    }

    public IOUtil getIoUtil() {
        return ioUtil;
    }

    public ConfigFile getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigFile configManager) {
        this.configManager = configManager;
    }

    public KothPointsManager getKothPointsManager() {
        return kothPointsManager;
    }

}
