package com.example.turystycznezaglebie;

import java.util.ArrayList;

public abstract class  Algorithm {
    TravelData travelData;
    protected ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    public Algorithm(TravelData td){
        travelData = td;
    }
    protected float collectedStars;
    public static final Integer CAR_PARKING_TIME = 5*60;
    protected int car;  //miejsce parkowania auta
    protected ArrayList<Boolean> travelByCar = new ArrayList<Boolean>();
    public CarSollution carSollution;

    public ArrayList getVisitedAttractions() {
        return visitedAttractions;
    }

    public abstract float findWay(int startPoint0, int timeMax, long calculation_time);

    public abstract CarSollution findWayMultimodal(int startPoint0, int timeMax, long calculation_time);


}
