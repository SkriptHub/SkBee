package com.shanebeestudios.skbee.elements.bound.objects;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
@SerializableAs("Bound")
public class Bound implements ConfigurationSerializable {

    private final String world;
    private String id;
    private List<UUID> owners = new ArrayList<>();
    private List<UUID> members = new ArrayList<>();
    private Map<String, Object> values = new HashMap<>();
    private BoundingBox boundingBox;

    /**
     * Create a new bound in a {@link World} with ID using a {@link BoundingBox}
     *
     * @param world       World this bound is in
     * @param id          ID of this bound
     * @param boundingBox BoundingBox of this bound
     */
    public Bound(String world, String id, BoundingBox boundingBox) {
        this.world = world;
        this.id = id;
        this.boundingBox = boundingBox;
    }

    /**
     * Create a new bound between 2 locations (must be in same world)
     *
     * @param location  Location 1
     * @param location2 Location 2
     * @param id        ID of this bound
     */
    public Bound(Location location, Location location2, String id) {
        Preconditions.checkArgument(location.getWorld() == location2.getWorld(), "Worlds have to match");
        this.world = location.getWorld().getName();
        this.id = id;
        this.boundingBox = BoundingBox.of(location, location2);
    }

    /**
     * Check if a location is within the region of this bound
     *
     * @param loc Location to check
     * @return True if location is within this bound
     */
    public boolean isInRegion(Location loc) {
        if (!Objects.requireNonNull(loc.getWorld()).getName().equals(world)) return false;
        return this.boundingBox.contains(loc.toVector());
    }

    /**
     * Get the entities within this Bound
     * <p>Note: If the chunk is unloaded, the entities will also be unloaded</p>
     *
     * @param type Type of entity to get
     * @return List of loaded entities in bound
     */
    public List<Entity> getEntities(Class<? extends Entity> type) {
        World world = getWorld();
        if (world == null) return null;
        Collection<Entity> nearbyEntities = world.getNearbyEntities(this.boundingBox, entity -> type.isAssignableFrom(entity.getClass()));
        return new ArrayList<>(nearbyEntities);
    }

    /**
     * Get a list of blocks within a bound
     *
     * @return List of blocks within bound
     */
    public List<Block> getBlocks() {
        World w = getWorld();
        if (w == null) return null;
        List<Block> array = new ArrayList<>();
        int x = (int) boundingBox.getMinX();
        int y = (int) boundingBox.getMinY();
        int z = (int) boundingBox.getMinZ();
        int x2 = (int) boundingBox.getMaxX();
        int y2 = (int) boundingBox.getMaxY();
        int z2 = (int) boundingBox.getMaxZ();
        for (int x3 = x; x3 <= x2; x3++) {
            for (int y3 = y; y3 <= y2; y3++) {
                for (int z3 = z; z3 <= z2; z3++) {
                    Block b = w.getBlockAt(x3, y3, z3);
                    array.add(b);
                }
            }
        }
        return array;
    }

    /**
     * Get the world of this bound
     *
     * @return World of this bound
     */
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    /**
     * Get the greater corner of this bound
     *
     * @return Location of greater corner
     */
    public Location getGreaterCorner() {
        World world = getWorld();
        if (world == null) return null;
        return boundingBox.getMax().toLocation(world);
    }

    /**
     * Get the lesser corner of this bound
     *
     * @return Location of lesser corner
     */
    public Location getLesserCorner() {
        World world = getWorld();
        if (world == null) return null;
        return boundingBox.getMin().toLocation(world);
    }

    /**
     * Get the center location of this bound
     *
     * @return The center location
     */
    public Location getCenter() {
        World world = getWorld();
        if (world == null) return null;
        return boundingBox.getCenter().toLocation(world);
    }

    public int getLesserX() {
        return ((int) boundingBox.getMinX());
    }

    public void setLesserX(int x) {
        Vector min = this.boundingBox.getMin();
        min.setX(x);
        this.boundingBox = BoundingBox.of(min, this.boundingBox.getMax());
    }

    public int getLesserY() {
        return ((int) boundingBox.getMinY());
    }

    public void setLesserY(int y) {
        Vector min = this.boundingBox.getMin();
        min.setY(y);
        this.boundingBox = BoundingBox.of(min, this.boundingBox.getMax());
    }

    public int getLesserZ() {
        return ((int) boundingBox.getMinZ());
    }

    public void setLesserZ(int z) {
        Vector min = this.boundingBox.getMin();
        min.setZ(z);
        this.boundingBox = BoundingBox.of(min, this.boundingBox.getMax());
    }

    public int getGreaterX() {
        return ((int) boundingBox.getMaxX());
    }

    public void setGreaterX(int x2) {
        Vector max = this.boundingBox.getMax();
        max.setX(x2);
        this.boundingBox = BoundingBox.of(this.boundingBox.getMin(), max);
    }

    public int getGreaterY() {
        return ((int) boundingBox.getMaxY());
    }

    public void setGreaterY(int y2) {
        Vector max = this.boundingBox.getMax();
        max.setY(y2);
        this.boundingBox = BoundingBox.of(this.boundingBox.getMin(), max);
    }

    public int getGreaterZ() {
        return ((int) boundingBox.getMaxZ());
    }

    public void setGreaterZ(int z2) {
        Vector max = this.boundingBox.getMax();
        max.setZ(z2);
        this.boundingBox = BoundingBox.of(this.boundingBox.getMin(), max);
    }

    public void change(Axis axis, Corner corner, int amount) {
        if (axis == Axis.X) {
            if (corner == Corner.GREATER) {
                setGreaterX(getGreaterX() + amount);
            } else {
                setLesserX(getLesserX() + amount);
            }
        } else if (axis == Axis.Y) {
            if (corner == Corner.GREATER) {
                setGreaterY(getGreaterY() + amount);
            } else {
                setLesserY(getLesserY() + amount);
            }
        } else {
            if (corner == Corner.GREATER) {
                setGreaterZ(getGreaterZ() + amount);
            } else {
                setLesserZ(getLesserZ() + amount);
            }
        }
    }

    public enum Axis {
        X, Y, Z
    }

    public enum Corner {
        GREATER, LESSER
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UUID> getOwners() {
        return owners;
    }

    public void setOwners(List<UUID> owners) {
        this.owners = owners;
    }

    public void clearOwners() {
        this.owners.clear();
    }

    public void addOwner(UUID owner) {
        if (!this.owners.contains(owner)) {
            this.owners.add(owner);
        }
    }

    public void removeOwner(UUID owner) {
        this.owners.remove(owner);
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public void clearMembers() {
        this.members.clear();
    }

    public void addMember(UUID member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
        }
    }

    public void removeMember(UUID member) {
        this.members.remove(member);
    }

    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void deleteValue(String key) {
        this.values.remove(key);
    }

    public void clearValues() {
        this.values = new HashMap<>();
    }

    public Object getValue(String key) {
        return this.values.get(key);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * Get the instance of the Bukkit {@link BoundingBox}
     *
     * @return Bukkit BoundingBox
     */
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public String toString() {
        return this.id;
    }

    /**
     * Serialize this bound into yaml configuration
     *
     * @return Yaml configuration serialization of this bound
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", id);
        result.put("world", world);
        result.put("boundingbox", boundingBox);

        List<String> owners = new ArrayList<>();
        this.owners.forEach(uuid -> owners.add(uuid.toString()));
        List<String> members = new ArrayList<>();
        this.members.forEach(uuid -> members.add(uuid.toString()));
        result.put("owners", owners);
        result.put("members", members);
        result.put("values", values);

        return result;
    }

    /**
     * Deserialize this bound from yaml configuration
     *
     * @param args Args from yaml
     * @return New bound from serialized yaml configuration
     */
    @SuppressWarnings("unchecked")
    public static Bound deserialize(Map<String, Object> args) {
        String world = ((String) args.get("world"));
        String id = String.valueOf(args.get("id"));
        Bound bound;
        if (args.containsKey("boundingbox")) {
            BoundingBox box = ((BoundingBox) args.get("boundingbox"));
            bound = new Bound(world, id, box);
        } else {
            int x = ((Number) args.get("x1")).intValue();
            int y = ((Number) args.get("y1")).intValue();
            int z = ((Number) args.get("z1")).intValue();
            int x2 = ((Number) args.get("x2")).intValue();
            int y2 = ((Number) args.get("y2")).intValue();
            int z2 = ((Number) args.get("z2")).intValue();
            BoundingBox box = new BoundingBox(x, y, z, x2, y2, z2);
            bound = new Bound(world, id, box);
        }

        if (args.containsKey("owners")) {
            List<String> owners = (List<String>) args.get("owners");
            List<UUID> ownerUUIDs = new ArrayList<>();
            owners.forEach(owner -> {
                UUID uuid = UUID.fromString(owner);
                ownerUUIDs.add(uuid);
            });
            bound.setOwners(ownerUUIDs);
        }

        if (args.containsKey("members")) {
            List<String> members = (List<String>) args.get("members");
            List<UUID> memberUUIDs = new ArrayList<>();
            members.forEach(member -> {
                UUID uuid = UUID.fromString(member);
                memberUUIDs.add(uuid);
            });
            bound.setMembers(memberUUIDs);
        }

        if (args.containsKey("values")) {
            bound.values = (Map<String, Object>) args.get("values");
        }

        return bound;
    }

}
