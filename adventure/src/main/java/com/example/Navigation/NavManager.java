package com.example.Navigation;

import java.util.PriorityQueue;
import java.util.Queue;

public class NavManager {
    /*
        NavManager -> Floor (10 in ascending enemy level range) -> Room (10 Enemy Encounters, 5 Event, 1 Boss at End)
     */
    public static final NavManager INSTANCE = new NavManager();

    //contains zones
    private Queue<Floor> floors = new PriorityQueue<>();
    private Floor currentFloor;

    private NavManager() {
        floors.add(getCurrentFloor());
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void nextFloor() {
        currentFloor = floors.poll();
    }
}
