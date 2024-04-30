package fr.syrql.giantkoth.data;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

public class FactionPoints {

    private String factionData;
    private int points;

    public FactionPoints(String factionData, int points) {
        this.factionData = factionData;
        this.points = points;
    }

    public String getFactionData() {
        return factionData;
    }

    public void setFactionData(String factionData) {
        this.factionData = factionData;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
