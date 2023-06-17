package com.example.turystycznezaglebie;

import java.util.ArrayList;

public class Greedy extends Algorithm{

    public Greedy(TravelData td){super(td);}

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
    public CarSollution findWayMultimodal(int startPoint0, int timeMaxMin, long calculation_time) {
        return findWayMultimodal(startPoint0, timeMaxMin);
    }

    public CarSollution findWayMultimodal(int startPoint, int timeMax) {
        timeMax *= 60;
        collectedStars = travelData.stars[startPoint].floatValue();
        ArrayList<Integer> attractionsToVisit = new ArrayList<Integer>();
        for (int i = 0; i < travelData.walking_matrix.length; i++)
            attractionsToVisit.add(i);

        attractionsToVisit.remove(startPoint);
        visitedAttractions.add(startPoint);
        timeMax -= travelData.visit_time[startPoint]*60;
        car = startPoint;

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
            float best_fitness_car = 0;
            Integer nextCity_car = -1;
            for (int city : attractionsToVisit) {
                float fitness = travelData.fitness_per_s_car(startPoint, city, timeMax, car);
                if(fitness == 0)
                    continue;
                if (best_fitness_car < fitness) {
                    best_fitness_car = fitness;
                    nextCity_car = city;
                }
            }
            if(nextCity==-1 || nextCity_car==-1)
                break;

            attractionsToVisit.remove(nextCity);
            visitedAttractions.add(nextCity);
            // idziemy pieszo
            if(best_fitness>best_fitness_car) {
                travelByCar.add(false);
                collectedStars += travelData.fitness(startPoint, nextCity, timeMax);
                timeMax -= travelData.visit_time[nextCity]*60 + travelData.walking_matrix[startPoint][nextCity];
            }else //jedziemy autem
            {
                travelByCar.add(true);
                collectedStars += travelData.fitness_car(startPoint, nextCity_car, timeMax);
                timeMax -= travelData.visit_time[nextCity_car]*60 + travelData.walking_matrix[startPoint][car]+
                        travelData.car_matrix[car][nextCity_car] + CAR_PARKING_TIME;
                car = nextCity_car;
            }
            startPoint = nextCity;
        }

        CarSollution cs = new CarSollution(travelByCar, visitedAttractions, collectedStars);
        try {
            carSollution = (CarSollution) cs.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return cs;
    }

}

