package com.example.turystycznezaglebie;

import java.util.ArrayList;

public class Greedy {
    private TravelData travelData;
    private ArrayList<Integer>  visitedAttractions = new ArrayList<Integer>();

    public Greedy(TravelData td){
        travelData = td;
    }

    public ArrayList getVisitedAttractions() {
        return visitedAttractions;
    }

    public int findWay(int startPoint, int timeMax){
        timeMax *= 60;
        int collectedStars = travelData.stars[startPoint];
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
                float fitness = travelData.fitness(startPoint, city, timeMax);
                if(fitness == -1)
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
            timeMax -= travelData.visit_time[nextCity]*60;
            timeMax -= travelData.walking_matrix[startPoint][nextCity];
            startPoint = nextCity;
            collectedStars += travelData.stars[nextCity];
        }

        return collectedStars;
    }
}
