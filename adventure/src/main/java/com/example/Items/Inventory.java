package com.example.Items;

import java.util.*;

public class Inventory {

    private final int maxSlots;
    private final Map<String, Item> slots = new HashMap<>();

    public Inventory(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public Item takeItem(String name) {
        Item slot = slots.get(name);
        if (slot == null) return null;

        if (!slot.getQuantity().decrement()) {
            slots.remove(name);
        }
        return slot;
    }

    public int giveItem(Item item) {
        String name = item.getName();

        // Merge logic: increment existing slot or add new if space
        return slots.compute(name, (key, existing) -> {
            if (existing != null) {
                int availableSpace = existing.getQuantity().getMaxValue() - existing.getQuantity().getValue();
                int toAdd = Math.min(item.getQuantity().getValue(), availableSpace);
                existing.getQuantity().increment(toAdd);

                int leftover = item.getQuantity().getValue() - toAdd;
                return leftover > 0 ? existing : existing; // existing remains; leftover tracked outside
            } else if (slots.size() < maxSlots) {
                return new Item(item);
            }
            return existing; // no space, nothing added
        }) == null ? item.getQuantity().getValue() : 0;
    }

    public boolean hasItem(String name) {
        return slots.containsKey(name);
    }

    public boolean hasItem(Item item) {
        return slots.values().stream().anyMatch(slot -> slot.equals(item));
    }

    public List<Item> getItems() {
        return new ArrayList<>(slots.values());
    }

    public List<Item> addAllItems(List<Item> items) {
        List<Item> dropped = new ArrayList<>();

        for (Item item : items) {
            String name = item.getName();

            slots.compute(name, (key, existing) -> {
                if (existing != null) {
                    int availableSpace = existing.getQuantity().getMaxValue() - existing.getQuantity().getValue();
                    int toAdd = Math.min(item.getQuantity().getValue(), availableSpace);
                    existing.getQuantity().increment(toAdd);

                    int leftover = item.getQuantity().getValue() - toAdd;
                    if (leftover > 0) {
                        dropped.add(new Item(name, leftover));
                    }
                    return existing;
                } else if (slots.size() < maxSlots) {
                    return new Item(item);
                } else {
                    dropped.add(new Item(item));
                    return null;
                }
            });
        }

        return dropped;
    }

    public List<Item> addAllItems(Inventory other) {
        return addAllItems(other.getItems());
    }
}