package com.example.turystycznezaglebie;

import java.util.ArrayList;

public abstract class  Algorithm {
    TravelData travelData;
    ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    public Algorithm(TravelData td){
        travelData = td;
    }

    public ArrayList getVisitedAttractions() {
        return visitedAttractions;
    }

    public abstract int findWay(int startPoint0, int timeMax, long calculation_time);


}
