package com.shanebeestudios.skbee.elements.other.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.slot.Slot;
import com.shanebeestudios.skbee.api.event.EntityBlockInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class OtherEvents {

    static {
        Skript.registerEvent("Block Physical Interact Event", SimpleEvent.class, EntityBlockInteractEvent.class,
                        "block (interact|trample)")
                .description("Called when an entity physically interacts with a block, for example, trampling.")
                .examples("on block interact:",
                        "\tif event-entity is a villager:",
                        "\t\tcancel event")
                .since("1.5.0");

        EventValues.registerEventValue(EntityBlockInteractEvent.class, Block.class, new Getter<>() {
            @Nullable
            @Override
            public Block get(EntityBlockInteractEvent event) {
                return event.getBlock();
            }
        }, 0);

        Skript.registerEvent("Anvil Prepare Event", SimpleEvent.class, PrepareAnvilEvent.class, "anvil prepare")
                .description("Called when a player attempts to combine 2 items in an anvil.",
                        "'event-slot' represents the result slot, can be used to get or set.")
                .examples("on anvil prepare:",
                        "\tif slot 0 of event-inventory is a diamond sword:",
                        "\t\tif slot 1 of event-inventory is an enchanted book:",
                        "\t\t\tif stored enchants of slot 1 of event-inventory contains sharpness 5:",
                        "\t\t\t\tset {_i} to slot 0 of event-inventory",
                        "\t\t\t\tadd \"&aOOOOOOO\" and \"&bAHHHHHH\" to lore of {_i}",
                        "\t\t\t\tenchant {_i} with sharpness 6",
                        "\t\t\t\tset event-slot to {_i}",
                        "\t\t\t\tset repair cost of event-inventory to 30")
                .since("1.11.0");

        EventValues.registerEventValue(PrepareAnvilEvent.class, Player.class, new Getter<>() {
            @Override
            public Player get(PrepareAnvilEvent event) {
                return (Player) event.getView().getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(PrepareAnvilEvent.class, Slot.class, new Getter<>() {
            @Override
            public Slot get(PrepareAnvilEvent event) {
                return new Slot() {
                    @Nullable
                    @Override
                    public ItemStack getItem() {
                        return event.getResult();
                    }

                    @Override
                    public void setItem(@Nullable ItemStack item) {
                        event.setResult(item);
                    }

                    @Override
                    public int getAmount() {
                        return event.getResult().getAmount();
                    }

                    @Override
                    public void setAmount(int amount) {
                        event.getResult().setAmount(amount);
                    }

                    @Override
                    public boolean isSameSlot(Slot o) {
                        ItemStack item = o.getItem();
                        return item != null && item.isSimilar(event.getResult());
                    }

                    @Override
                    public String toString(@Nullable Event e, boolean debug) {
                        return "anvil inventory result slot";
                    }
                };
            }
        }, 0);

        EventValues.registerEventValue(PrepareAnvilEvent.class, Inventory.class, new Getter<>() {
            @Override
            public Inventory get(PrepareAnvilEvent event) {
                return event.getInventory();
            }
        }, 0);

        // Player shear entity event
        Skript.registerEvent("Shear Entity", SimpleEvent.class, PlayerShearEntityEvent.class, "[player] shear entity")
                .description("Called when a player shears an entity. Requires Minecraft 1.9.4+")
                .examples("on player shear entity:")
                .since("1.8.0");

        // Entity Breed Event
        Skript.registerEvent("Entity Breed", SimpleEvent.class, EntityBreedEvent.class,
                        "entity breed")
                .description("Called when one Entity breeds with another Entity.")
                .examples("on entity breed:", "\nif breeding mother is a sheep:",
                        "\n\nkill breeding player")
                .since("INSERT VERSION");

        EventValues.registerEventValue(EntityBreedEvent.class, Player.class, new Getter<>() {
            @Override
            public @Nullable Player get(EntityBreedEvent breedEvent) {
                LivingEntity breeder = breedEvent.getBreeder();
                if (breeder instanceof Player player) {
                    return player;
                }
                return null;
            }
        }, 0);

        EventValues.registerEventValue(EntityBreedEvent.class, Entity.class, new Getter<>() {
            @Override
            public @Nullable Entity get(EntityBreedEvent breedEvent) {
                return breedEvent.getEntity();
            }
        }, 0);

        EventValues.registerEventValue(EntityBreedEvent.class, ItemType.class, new Getter<>() {
            @Override
            public @Nullable ItemType get(EntityBreedEvent breedEvent) {
                ItemStack bredWith = breedEvent.getBredWith();
                if (bredWith != null) {
                    return new ItemType(bredWith);
                }
                return null;
            }
        }, 0);
    }

}
