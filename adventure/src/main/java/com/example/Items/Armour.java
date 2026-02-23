package com.example.Items;

public class Armour extends Item  {
    public enum Type { HEAD, BODY, GLOVES, LEGS, BOOTS }
    
    private Type type;
    private int defencePower;
    
    public Armour(Type type, String name, int goldValue, int defencePower, int quantity) {
        super(name, goldValue, quantity);

        this.type = type;
        this.defencePower = defencePower;
    }

    public Armour(Type type, String name, int goldValue, int defencePower) {
        super(name, goldValue);

        this.type = type;
        this.defencePower = defencePower;
    }

    public int getDefencePower() {
        return defencePower;
    }

    public Type getType() {
        return type;
    }
}
