package com.example.turystycznezaglebie;

import java.util.ArrayList;

public class Greedy extends Algorithm{

    public Greedy(TravelData td){
        super(td);
    }

    public ArrayList getVisitedAttractions() {
        return visitedAttractions;
    }

    @Override
    public float findWay(int startPoint0, int timeMax, long calculation_time){
        return findWay(startPoint0, timeMax);
    }


    public float findWay(int startPoint, int timeMax){
        timeMax *= 60;
        collectedStars = travelData.stars[startPoint].floatValue();
        ArrayList<Integer> attractionsToVisit = new ArrayList<Integer>();
        for (int i = 0; i < travelData.walking_matrix.length; i++)
            attractionsToVisit.add(i);

        attractionsToVisit.remove(startPoint);
        visitedAttractions.add(startPoint);
        timeMax -= travelData.visit_time[startPoint]*60;

        while(timeMax>0 && !attractionsToVisit.isEmpty()){
            float best_fitness = 0;
            Integer nextCity = -1;
            for (int city : attractionsToVisit) {
                float fitness = travelData.fitness_per_s(startPoint, city, timeMax);
                if(fitness == 0)
                    continue;
                if (best_fitness < fitness) {
                    best_fitness = fitness;
                    nextCity = city;
                }
            }
            if(nextCity == -1)
                break;
            attractionsToVisit.remove(nextCity);
            visitedAttractions.add(nextCity);
            collectedStars += travelData.fitness(startPoint, nextCity, timeMax);
            timeMax -= travelData.visit_time[nextCity]*60;
            timeMax -= travelData.walking_matrix[startPoint][nextCity];
            startPoint = nextCity;
        }

        return collectedStars;
    }

    @Override
    public int findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        return 0;
    }
}

