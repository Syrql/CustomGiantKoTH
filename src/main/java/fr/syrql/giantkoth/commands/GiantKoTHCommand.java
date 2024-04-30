package fr.syrql.giantkoth.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.data.KothPointsData;
import fr.syrql.giantkoth.provider.KothPointsProvider;
import fr.syrql.giantkoth.utils.NumberUtils;
import fr.syrql.giantkoth.utils.command.ACommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiantKoTHCommand extends ACommand {

    private final CustomGiantKoTH shivana;
    private final KothPointsProvider kothProvider;
    private final List<String> spacerUp;
    private final List<String> spacerDown;
    private final String formatKothList;
    private final List<String> allowedArgsConsole;
    private final List<String> allowedArgsAdmin;


    public GiantKoTHCommand(CustomGiantKoTH shivana) {
        super(shivana, "giantkoth", "command.giantkoth", true);
        this.shivana = shivana;
        this.kothProvider = this.shivana.getKothPointsManager().getKothProvider();
        this.spacerUp = this.shivana.getConfigManager().getStringList("KOTH_GIANT.SPACER_MESSAGE_UP");
        this.spacerDown = this.shivana.getConfigManager().getStringList("KOTH_GIANT.SPACER_MESSAGE_DOWN");
        this.formatKothList = this.shivana.getConfigManager().getString("KOTH_GIANT.LIST_FORMAT");
        this.allowedArgsAdmin = Arrays.asList("show", "start", "stop", "tp", "create", "remove", "setarea", "setzone");
        this.allowedArgsConsole = Arrays.asList("show", "start", "stop");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length == 0 || (args.length == 1 && !args[0].equalsIgnoreCase("list"))) {
            this.sendKothHelp(sender);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            this.sendKothList(sender);
            return true;
        }

        if (args.length == 2) {

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (args[0].equalsIgnoreCase("show")) {
                if (kothData == null) {
                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                    return true;
                }

                this.sendKothShow(sender, kothData);
                return true;
            }

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission("command.koth.admin")) {
                    if (!this.allowedArgsAdmin.contains(args[0])) {
                        this.sendKothHelp(player);
                        return true;
                    }

                    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                    Selection selection = worldEdit.getSelection(player);

                    switch (args[0]) {
                        case "stop":

                            if (this.kothProvider.get(args[1]) == null) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            String stopMessage = this.shivana.getConfigManager().getString("KOTH_GIANT.STOP_BROADCAST")
                                    .replace("%name%", kothData.getKothName());

                            kothData.stop(this.shivana, Collections.singletonList(stopMessage));
                            break;
                        case "tp":

                            if (this.kothProvider.get(args[1]) == null) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            if (!this.kothProvider.exist(kothData)) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            player.teleport(kothData.getCaptureZone().toLocationCenter());

                            break;
                        case "create":

                            if (this.kothProvider.get(args[1]) != null) {
                                player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NAME_ALREADY_EXIST"));
                                return true;
                            }

                            KothPointsData koth = new KothPointsData(args[1], false);
                            this.kothProvider.provide(args[1], koth);

                            player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.SUCCESS_CREATE")
                                    .replace("%name%", koth.getKothName()));
                            break;
                        case "setarea":

                            if (selection == null) {
                                player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_REGION_SELECTED"));
                                return true;
                            }

                            if (this.kothProvider.get(args[1]) == null) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            Location firstLocationArea = selection.getMaximumPoint();
                            Location secondLocationArea = selection.getMinimumPoint();

                            kothData.createZone(firstLocationArea, secondLocationArea, true);

                            player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.SUCCESS_CREATE_ZONE")
                                    .replace("%name%", kothData.getKothName()));
                            break;
                        case "setzone":

                            if (selection == null) {
                                player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_REGION_SELECTED"));
                                return true;
                            }

                            if (this.kothProvider.get(args[1]) == null) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            Location firstLocationZone = selection.getMaximumPoint();
                            Location secondLocationZone = selection.getMinimumPoint();

                            kothData.createZone(firstLocationZone, secondLocationZone, false);

                            player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.SUCCESS_CREATE_ZONE")
                                    .replace("%name%", kothData.getKothName()));
                            break;
                        case "remove":

                            if (this.kothProvider.get(args[1]) == null) {
                                sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                                return true;
                            }

                            this.kothProvider.remove(args[1]);

                            player.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.SUCCESS_REMOVE")
                                    .replace("%name%", args[1]));
                            break;
                        default:
                            this.sendKothHelp(player);
                            break;
                    }
                } else {
                    if (!sender.hasPermission("command.koth.admin")) {
                        this.sendKothHelp(sender);
                        return true;
                    }
                }
            } else {
                if (!this.allowedArgsConsole.contains(args[0])) {
                    this.sendKothHelp(sender);
                    return true;
                }
            }
        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("start")) {
                KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

                if (kothData == null || !this.kothProvider.exist(kothData)) {
                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                    return true;
                }

                if (kothData.isActive()) {
                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.ALREADY_START")
                            .replace("%name%", kothData.getKothName()));
                    return true;
                }

                int duration = NumberUtils.parseSeconds(args[2]);

                if (duration == -1) {
                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_DURATION_NOT_FOUND"));
                    return true;
                }

                String startMessage = this.shivana.getConfigManager().getString("KOTH_GIANT.SUCCESS_START_BROADCAST").replace("%name%", kothData.getKothName());
                kothData.start(duration, startMessage);
            }

            if (args[0].equalsIgnoreCase("points")) {
                KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

                if (kothData == null || !this.kothProvider.exist(kothData)) {
                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.NOT_EXIST"));
                    return true;
                }

                if (NumberUtils.isInteger(args[2])) {
                    int points = Integer.parseInt(args[2]);

                    kothData.setMaxPoints(points);

                    sender.sendMessage(this.shivana.getConfigManager().getString("KOTH_GIANT.UPDATE_POINTS"));
                }
            }

        }

        return true;
    }

    private void sendKothHelp(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("command.koth.admin"))
                this.shivana.getConfigManager().getStringList("KOTH_GIANT.ADMIN").forEach(sender::sendMessage);
            else
                this.shivana.getConfigManager().getStringList("KOTH_GIANT.PLAYER_NO_PERM").forEach(sender::sendMessage);

        } else {
            this.shivana.getConfigManager().getStringList("KOTH_GIANT.CONSOLE").forEach(sender::sendMessage);
        }
    }

    private void sendKothList(CommandSender sender) {

        String symbolNoCoords = this.shivana.getConfigManager().getString("KOTH_GIANT.SYMBOL_NO_COORDS");

        this.spacerUp.forEach(sender::sendMessage);

        this.shivana.getKothPointsManager().getKothProvider().getKothPoints().stream().filter(this.kothProvider::exist)
                .forEach(koth -> sender.sendMessage(this.formatKothList
                        .replace("%koth_name%", koth.getKothName())
                        .replace("%x%", !this.kothProvider.exist(koth) ? symbolNoCoords : String.valueOf(koth.getCaptureZone()
                                .getCenterX()))
                        .replace("%z%", !this.kothProvider.exist(koth) ? symbolNoCoords : String.valueOf(koth.getCaptureZone()
                                .getCenterZ()))
                        .replace("%world%", !this.kothProvider.exist(koth) ? symbolNoCoords : koth.getArea().getWorld())));

        this.spacerDown.forEach(sender::sendMessage);
    }

    private void sendKothShow(CommandSender sender, KothPointsData koth) {

        String symbolNoCoords = this.shivana.getConfigManager().getString("KOTH_GIANT.SYMBOL_NO_COORDS");

        this.shivana.getConfigManager().getStringList("KOTH_GIANT.SHOW_MESSAGE").forEach(l ->
                sender.sendMessage(l.replace("%koth_name%", koth.getKothName())
                        .replace("%x%", koth.getArea() == null ? symbolNoCoords : String.valueOf(koth.getCaptureZone()
                                .getCenterX()))
                        .replace("%z%", koth.getArea() == null ? symbolNoCoords : String.valueOf(koth.getCaptureZone()
                                .getCenterZ()))));
    }
}
