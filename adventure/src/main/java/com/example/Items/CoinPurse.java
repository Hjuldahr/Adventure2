package com.example.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.Items.Currency.CoinType;

public class CoinPurse 
{
    // tracked separately
    private HashMap<CoinType,Integer> pieces;

    public CoinPurse() {
        pieces = new HashMap<>();
        pieces.put(CoinType.COPPER, 0);
        pieces.put(CoinType.SILVER, 0);
        pieces.put(CoinType.GOLD, 0);
    }

    public Map<CoinType,Integer> get() {
        return Collections.unmodifiableMap(pieces);
    }

    public void set(Map<CoinType,Integer> newPieces) {
        pieces = new HashMap<>(newPieces);
    }

    public void deposit(HashMap<CoinType,Integer> depositing) { 
        // perform deposit
        depositing.entrySet().forEach(c -> deposit(c.getKey(), c.getValue()));
    }

    public void deposit(CoinType coinType, int depositing) { 
        // perform deposit
        if (depositing > 0) {
            pieces.put(coinType, pieces.get(coinType) + depositing);
        }
    }

    public boolean withdraw(CoinType coinType, int withdrawing) {
        // allow for free purchases or valueless item selling without false flagging
        if (withdrawing >= 0 && pieces.get(coinType) >= withdrawing) {
            pieces.put(coinType, pieces.get(coinType) - withdrawing);
            return true;
        }
        return false;
    }

    public boolean withdraw(HashMap<CoinType,Integer> withdrawing) { 
        // test if funds match requested withdraw
        if (withdrawing.entrySet().stream().anyMatch(c -> pieces.get(c.getKey()) < c.getValue())) {
            return false;
        }
        // perform withdraw
        withdrawing.forEach((k, v) -> pieces.put(k, pieces.get(k) - v));
        return true;
    }

    public static boolean transfer(CoinPurse to, CoinPurse from, CoinType coinType, int transferring) {
        if (!from.withdraw(coinType, transferring)) {
            return false;
        }
        to.deposit(coinType, transferring);
        return true;
    }

    public static boolean transfer(CoinPurse to, CoinPurse from, HashMap<CoinType,Integer> transferring) {
        if (!from.withdraw(transferring)) {
            return false;
        }
        to.deposit(transferring);
        return true;
    }

    public String toString() {
        return "%d CP %d SP %d GP".formatted(pieces.get(CoinType.COPPER), pieces.get(CoinType.SILVER), pieces.get(CoinType.GOLD));
    }
}
