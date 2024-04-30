package fr.syrql.giantkoth.utils.command;

import fr.syrql.giantkoth.CustomGiantKoTH;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class ACommand implements CommandExecutor {

    private final String commandName;
    private final String permission;
    private final boolean consoleCanExecute;
    private final CustomGiantKoTH customCitadel;

    public ACommand(@NotNull CustomGiantKoTH customCitadel, @NotNull String commandName, @NotNull String permission, boolean consoleCanExecute) {
        this.permission = permission;
        this.commandName = commandName;
        this.consoleCanExecute = consoleCanExecute;
        this.customCitadel = customCitadel;
        Objects.requireNonNull(customCitadel.getCommand(commandName)).setExecutor(this);
    }

    public ACommand(@NotNull CustomGiantKoTH customCitadel, @NotNull String commandName, @NotNull String permission, boolean consoleCanExecute, List<String> aliases) {
        this.permission = permission;
        this.commandName = commandName;
        this.consoleCanExecute = consoleCanExecute;
        this.customCitadel = customCitadel;
        Objects.requireNonNull(customCitadel.getCommand(commandName)).setAliases(aliases);
        Objects.requireNonNull(customCitadel.getCommand(commandName)).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!command.getLabel().equalsIgnoreCase(commandName))
            return true;

        if (!consoleCanExecute && !(sender instanceof Player)) {
            sender.sendMessage(customCitadel.getConfigManager().getString("NOT_PLAYER"));
            return true;
        }

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(customCitadel.getConfigManager().getString("NO_PERMISSION"));
            return true;
        }

        return execute(sender, args);
    }

    public abstract boolean execute(CommandSender sender, String[] args);
}
