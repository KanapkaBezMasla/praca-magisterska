package com.example.turystycznezaglebie;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FirstImprovementHillClimber {
    private TravelData travelData;
    public float collectedStars;

    public FirstImprovementHillClimber(TravelData td){
        travelData = td;
    }
/*
    public ArrayList<Integer> baseImprove(@NonNull ArrayList<Integer> visitedAttractions){
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
    }*/

    public ArrayList<Integer> improve(@NonNull ArrayList<Integer> visitedAttractions, int timeMax){
        timeMax *= 60;
        int firstAttraction = visitedAttractions.get(0);
        int currentPathTime =  travelData.countCurrentPathTime(visitedAttractions);

        //Tablica zawierająca info, czy atrakcje są na liście. Stworzona, by działać szybciej niż contains().
        boolean [] isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Integer visitedAttraction : visitedAttractions)
            isVisitedAttractionTable[visitedAttraction] = true;


        boolean restart = true;
        while(restart) {
            restart = false;
            collectedStars = travelData.fitness4listOfAttractionNoCut(visitedAttractions, timeMax);
            for (int i = 1; i < visitedAttractions.size() && !restart; i++) {
                int currentAttraction = visitedAttractions.get(i);
                for (int j = 0; j < travelData.walking_matrix.length && !restart; j++) {
                    if (j != firstAttraction && j != currentAttraction) {
                        //Obie atrakcje już są na liście i tylko zamieniamy ich kolejność.
                        if (isVisitedAttractionTable[j]) {
                            int i2 = i;
                            int j2 = visitedAttractions.indexOf(j); //i2, j2 - indeksy na liście zwiedzania
                            if (i2 > j2) {
                                i2 = visitedAttractions.indexOf(j);
                                j2 = i;
                            }
                            //numery atrakcji (nie indeksy z listy zwiedzania)
                            int ai = visitedAttractions.get(i2 - 1);
                            int bi = visitedAttractions.get(i2);
                            int ci;
                            int aj;
                            int bj = visitedAttractions.get(j2);
                            //Atrakcja nie jest ostatnia na liście
                            if (j2 != visitedAttractions.size()-1) {
                                int cj = visitedAttractions.get(j2 + 1);
                                int prevTime = travelData.walking_matrix[ai][bi] + travelData.walking_matrix[bj][cj];
                                int newTime = travelData.walking_matrix[ai][bj] + travelData.walking_matrix[bi][cj];
                                if (j2 - i2 != 1) {
                                    ci = visitedAttractions.get(i2 + 1);
                                    aj = visitedAttractions.get(j2 - 1);
                                    prevTime += travelData.walking_matrix[bi][ci] + travelData.walking_matrix[aj][bj];
                                    newTime += travelData.walking_matrix[bj][ci] + travelData.walking_matrix[aj][bi];
                                } else {
                                    prevTime += travelData.walking_matrix[bi][bj];
                                    newTime += travelData.walking_matrix[bj][bi];
                                }
                                if (newTime < prevTime) {
                                    ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) visitedAttractions.clone();
                                    newVisitedAttractions.set(i2, bj);
                                    newVisitedAttractions.set(j2, bi);
                                    float newFitness = travelData.fitness4listOfAttractionNoCut(newVisitedAttractions, timeMax);
                                    if(newFitness>collectedStars) {
                                        visitedAttractions.set(i2, bj);
                                        visitedAttractions.set(j2, bi);
                                        currentPathTime += newTime - prevTime;
                                        restart = true;
                                    }
                                }
                            }
                            //Ostatnia atrakcja na liście
                            else{
                                ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) visitedAttractions.clone();
                                newVisitedAttractions.set(i2, bj);
                                newVisitedAttractions.set(j2, bi);
                                float newFitness = travelData.fitness4listOfAttractionNoCut(newVisitedAttractions, timeMax);
                                if(newFitness > collectedStars) {
                                    visitedAttractions.set(i2, bj);
                                    visitedAttractions.set(j2, bi);
                                    currentPathTime = travelData.countCurrentPathTime(visitedAttractions);
                                    restart = true;
                                }
                            }
                        }
                        //Atrakcji nie ma na liście i wymieniamy ją.
                        else {
                            int bi = currentAttraction;
                            if (i != visitedAttractions.size() - 1) {
                                ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) visitedAttractions.clone();
                                newVisitedAttractions.set(i, j);
                                float newFitness = travelData.fitness4listOfAttractionNoCut(newVisitedAttractions, timeMax);
                                if (newFitness > collectedStars) {
                                    visitedAttractions.set(i, j);
                                    isVisitedAttractionTable[j] = true;
                                    isVisitedAttractionTable[bi] = false;
                                    currentPathTime = travelData.countCurrentPathTime(visitedAttractions);
                                    restart = true;
                                }

                            }
                            else{   //wymieniamy ostatnią atrakcję na liście
                                int ai = visitedAttractions.get(i - 1);
                                int prevTime = travelData.walking_matrix[ai][bi] + travelData.visit_time[bi] * 60;
                                int newTime = travelData.walking_matrix[ai][j] + travelData.visit_time[j] * 60;
                                float newFitness = travelData.fitness(ai, j,  timeMax - currentPathTime + prevTime);
                                if (newFitness>collectedStars) {
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
            } //Koniec przestawiania i zamian.
            //Teraz próba dodania nowych
            boolean noNew = false;
            while(!noNew && timeMax>currentPathTime && !restart){
                Integer startPoint = visitedAttractions.get(visitedAttractions.size()-1);
                float best_fitness = 0;
                Integer nextCity = -1;
                for (int i=0; i<isVisitedAttractionTable.length; i++) {
                    if(isVisitedAttractionTable[i])
                        continue;
                    float fitness = travelData.fitness(startPoint, i, timeMax-currentPathTime);
                    if(fitness == 0)
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
                restart = true;
            }
        }
        collectedStars = travelData.fitness4listOfAttraction(visitedAttractions, timeMax);
        return visitedAttractions;
    }

    public CarSollution improveMultimoda(@NonNull CarSollution cs, int timeMax){
        timeMax *= 60;
        int firstAttraction = cs.visitedAttractions.get(0);
        int currentPathTime =  travelData.countCurrentPathTimeMultimodal(cs);

        //Tablica zawierająca info, czy atrakcje są na liście. Stworzona, by działać szybciej niż contains().
        boolean [] isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Integer visitedAttraction : cs.visitedAttractions)
            isVisitedAttractionTable[visitedAttraction] = true;


        boolean restart = true;
        while(restart) {
            restart = false;
            int car = firstAttraction;
            for (int i = 1; i < cs.visitedAttractions.size() && !restart; i++) {
                int currentAttraction = cs.visitedAttractions.get(i);
                for (int j = 0; j < travelData.walking_matrix.length && !restart; j++) {
                    if (j != firstAttraction && j != currentAttraction) {
                        //Obie atrakcje już są na liście i tylko zamieniamy ich kolejność.
                        if (isVisitedAttractionTable[j]) {
                            int i2 = i;
                            int j2 = cs.visitedAttractions.indexOf(j); //i2, j2 - indeksy na liście zwiedzania
                            if (i2 > j2) {
                                i2 = cs.visitedAttractions.indexOf(j);
                                j2 = i;
                            }
                            //numery atrakcji (nie indeksy z listy zwiedzania)
                            int ai = cs.visitedAttractions.get(i2 - 1);
                            int bi = cs.visitedAttractions.get(i2);
                            int ci;
                            int aj;
                            int bj = cs.visitedAttractions.get(j2);
                            //Atrakcja nie jest ostatnia na liście
                            if (j2 != cs.visitedAttractions.size()-1) {
                                int cj = cs.visitedAttractions.get(j2 + 1);
                                int prevTime = travelData.walking_matrix[ai][bi] + travelData.walking_matrix[bj][cj];
                                int newTime = travelData.walking_matrix[ai][bj] + travelData.walking_matrix[bi][cj];
                                if (j2 - i2 != 1) {
                                    ci = cs.visitedAttractions.get(i2 + 1);
                                    aj = cs.visitedAttractions.get(j2 - 1);
                                    prevTime += travelData.walking_matrix[bi][ci] + travelData.walking_matrix[aj][bj];
                                    newTime += travelData.walking_matrix[bj][ci] + travelData.walking_matrix[aj][bi];
                                } else {
                                    prevTime += travelData.walking_matrix[bi][bj];
                                    newTime += travelData.walking_matrix[bj][bi];
                                }
                                ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) cs.visitedAttractions.clone();
                                newVisitedAttractions.set(i2, bj);
                                newVisitedAttractions.set(j2, bi);
                                if (newTime < prevTime) {
                                    cs.visitedAttractions.set(i2, bj);
                                    cs.visitedAttractions.set(j2, bi);
                                    currentPathTime += newTime - prevTime;
                                    restart = true;
                                }
                            }
                            //Ostatnia atrakcja na liście
                            else{
                                ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) cs.visitedAttractions.clone();
                                newVisitedAttractions.set(i2, bj);
                                newVisitedAttractions.set(j2, bi);
                                float oldFitness = travelData.fitness4listOfAttractionNoCut(cs.visitedAttractions, timeMax);
                                float newFitness = travelData.fitness4listOfAttractionNoCut(newVisitedAttractions, timeMax);
                                if(newFitness > oldFitness) {
                                    cs.visitedAttractions.set(i2, bj);
                                    cs.visitedAttractions.set(j2, bi);
                                    currentPathTime = travelData.countCurrentPathTime(cs.visitedAttractions);
                                    restart = true;
                                }
                            }
                        }
                        //Atrakcji nie ma na liście i wymieniamy ją.
                        else {
                            int bi = currentAttraction;
                            if (i != cs.visitedAttractions.size() - 1) {
                                ArrayList<Integer> newVisitedAttractions = (ArrayList<Integer>) cs.visitedAttractions.clone();
                                newVisitedAttractions.set(i, j);
                                float oldFitness = travelData.fitness4listOfAttractionNoCut(cs.visitedAttractions, timeMax);
                                float newFitness = travelData.fitness4listOfAttractionNoCut(newVisitedAttractions, timeMax);
                                if (newFitness > oldFitness) {
                                    cs.visitedAttractions.set(i, j);
                                    isVisitedAttractionTable[j] = true;
                                    isVisitedAttractionTable[bi] = false;
                                    currentPathTime = travelData.countCurrentPathTime(cs.visitedAttractions);
                                    restart = true;
                                }

                            }
                            else{   //wymieniamy ostatnią atrakcję na liście
                                int ai = cs.visitedAttractions.get(i - 1);
                                int prevTime = travelData.walking_matrix[ai][bi] + travelData.visit_time[bi] * 60;
                                int newTime = travelData.walking_matrix[ai][j] + travelData.visit_time[j] * 60;
                                float oldFitness = travelData.fitness(ai, bi, timeMax - currentPathTime + prevTime);
                                float newFitness = travelData.fitness(ai, j,  timeMax - currentPathTime + prevTime);
                                if (newFitness>oldFitness) {
                                    cs.visitedAttractions.set(i, j);
                                    isVisitedAttractionTable[j] = true;
                                    isVisitedAttractionTable[bi] = false;
                                    currentPathTime -= prevTime - newTime;
                                    restart = true;
                                }
                            }
                        }
                    }
                }
            } //Koniec przestawiania i zamian.
            //Teraz próba dodania nowych
            boolean noNew = false;
            while(!noNew && timeMax>currentPathTime && !restart){
                Integer startPoint = cs.visitedAttractions.get(cs.visitedAttractions.size()-1);
                float best_fitness = 0;
                Integer nextCity = -1;
                for (int i=0; i<isVisitedAttractionTable.length; i++) {
                    if(isVisitedAttractionTable[i])
                        continue;
                    float fitness = travelData.fitness(startPoint, i, timeMax-currentPathTime);
                    if(fitness == 0)
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
                cs.visitedAttractions.add(nextCity);
                currentPathTime += travelData.visit_time[nextCity]*60 + travelData.walking_matrix[startPoint][nextCity];
                restart = true;
            }
        }
        collectedStars = travelData.fitness4listOfAttraction(cs.visitedAttractions, timeMax);
        return cs;
    }
    
}
