package fr.syrql.giantkoth.manager;

import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.comparator.FactionPointsComparator;
import fr.syrql.giantkoth.data.FactionPoints;
import fr.syrql.giantkoth.data.KothPointsData;
import fr.syrql.giantkoth.listener.KothPointsListener;
import fr.syrql.giantkoth.provider.KothPointsProvider;
import fr.syrql.giantkoth.ranking.Ranking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class KothPointsManager {

    private final CustomGiantKoTH shivana;
    private final KothPointsProvider kothProvider;
    private List<Ranking> rankings;

    public KothPointsManager(CustomGiantKoTH shivana) {
        this.shivana = shivana;
        this.kothProvider = new KothPointsProvider(this.shivana);
        this.kothProvider.read(false);
        this.shivana.getServer().getPluginManager().registerEvents(new KothPointsListener(this.shivana), this.shivana);
        this.setupRewards();
    }

    public void setupRewards() {
        this.rankings = new ArrayList<>();

        for (String key : this.shivana.getConfigManager().getSection("KOTH_GIANT_REWARDS.REWARDS").getKeys(false)) {
            String path = "KOTH_GIANT_REWARDS.REWARDS." + key + ".";

            int number = this.shivana.getConfigManager().getInt(path + "NUMBER");
            List<String> commands = this.shivana.getConfigManager().getStringList(path + "COMMANDS");

            this.rankings.add(new Ranking(number, commands));
        }
    }

    public void disable() {
        this.kothProvider.write();
    }

    public KothPointsProvider getKothProvider() {
        return kothProvider;
    }

    public List<FactionPoints> getFactionsPoints(KothPointsData kothPointsData) {
        LinkedList<FactionPoints> factionDungeons = new LinkedList<>(kothPointsData.getCurrentCappers());

        return factionDungeons.stream()
                .sorted(new FactionPointsComparator())
                .collect(Collectors.toList());
    }

    public FactionPoints getFactionPointsByData(String factionData, KothPointsData kothPointsData) {

        if (kothPointsData.getCurrentCappers() == null) kothPointsData.setCurrentCappers(new ArrayList<>());

        return kothPointsData.getCurrentCappers().stream().filter(factionPoints -> factionPoints.getFactionData().equalsIgnoreCase(factionData))
                .findFirst().orElse(null);
    }

    public List<Ranking> getRankings() {
        return rankings;
    }
}
