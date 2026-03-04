package com.example.Items;

import com.example.Utility.Clamped;
import com.example.Utility.IdGenerator;

public class Item {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 999;
    
    protected String name;
    //sell price in copper
    protected int sellCP; 
    protected Clamped quantity;

    protected final long id = IdGenerator.next(IdGenerator.IdTypes.ITEM);

    public Item(String name, int sellCP, int quantity) {
        this.name = name;
        this.sellCP = sellCP;
        this.quantity = new Clamped(MIN_QUANTITY, quantity, MAX_QUANTITY);
    }

    public Item(String name, int sellCP) {
        this(name, sellCP, 1);
    }

    public Item(Item item) {
        name = item.name;
        sellCP = item.sellCP;
        quantity = new Clamped(item.quantity);
    }

    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellCP;
    }

    public Clamped getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "%s%s".formatted(
            name,
            quantity.atMinimum() ? "" : " x%d".formatted(quantity.getValue())
        );
    }

    public boolean equals(Item other) {
        return name == other.name;
    }
}
