package fr.syrql.giantkoth.data;

import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.area.Area;
import fr.syrql.giantkoth.ranking.Ranking;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class KothPointsData {

    private String kothName;
    private boolean isActive;
    private int timeLeft;
    private int defaultTimeLeft;
    private Area area;
    private Area captureZone;
    private int maxPoints;
    private transient List<FactionPoints> currentCappers;

    public KothPointsData(String kothName, boolean isActive, int timeLeft, int defaultTimeLeft, Area area, Area captureZone, int maxPoints) {
        this.kothName = kothName;
        this.isActive = isActive;
        this.timeLeft = timeLeft;
        this.defaultTimeLeft = defaultTimeLeft;
        this.area = area;
        this.captureZone = captureZone;
        this.maxPoints = maxPoints;
        this.currentCappers = new ArrayList<>();
    }

    public KothPointsData(String kothName, boolean isActive) {
        this.kothName = kothName;
        this.isActive = isActive;
    }

    public String getKothName() {
        return kothName;
    }

    public void setKothName(String kothName) {
        this.kothName = kothName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getDefaultTimeLeft() {
        return defaultTimeLeft;
    }

    public void setDefaultTimeLeft(int defaultTimeLeft) {
        this.defaultTimeLeft = defaultTimeLeft;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getCaptureZone() {
        return captureZone;
    }

    public void setCaptureZone(Area captureZone) {
        this.captureZone = captureZone;
    }

    public List<FactionPoints> getCurrentCappers() {
        return currentCappers;
    }

    public void setCurrentCappers(List<FactionPoints> currentCappers) {
        this.currentCappers = currentCappers;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void createZone(Location locationOne, Location locationTwo, boolean isArea) {

        if (isArea) {
            Area zone = new Area(locationOne.getWorld().getName(), locationOne.getBlockX(), locationOne.getBlockZ(),
                    locationTwo.getBlockX(), locationTwo.getBlockZ());
            this.setArea(zone);
        } else {
            Area captureZone = new Area(locationOne.getWorld().getName(), locationOne.getBlockX(), locationOne.getBlockY(), locationOne.getBlockZ(),
                    locationTwo.getBlockX(), locationTwo.getBlockY(), locationTwo.getBlockZ());

            this.setCaptureZone(captureZone);
        }
    }

    public void start(int time, String message) {
        this.setActive(true);
        this.setTimeLeft(time);
        this.setDefaultTimeLeft(time);

        if (this.getCurrentCappers() != null) this.getCurrentCappers().clear();

        else this.setCurrentCappers(new ArrayList<>());

        Bukkit.broadcastMessage(message);
    }

    public void stop(CustomGiantKoTH shivana, List<String> message) {
        this.setActive(false);
        this.setTimeLeft(-1);

        for (Ranking ranking : shivana.getKothPointsManager().getRankings()) {

            if (shivana.getKothPointsManager().getFactionsPoints(this).size() < ranking.getNumber()) break;

            if (shivana.getKothPointsManager().getFactionsPoints(this).get(ranking.getNumber() - 1) == null) break;

            FactionPoints playerFast = shivana.getKothPointsManager().getFactionsPoints(this).get(ranking.getNumber() - 1);

            if (playerFast.getFactionData() != null) {
                Bukkit.getScheduler().runTask(shivana, () -> ranking.getCommands()
                        .forEach(line -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%faction%", playerFast.getFactionData()))));
            }
        }

        this.getCurrentCappers().clear();

        message.forEach(Bukkit::broadcastMessage);
    }
}
