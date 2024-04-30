package fr.syrql.giantkoth.task;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import fr.syrql.giantkoth.CustomGiantKoTH;

import fr.syrql.giantkoth.data.FactionPoints;
import fr.syrql.giantkoth.data.KothPointsData;
import fr.syrql.giantkoth.provider.KothPointsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KothPointsTask implements Runnable {

    private final CustomGiantKoTH shivana;
    private final List<Player> inArea;
    private final List<Player> inZone;
    private final String entryZoneMessage;
    private final String quitZoneMessage;
    private final String entryZoneCapMessage;
    private final String quitZoneCapMessage;
    private final KothPointsProvider kothProvider;

    public KothPointsTask(CustomGiantKoTH shivana) {
        this.shivana = shivana;
        this.inArea = new ArrayList<>();
        this.inZone = new ArrayList<>();
        this.entryZoneMessage = this.shivana.getConfigManager().getString("KOTH_ENTRY_ZONE");
        this.quitZoneMessage = this.shivana.getConfigManager().getString("KOTH_LEAVE_ZONE");

        this.entryZoneCapMessage = this.shivana.getConfigManager().getString("KOTH_GIANT.ENTRY_ZONE_CAP");
        this.quitZoneCapMessage = this.shivana.getConfigManager().getString("KOTH_GIANT.LEAVE_ZONE_CAP");

        this.kothProvider = this.shivana.getKothPointsManager().getKothProvider();
    }

    @Override
    public void run() {
        this.shivana.getKothPointsManager().getKothProvider().getKothPoints()
                .stream()
                .filter(this.kothProvider::exist)
                .filter(KothPointsData::isActive)
                .forEach(kothPointsData -> {
                    if (kothPointsData.isActive()) {
                        if (kothPointsData.getCurrentCappers() == null)
                            kothPointsData.setCurrentCappers(new ArrayList<>());
                        if (kothPointsData.getTimeLeft() > 0) {
                            kothPointsData.setTimeLeft(kothPointsData.getTimeLeft() - 1);
                        } else {

                            List<String> messageEnd = new ArrayList<>();

                            this.shivana.getConfigManager().getStringList("KOTH_GIANT.END_MESSAGE_NO_WINNER").forEach(l ->
                                    messageEnd.add(l.replace("%name%", kothPointsData.getKothName())));

                            this.inZone.clear();
                            this.inArea.clear();

                            kothPointsData.stop(this.shivana, messageEnd);
                        }

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (!inArea.contains(player) && kothPointsData.getArea().getCuboid().isIn(player)) {
                                this.inArea.add(player);
                                player.sendMessage(this.entryZoneMessage.replace("%name%", kothPointsData.getKothName()));
                            }

                            if (!kothPointsData.getArea().getCuboid().isIn(player) && this.inArea.contains(player)) {
                                this.inArea.remove(player);
                                player.sendMessage(this.quitZoneMessage.replace("%name%", kothPointsData.getKothName()));
                            }

                            if (!inZone.contains(player) && kothPointsData.getCaptureZone().getCuboid().isIn(player)) {
                                this.inZone.add(player);
                                player.sendMessage(this.entryZoneCapMessage.replace("%name%", kothPointsData.getKothName()));

                                Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
                                if (faction.isWilderness()) return;

                                if (this.shivana.getKothPointsManager().getFactionPointsByData(faction.getTag(), kothPointsData) == null) {
                                    FactionPoints factionPoints = new FactionPoints(faction.getTag(), 0);
                                    kothPointsData.getCurrentCappers().add(factionPoints);
                                }
                            }

                            if (!kothPointsData.getCaptureZone().getCuboid().isIn(player) && this.inZone.contains(player)) {
                                this.inZone.remove(player);
                                player.sendMessage(this.quitZoneCapMessage.replace("%name%", kothPointsData.getKothName()));
                            }
                        });

                        for (FactionPoints capper : kothPointsData.getCurrentCappers()) {

                            List<Player> players = Factions.getInstance().getByTag(capper.getFactionData())
                                    .getOnlinePlayers().stream()
                                    .filter(player -> kothPointsData.getCaptureZone().getCuboid().isIn(player))
                                    .collect(Collectors.toList());

                            if (!players.isEmpty()) {

                                if (capper.getPoints() >= kothPointsData.getMaxPoints()) {

                                    List<String> messageEnd = new ArrayList<>();

                                    this.shivana.getConfigManager().getStringList("KOTH_GIANT.END_MESSAGE").forEach(l ->
                                            messageEnd.add(l.replace("%name%", kothPointsData.getKothName())
                                                    .replace("%faction_name%", capper.getFactionData())));

                                    this.inZone.clear();
                                    this.inArea.clear();
                                    kothPointsData.stop(this.shivana, messageEnd);
                                    return;
                                }

                                capper.setPoints(capper.getPoints() + 1);

                            }
                        }
                    }
                });

    }
}
