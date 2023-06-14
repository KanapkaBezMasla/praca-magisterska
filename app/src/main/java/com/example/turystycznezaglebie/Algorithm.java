package com.example.turystycznezaglebie;

import java.util.ArrayList;

public abstract class  Algorithm {
    TravelData travelData;
    protected ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    public Algorithm(TravelData td){
        travelData = td;
    }
    protected float collectedStars;

    public ArrayList getVisitedAttractions() {
        return visitedAttractions;
    }

    public abstract float findWay(int startPoint0, int timeMax, long calculation_time);

    public abstract int findWayMultimodal(int startPoint0, int timeMax, long calculation_time);


}
