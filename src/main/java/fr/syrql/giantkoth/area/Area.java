package fr.syrql.giantkoth.area;

import fr.syrql.giantkoth.utils.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Area {

    private String world;
    private int x1, y1, z1, x2, y2, z2;

    public Area(String world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = world;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public Area(String world, int x1, int z1, int x2, int z2) {
        this.world = world;
        this.x1 = x1;
        this.y1 = 256;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = 0;
        this.z2 = z2;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getZ1() {
        return z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getZ2() {
        return z2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    public Location getFirstPoint() {
        return new Location(Bukkit.getWorld(this.world), this.x1, this.y1, this.z1);
    }

    public Location getSecondPoint() {
        return new Location(Bukkit.getWorld(this.world), this.x2, this.y2, this.z2);
    }

    public Cuboid getCuboid() {
        return new Cuboid(this.getFirstPoint(), this.getSecondPoint());
    }

    public int getCenterX() {
        return (this.x1 + this.x2) / 2;
    }

    public int getCenterY() {
        return (this.y1 + this.y2) / 2;
    }

    public int getCenterZ() {
        return (this.z1 + this.z2) / 2;
    }

    public Location toLocationCenter() {
        return new Location(Bukkit.getWorld(this.world), this.getCenterX(), this.getCenterY(), this.getCenterZ());

    }
}
