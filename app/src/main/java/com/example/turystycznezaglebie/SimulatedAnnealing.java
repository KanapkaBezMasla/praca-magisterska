package com.example.turystycznezaglebie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SimulatedAnnealing extends Algorithm{
    private ArrayList<Integer> currentVisitedAttr = new ArrayList<Integer>();
    public double boltzman;
    private Random random = new Random();
    boolean [] isVisitedAttractionTable;

    public SimulatedAnnealing(TravelData td, double bolt){
        super(td);
        boltzman = bolt;
    }

    @Override
    public int findWay(int startPoint0, int timeMax, long calculation_time){
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        RandomAlg ra = new RandomAlg(travelData);
        int currCollectedStars = ra.singleRand(startPoint0, timeMax);
        collectedStars = currCollectedStars;
        visitedAttractions = ra.getVisitedAttractions();
        currentVisitedAttr = (ArrayList<Integer>) ra.getVisitedAttractions().clone();
        double temp = collectedStars*1000.0;

        //Tablica zawierająca info, czy atrakcje są na liście. Stworzona, by działać szybciej niż contains().
        isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Iterator<Integer> iter = visitedAttractions.iterator(); iter.hasNext(); )
            isVisitedAttractionTable[iter.next()] = true;
        do {
            ArrayList<Integer> newPath = (ArrayList<Integer>)currentVisitedAttr.clone();
            mutate(newPath);
            int newCollectedStars = 0;
            for (int atr : newPath){
                newCollectedStars += travelData.stars[atr];
            }
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

        return collectedStars;
    }

    private ArrayList<Integer> mutate(ArrayList<Integer> atrListToChange){
        boolean swap = random.nextBoolean();
        if(swap){
            int a = 0;
            int b = 0;
            while(a==b && a!=1) {
                a = random.nextInt(atrListToChange.size() - 2) + 1;
                b = random.nextInt(atrListToChange.size() - 2) + 1;
            }
            atrListToChange.set(b, atrListToChange.get(a));
            atrListToChange.set(a, atrListToChange.get(b));
        }else{  //wymiana wartości na nową
            int a = random.nextInt(atrListToChange.size() - 2) + 1;
            int b = -1;
            boolean visited = true;
            while(visited) {
                b = random.nextInt(isVisitedAttractionTable.length - 2) + 1;
                visited = isVisitedAttractionTable[b];
            }
            atrListToChange.set(a, b);
            isVisitedAttractionTable[a] = false;
            isVisitedAttractionTable[b] = true;
        }

        return atrListToChange;
    }

    @Override
    public int findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        return 0;
    }
}
