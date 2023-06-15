package com.example.turystycznezaglebie;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing extends Algorithm{
    private ArrayList<Integer> currentVisitedAttr = new ArrayList<Integer>();
    public double boltzman;
    private Random random = new Random();


    public SimulatedAnnealing(TravelData td, double bolt){
        super(td);
        boltzman = bolt;
    }

    @Override
    public float findWay(int startPoint0, int timeMax, long calculation_time){
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        RandomAlg ra = new RandomAlg(travelData);
        float currCollectedStars = ra.singleRand(startPoint0, timeMax);
        collectedStars = currCollectedStars;
        visitedAttractions = ra.getVisitedAttractions();
        currentVisitedAttr = (ArrayList<Integer>) ra.getVisitedAttractions().clone();
        double temp = collectedStars*1000.0;

        do {
            ArrayList<Integer> newPath = (ArrayList<Integer>)currentVisitedAttr.clone();
            mutate(newPath);
            float newCollectedStars = travelData.fitness4listOfAttraction(newPath, timeMax);
            if (newCollectedStars > currCollectedStars) {
                if(newCollectedStars>collectedStars){
                    collectedStars = newCollectedStars;
                    visitedAttractions = (ArrayList<Integer>)newPath.clone();
                }
                currCollectedStars = newCollectedStars;
                currentVisitedAttr = newPath;
            }else{
                double acc_probability = Math.exp((currCollectedStars - newCollectedStars)/temp);
                double r = random.nextDouble();
                if(r<acc_probability) {
                    currentVisitedAttr = newPath;
                    currCollectedStars = newCollectedStars;
                }
            }
            temp *= boltzman;
            long finish = System.nanoTime();
            timeElapsed = finish - start;
        }while(timeElapsed < calculation_time && temp>0.001);
        return 0;
        //return collectedStars;
    }

    @NonNull
    private ArrayList<Integer> mutate(ArrayList<Integer> atrListToChange){
        boolean swap = random.nextBoolean();
        if(swap && atrListToChange.size()>2){
            int a = 0;
            int b = 0;
            while(a==b) {
                a = random.nextInt(atrListToChange.size() - 2) + 1;
                b = random.nextInt(atrListToChange.size() - 2) + 1;
                if(a==b && a==1)//nextInt ma problem czasem z wylosowaniem 0 i zwiesza program
                    b=2;
            }
            atrListToChange.set(b, atrListToChange.get(a));
            atrListToChange.set(a, atrListToChange.get(b));
        }
        else if (atrListToChange.size()>1)
        {  //wymiana wartości na nową
            int a = 1;
            if (atrListToChange.size()>2)
                a = random.nextInt(atrListToChange.size() - 2) + 1;
            try {
                int b = random.nextInt(travelData.visit_time.length - 2) + 1;
                boolean visited = true;
                while(visited) {
                    visited = atrListToChange.contains(b);
                    if(visited){
                        if(++b==travelData.visit_time.length)
                            b=1;
                    }
                }
                atrListToChange.set(a, b);
            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "isVisitedAttractionTable.lenght musi wynosić minimum 3", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return atrListToChange;
    }

    @Override
    public float findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        return 0;
    }
}
