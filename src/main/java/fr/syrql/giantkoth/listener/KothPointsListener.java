package fr.syrql.giantkoth.listener;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.data.FactionPoints;

import fr.syrql.giantkoth.data.KothPointsData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KothPointsListener implements Listener {

    private final CustomGiantKoTH shivana;

    public KothPointsListener(CustomGiantKoTH shivana) {
        this.shivana = shivana;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();

        if (faction.isWilderness()) return;

        for (KothPointsData kothPointsData : this.shivana.getKothPointsManager().getKothProvider().getKothPoints()) {
            FactionPoints factionPoints = this.shivana.getKothPointsManager().getFactionPointsByData(faction.getTag(), kothPointsData);

            if (factionPoints == null) return;

            factionPoints.setPoints((int) (factionPoints.getPoints() * 0.85));
        }
    }
}
