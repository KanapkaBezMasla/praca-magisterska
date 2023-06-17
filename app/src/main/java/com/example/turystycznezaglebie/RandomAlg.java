package com.example.turystycznezaglebie;

import java.util.Random;
import java.util.ArrayList;

public class RandomAlg extends Algorithm{
    private ArrayList<Integer> visitedAttractionsIter = new ArrayList<Integer>();
    private ArrayList<Boolean> travelByCarIter = new ArrayList<Boolean>();

    public RandomAlg(TravelData td) {super(td);}

    public float getCollectedStars() {
        return collectedStars;
    }

    public float singleRand(int startPoint0, int timeMax){
        //int collectedStars = 0;
        timeMax *= 60;
        Random rand = new Random();
        int startPoint = startPoint0;
        int travelTimeLeft = timeMax;
        float collectedStarsIter = travelData.stars[startPoint];
        ArrayList<Integer> attractionsToVisit = new ArrayList<Integer>();
        visitedAttractionsIter = new ArrayList<Integer>();
        for (int i = 0; i < travelData.walking_matrix.length; i++)
            attractionsToVisit.add(i);

        attractionsToVisit.remove(startPoint);
        visitedAttractionsIter.add(startPoint);
        travelTimeLeft -= travelData.visit_time[startPoint] * 60;

        while (travelTimeLeft > 0 && !attractionsToVisit.isEmpty()) {
            Integer nextCity=0;
            if(attractionsToVisit.size()>1)
                nextCity = attractionsToVisit.get(rand.nextInt(attractionsToVisit.size() - 1));
            else if(attractionsToVisit.isEmpty())
                break;
            if (travelTimeLeft < travelData.walking_matrix[startPoint][nextCity])
                break;
            attractionsToVisit.remove(nextCity);
            visitedAttractionsIter.add(nextCity);
            collectedStarsIter += travelData.fitness(startPoint, nextCity, travelTimeLeft);
            travelTimeLeft -= travelData.visit_time[nextCity]*60 + travelData.walking_matrix[startPoint][nextCity];
            startPoint = nextCity;
            //collectedStarsIter += travelData.stars[nextCity];
        }

        if (visitedAttractions.size()==0)
            visitedAttractions = visitedAttractionsIter;

        return collectedStarsIter;
    }

    @Override
    public float findWay(int startPoint0, int timeMax, long calculation_time){
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        collectedStars = 0;
        do {
            float collectedStarsIter = singleRand(startPoint0, timeMax);
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
    public CarSollution findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        collectedStars = 0;
        carSollution = new CarSollution(travelByCar, visitedAttractions, 0);
        do {
            CarSollution cs_iter = singleRandMultimodal(startPoint0, timeMax);
            if (cs_iter.collectedStars > carSollution.collectedStars) {
                try {
                    carSollution = (CarSollution) cs_iter.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                };
            }
            long finish = System.nanoTime();
            timeElapsed = finish - start;
        }while(timeElapsed < calculation_time);

        return carSollution;
    }

    public CarSollution singleRandMultimodal(int startPoint0, int timeMax){
        //int collectedStars = 0;
        timeMax *= 60;
        Random rand = new Random();
        int startPoint = startPoint0;
        car = startPoint0;
        int travelTimeLeft = timeMax;
        float collectedStarsIter = travelData.stars[startPoint];
        ArrayList<Integer> attractionsToVisit = new ArrayList<Integer>();
        visitedAttractionsIter = new ArrayList<Integer>();
        for (int i = 0; i < travelData.walking_matrix.length; i++)
            attractionsToVisit.add(i);

        attractionsToVisit.remove(startPoint);
        visitedAttractionsIter.add(startPoint);
        travelTimeLeft -= travelData.visit_time[startPoint] * 60;

        while (travelTimeLeft > 0 && !attractionsToVisit.isEmpty()) {
            Integer nextCity=0;
            if(attractionsToVisit.size()>1)
                nextCity = attractionsToVisit.get(rand.nextInt(attractionsToVisit.size() - 1));
            else if(attractionsToVisit.isEmpty())
                break;

            boolean walk = rand.nextBoolean();
            if (walk && travelTimeLeft < travelData.walking_matrix[startPoint][nextCity])
                break;
            int timeByCar =  travelData.walking_matrix[startPoint][car] +
                                travelData.walking_matrix[car][nextCity] + CAR_PARKING_TIME;
            if (!walk && travelTimeLeft < timeByCar)
                break;
            attractionsToVisit.remove(nextCity);
            visitedAttractionsIter.add(nextCity);
            travelByCarIter.add(!walk);
            if(walk) {
                collectedStarsIter += travelData.fitness(startPoint, nextCity, travelTimeLeft);
                travelTimeLeft -= travelData.visit_time[nextCity]*60 + travelData.walking_matrix[startPoint][nextCity];
            }else{
                collectedStarsIter += travelData.fitness_car(startPoint, nextCity, travelTimeLeft);
                travelTimeLeft -= timeByCar;
            }
            startPoint = nextCity;

        }

        if (visitedAttractions.size()==0)
            visitedAttractions = visitedAttractionsIter;

        CarSollution cs = new CarSollution(travelByCarIter, visitedAttractionsIter, collectedStarsIter);

        return cs;
    }
}
