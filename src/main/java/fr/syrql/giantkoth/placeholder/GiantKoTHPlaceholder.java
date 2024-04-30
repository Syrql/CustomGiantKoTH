package fr.syrql.giantkoth.placeholder;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.comparator.FactionPointsComparator;
import fr.syrql.giantkoth.data.FactionPoints;
import fr.syrql.giantkoth.data.KothPointsData;
import fr.syrql.giantkoth.utils.NumberUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GiantKoTHPlaceholder extends PlaceholderExpansion {

    private final CustomGiantKoTH shivana;

    public GiantKoTHPlaceholder(CustomGiantKoTH shivana) {
        this.shivana = shivana;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "giantkoth";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SYRQL";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (identifier.contains("kothpoints_isEnable")) {
            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData.isActive())
                return "true";
            else
                return "false";

        }

        if (identifier.contains("kothpoints_timer")) {
            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData == null || !kothData.isActive())
                return "✘";
            else {
                return DurationFormatUtils.formatDuration(kothData.getTimeLeft() * 1000L, "mm:ss");
            }
        }

        if (identifier.contains("kothpoints_maxpoints")) {
            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData == null || !kothData.isActive())
                return "✘";
            else {
                return String.valueOf(kothData.getMaxPoints());
            }
        }

        if (identifier.contains("kothpoints_me")) {
            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData == null || !kothData.isActive())
                return "✘";
            else {

                Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
                if (faction.isWilderness()) return "&4&l✘";

                FactionPoints factionPoints = this.shivana.getKothPointsManager().getFactionPointsByData(faction.getTag(), kothData);

                if (factionPoints == null) return "&4&l✘";

                return String.valueOf(factionPoints.getPoints());
            }
        }

        if (identifier.contains("kothpoints_capper_name")) {

            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData == null || !kothData.isActive())
                return "✘";
            else {

                LinkedList<FactionPoints> factionDungeons = new LinkedList<>(kothData.getCurrentCappers());

                List<FactionPoints> factions = factionDungeons.stream()
                        .sorted(new FactionPointsComparator())
                        .collect(Collectors.toList());

                if (NumberUtils.isInteger(args[2])) {
                    int number = Integer.parseInt(args[2]);

                    if (factions.size() < number) {
                        return "&4&l✘";
                    }

                    if (factions.get(number - 1) != null) {
                        return String.valueOf(factions.get(number - 1).getFactionData());
                    } else return "&4&l✘";
                }
            }
        }

        if (identifier.contains("kothpoints_capper_points")) {
            String[] args = identifier.split(":");

            KothPointsData kothData = this.shivana.getKothPointsManager().getKothProvider().get(args[1]);

            if (kothData == null || !kothData.isActive())
                return "✘";
            else {

                LinkedList<FactionPoints> factionDungeons = new LinkedList<>(kothData.getCurrentCappers());

                List<FactionPoints> factions = factionDungeons.stream()
                        .sorted(new FactionPointsComparator())
                        .collect(Collectors.toList());

                if (NumberUtils.isInteger(args[2])) {
                    int number = Integer.parseInt(args[2]);

                    if (factions.size() < number) {
                        return "&4&l✘";
                    }

                    if (factions.get(number - 1) != null) {
                        return String.valueOf(factions.get(number - 1).getPoints());
                    } else return "&4&l✘";
                }
            }
        }
        return null;
    }

}

