package com.example.turystycznezaglebie;

import java.util.ArrayList;
import java.util.Iterator;

public class FirstImprovementHillClimber {
    private TravelData travelData;

    public FirstImprovementHillClimber(TravelData td){
        travelData = td;
    }

    public ArrayList<Integer> baseImprove(ArrayList<Integer> visitedAttractions){
        for (int i=1; i<visitedAttractions.size(); i++){
            for(int j=i+1; j<visitedAttractions.size(); j++){
                int ai = visitedAttractions.get(i-1);
                int bi = visitedAttractions.get(i);
                int ci;
                int aj;
                int bj = visitedAttractions.get(j);
                int cj = visitedAttractions.get(j+1);
                int prevTime = travelData.walking_matrix[ai][bi];
                int newTime  = travelData.walking_matrix[ai][bj];
                prevTime += travelData.walking_matrix[bj][cj];
                newTime  += travelData.walking_matrix[bi][cj];
                if(j-i!=1) {
                    ci = visitedAttractions.get(i + 1);
                    aj = visitedAttractions.get(j - 1);
                    prevTime += travelData.walking_matrix[bi][ci];
                    newTime  += travelData.walking_matrix[bj][ci];
                    prevTime += travelData.walking_matrix[aj][bj];
                    newTime  += travelData.walking_matrix[aj][bi];
                }
                else{
                    prevTime += travelData.walking_matrix[bi][bj];
                    newTime  += travelData.walking_matrix[bj][bi];
                }
                if(newTime<prevTime){
                    visitedAttractions.set(i, bj);
                    visitedAttractions.set(j, bi);
                }



            }
        }
        return visitedAttractions;
    }

    public ArrayList<Integer> improve(ArrayList<Integer> visitedAttractions, int timeMax){
        timeMax *= 60;
        int firstAttraction = visitedAttractions.get(0);
        int currentPathTime = 0;
        int prevAttraction = firstAttraction;
        for (Integer atr : visitedAttractions){
            currentPathTime += travelData.visit_time[atr]*60 + travelData.walking_matrix[prevAttraction][atr];
            prevAttraction = atr;
        }


        //Tablica zawierająca info, czy atrakcje są na liście. Stworzona, by działać szybciej niż contains().
        boolean [] isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Integer visitedAttraction : visitedAttractions)
            isVisitedAttractionTable[visitedAttraction] = true;


        boolean restart = false;
        while(!restart) {
            for (int i = 1; i < visitedAttractions.size() && !restart; i++) {
                int currentAttraction = visitedAttractions.get(i);
                for (int j = 0; j < travelData.walking_matrix.length && !restart; j++) {
                    if (j != firstAttraction && j != currentAttraction) {
                        //Obie atrakcje już są na liście i tylko zamieniamy ich kolejność.
                        if (isVisitedAttractionTable[j]) {
                            int i2 = i;
                            int j2 = visitedAttractions.indexOf(j);
                            if (i2 > j2) {
                                i2 = j;
                                j2 = i;
                            }

                            int ai = visitedAttractions.get(i2 - 1);
                            int bi = visitedAttractions.get(i2);
                            int ci;
                            int aj;
                            int bj = visitedAttractions.get(j2);
                            int cj = visitedAttractions.get(j2 + 1);
                            int prevTime = travelData.walking_matrix[ai][bi] + travelData.walking_matrix[bj][cj];
                            int newTime = travelData.walking_matrix[ai][bj] + travelData.walking_matrix[bi][cj];
                            if (j2 - i2 != 1) {
                                ci = visitedAttractions.get(i2 + 1);
                                aj = visitedAttractions.get(j2 - 1);
                                prevTime += travelData.walking_matrix[bi][ci];
                                newTime += travelData.walking_matrix[bj][ci];
                                prevTime += travelData.walking_matrix[aj][bj];
                                newTime += travelData.walking_matrix[aj][bi];
                            } else {
                                prevTime += travelData.walking_matrix[bi][bj];
                                newTime += travelData.walking_matrix[bj][bi];
                            }
                            if (newTime < prevTime) {
                                visitedAttractions.set(i2, bj);
                                visitedAttractions.set(j2, bi);
                                restart = true;
                            }
                        }
                        //Atrakcji nie ma na liście i wymieniamy ją.
                        else {
                            int ai = visitedAttractions.get(i - 1);
                            int bi = currentAttraction;
                            int ci = visitedAttractions.get(i + 1);
                            int prevTime = travelData.walking_matrix[ai][bi] + travelData.walking_matrix[bi][ci] + travelData.visit_time[bi]*60;
                            int newTime = travelData.walking_matrix[ai][j] + travelData.walking_matrix[j][ci] + travelData.visit_time[j]*60;

                            if (travelData.stars[bi] < travelData.stars[j] || (travelData.stars[bi] == travelData.stars[j] && prevTime > newTime)) {
                                //Mieścimy się w czasie zwiedzania.
                                if (newTime <= prevTime || timeMax > currentPathTime - prevTime + newTime) {
                                    visitedAttractions.set(i, j);
                                    isVisitedAttractionTable[j] = true;
                                    isVisitedAttractionTable[bi] = false;
                                    currentPathTime -= prevTime - newTime;
                                    restart = true;
                                }
                                //Nie mieścimy się w czasie zwiedzania.
                                else {
                                    int lastAttraction = visitedAttractions.get(visitedAttractions.size() - 1);
                                    int penultimateAttraction = visitedAttractions.get(visitedAttractions.size() - 2);
                                    int prevTimeLeft4LastAttraction = timeMax - currentPathTime + travelData.walking_matrix[penultimateAttraction][lastAttraction] + travelData.visit_time[lastAttraction]*60;
                                    float oldFitness = travelData.fitness(ai, bi, Integer.MAX_VALUE) + travelData.fitness(bi, ci, Integer.MAX_VALUE)
                                            + travelData.fitness(penultimateAttraction, lastAttraction, prevTimeLeft4LastAttraction);
                                    float newFitness = travelData.fitness(ai, j, Integer.MAX_VALUE) + travelData.fitness(j, ci, Integer.MAX_VALUE)
                                            + travelData.fitness(penultimateAttraction, lastAttraction, prevTimeLeft4LastAttraction-prevTime+newTime);
                                    if(newFitness>oldFitness){
                                        visitedAttractions.set(i, j);
                                        isVisitedAttractionTable[j] = true;
                                        isVisitedAttractionTable[bi] = false;
                                        currentPathTime -= prevTime - newTime;
                                        restart = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } //Koniec przestawiania i zamian.
            //Teraz próba dodania nowych
            boolean noNew = false;
            while(!noNew && timeMax>currentPathTime){
                Integer startPoint = visitedAttractions.get(visitedAttractions.size()-1);
                float best_fitness = 0;
                Integer nextCity = -1;
                for (int i=0; i<isVisitedAttractionTable.length; i++) {
                    if(isVisitedAttractionTable[i])
                        continue;
                    float fitness = travelData.fitness(startPoint, i, timeMax-currentPathTime);
                    if(fitness == -1)
                        continue;
                    if (best_fitness < fitness) {
                        best_fitness = fitness;
                        nextCity = i;
                    }
                }
                if(nextCity == -1) {
                    noNew=true;
                    break;
                }
                isVisitedAttractionTable[nextCity] = true;
                visitedAttractions.add(nextCity);
                currentPathTime += travelData.visit_time[nextCity]*60 + travelData.walking_matrix[startPoint][nextCity];
                //collectedStars += travelData.stars[nextCity];
                restart = true;
            }
        }
        return visitedAttractions;
    }

    
}
