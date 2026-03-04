package com.example.Utility;

import java.util.HashMap;

public abstract class IdGenerator 
{
    public enum IdTypes { ENTITY, ITEM, SPELL, ATTACK }
    
    private static HashMap<IdTypes,Long> ids = new HashMap<>();

    //static {
    //    for (IdTypes idType : IdTypes.values()) 
    //        ids.put(idType, -1l);
    //}

    /**
     * returns then increments the current id
     * @return id
     */
    public static long next(IdTypes idType) {
        return ids.merge(idType, 1l, Long::sum);
    }

    /**
     * returns current id without incrementing
     * @return id
     */
    public static long peek(IdTypes idType) {
        return ids.get(idType);
    }
}
