package com.example.turystycznezaglebie;

import java.util.Random;
import java.util.ArrayList;

public class RandomAlg extends Algorithm{
    private ArrayList<Integer> visitedAttractionsIter = new ArrayList<Integer>();

    public RandomAlg(TravelData td) {
        super(td);
    }

    public int getCollectedStars() {
        return collectedStars;
    }

    public int singleRand(int startPoint0, int timeMax){
        int collectedStars = 0;
        timeMax *= 60;
        Random rand = new Random();
        int startPoint = startPoint0;
        int travelTimeLeft = timeMax;
        int collectedStarsIter = travelData.stars[startPoint];
        ArrayList<Integer> attractionsToVisit = new ArrayList<Integer>();
        visitedAttractionsIter = new ArrayList<Integer>();
        for (int i = 0; i < travelData.walking_matrix.length; i++)
            attractionsToVisit.add(i);

        attractionsToVisit.remove(startPoint);
        visitedAttractionsIter.add(startPoint);
        travelTimeLeft -= travelData.visit_time[startPoint] * 60;

        while (travelTimeLeft > 0 && !attractionsToVisit.isEmpty()) {
            Integer nextCity = attractionsToVisit.get(rand.nextInt(attractionsToVisit.size() - 1));

            attractionsToVisit.remove(nextCity);
            visitedAttractionsIter.add(nextCity);
            if (travelTimeLeft < travelData.walking_matrix[startPoint][nextCity])
                break;
            travelTimeLeft -= travelData.visit_time[nextCity] * 60;
            travelTimeLeft -= travelData.walking_matrix[startPoint][nextCity];
            startPoint = nextCity;
            collectedStarsIter += travelData.stars[nextCity];
        }

        if (visitedAttractions.size()==0)
            visitedAttractions = visitedAttractionsIter;

        return collectedStarsIter;
    }

    @Override
    public int findWay(int startPoint0, int timeMax, long calculation_time){
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        Random rand = new Random();
        do {
            int collectedStarsIter = singleRand(startPoint0, timeMax);
            if (collectedStarsIter > collectedStars) {
                collectedStars = collectedStarsIter;
                visitedAttractions = visitedAttractionsIter;
            }
            long finish = System.nanoTime();
            timeElapsed = finish - start;
        }while(timeElapsed < calculation_time);

        return collectedStars;
    }

    @Override
    public int findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        return 0;
    }
}
