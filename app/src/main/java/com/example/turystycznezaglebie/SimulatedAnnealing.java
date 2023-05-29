package com.example.turystycznezaglebie;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private TravelData travelData;
    private ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    private ArrayList<Integer> currentVisitedAttr = new ArrayList<Integer>();

    public SimulatedAnnealing(TravelData td){
        travelData = td;
    }

    public int findWay(int startPoint0, int timeMax, long calculation_time){
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        Random random = new Random();
        RandomAlg ra = new RandomAlg(travelData);
        int collectedStarsIter = ra.singleRand(startPoint0, timeMax);
        do {

            //if (collectedStarsIter > ra.collectedStars) {
            //    ra.collectedStars = collectedStarsIter;
            //    visitedAttractions = ra.visitedAttractionsIter;
            //}
            //ra.visitedAttractionsIter.clear();
            long finish = System.nanoTime();
            timeElapsed = finish - start;
        }while(timeElapsed < calculation_time);

        //return collectedStars;
        return -1;
    }
}
