package com.example.turystycznezaglebie;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SimulatedAnnealing extends Algorithm{
    private ArrayList<Integer> currentVisitedAttr = new ArrayList<Integer>();
    public double boltzman;
    private Random random = new Random();
    private double temp_beg;
    private boolean stop;

    public SimulatedAnnealing(TravelData td, double bolt, double tb){
        super(td);
        boltzman = bolt;
        temp_beg = tb;
    }

    public float findWay(int startPoint0, int timeMaxMin, long calculation_time){return 0;}

    //@Override
    public float findWay(int startPoint0, int timeMaxMin, long calculation_time, Context context){
        ReadTxtFile fileReader = new ReadTxtFile();
        stop = false;
        int timeMaxS = timeMaxMin*60;
        long start = System.nanoTime();
        long timeElapsed;
        calculation_time *= 1000000000;
        //RandomAlg alg = new RandomAlg(travelData);
        Greedy alg = new Greedy(travelData);
        float currCollectedStars = alg.findWay(startPoint0, timeMaxMin);
        collectedStars = currCollectedStars;
        visitedAttractions = alg.getVisitedAttractions();
        currentVisitedAttr = (ArrayList<Integer>) alg.getVisitedAttractions().clone();
        double temp = collectedStars*temp_beg;
        {
            long startBreak = System.nanoTime();
            long finish = System.nanoTime();
            //timeElapsed = finish - start;
            fileReader.saveToFile(collectedStars, context, "sa_stars_tune_single.txt");
            //fileReader.saveToFile(timeElapsed/1000000000, context, "sa_time_tune_single.txt");
            fileReader.saveToFile(collectedStars, context, "sa_best_tune_single.txt");
            fileReader.saveToFile(temp, context, "sa_temp_tune_single.txt");
            long stopBreak = System.nanoTime();
            start += stopBreak - startBreak;
        }
        int n = 0;
        do {
            ArrayList<Integer> newPath = (ArrayList<Integer>)currentVisitedAttr.clone();
            mutate(newPath, timeMaxS);
            float newCollectedStars = travelData.fitness4listOfAttraction(newPath, timeMaxS);
            if (newCollectedStars > currCollectedStars) {
                if(newCollectedStars>collectedStars){
                    collectedStars = newCollectedStars;
                    visitedAttractions = (ArrayList<Integer>)newPath.clone();
                }
                currCollectedStars = newCollectedStars;
                currentVisitedAttr = newPath;
            }else{
                double acc_probability = Math.exp((newCollectedStars - currCollectedStars)/temp);
                double r = random.nextDouble();
                if(r<acc_probability) {
                    currentVisitedAttr = newPath;
                    currCollectedStars = newCollectedStars;
                }
            }
            temp *= boltzman;
            long finish = System.nanoTime();
            timeElapsed = finish - start;
            long startBreak = System.nanoTime();
            if(++n==100) {
                n=0;
                fileReader.saveToFile(currCollectedStars, context, "sa_stars_tune_single.txt");
                //fileReader.saveToFile(timeElapsed/1000000000.0, context, "sa_time_tune_single.txt");
                fileReader.saveToFile(collectedStars, context, "sa_best_tune_single.txt");
                long stopBreak = System.nanoTime();
                start += stopBreak - startBreak;
            }
        }while(timeElapsed < calculation_time && temp>0.001 && !stop);
        //fileReader.saveToFile(temp, context, "sa_temp_tune_single.txt");
        return collectedStars;
    }

    @NonNull
    private ArrayList<Integer> mutate(ArrayList<Integer> atrListToChange, int timeMaxS){
        boolean [] isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Iterator<Integer> iter = atrListToChange.iterator(); iter.hasNext(); )
            isVisitedAttractionTable[iter.next()] = true;

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
            int buf = atrListToChange.get(b);
            atrListToChange.set(b, atrListToChange.get(a));
            atrListToChange.set(a, atrListToChange.get(b));
        }
        else if (atrListToChange.size()>1 && atrListToChange.size() != travelData.visit_time.length)
        {  //wymiana wartości na nową
            int a = 1;
            if (atrListToChange.size()>2)
                a = random.nextInt(atrListToChange.size() - 2) + 1;
            try {
                int b = random.nextInt(travelData.visit_time.length - 2) + 1;
                boolean visited = true;
                while(visited || b==atrListToChange.get(0)) {
                    visited = isVisitedAttractionTable[b];;
                    if(visited){
                        if(++b==travelData.visit_time.length)
                            b=0;
                    }
                }
                isVisitedAttractionTable[atrListToChange.get(a)] = false;
                isVisitedAttractionTable[b] = true;
                atrListToChange.set(a, b);
            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "isVisitedAttractionTable.lenght musi wynosić minimum 3", Toast.LENGTH_SHORT).show();
                return null;
            }
        }//jeśli wszystkie atrakcje w pełni na liście, to kończymy
        else if (atrListToChange.size() == travelData.visit_time.length){
            int timeMaxS2 = timeMaxS;
            timeMaxS2 -= travelData.visit_time[atrListToChange.get(0)].floatValue()*60;
            int start = atrListToChange.get(0);
            for(int dest : visitedAttractions){
                if(dest==atrListToChange.get(0))
                    continue;
                timeMaxS2 -= travelData.visit_time[dest].floatValue()*60 + travelData.walking_matrix[start][dest].floatValue();
                start = dest;
                if (timeMaxS2<=0)
                    stop = true;
            }
        }
        //jeżeli czas za krótki, by dojść do jakiegokolwiek atrakcji, to kończymy
        else if(atrListToChange.size()==1){
            int start = atrListToChange.get(0);
            stop = true;
            for(int dest : visitedAttractions){
                if(dest==atrListToChange.get(0))
                    continue;
                if (timeMaxS>travelData.walking_matrix[start][dest])
                    stop=false;
            }
        }

        //próbujemy dołożyć nowe atrakcje na koniec listy
        int currentPathTime = travelData.countCurrentPathTime(atrListToChange);;
        if(currentPathTime<timeMaxS){
            boolean noNew = false;
            while(!noNew){
                Integer startPoint = visitedAttractions.get(visitedAttractions.size()-1);
                float best_fitness = 0;
                Integer nextCity = -1;
                for (int i=0; i<isVisitedAttractionTable.length; i++) {
                    if(isVisitedAttractionTable[i])
                        continue;
                    float fitness = travelData.fitness(startPoint, i, timeMaxS-currentPathTime);
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
            }
        }

        return atrListToChange;
    }

    public CarSollution findWayMultimodal(int startPoint0, int timeMax, long calculation_time){return null;}

    //@Override
    public CarSollution findWayMultimodal(int startPoint0, int timeMaxMin, long calculation_time, Context context) {
        ReadTxtFile fileReader = new ReadTxtFile();
        stop = false;
        int timeMaxS = timeMaxMin*60;
        long start = System.nanoTime();
        long timeElapsed = 0;
        calculation_time *= 1000000000;
        //RandomAlg alg = new RandomAlg(travelData);
        Greedy alg = new Greedy(travelData);
        CarSollution currCarSollution = alg.findWayMultimodal(startPoint0, timeMaxMin);
        try {
            carSollution = (CarSollution) currCarSollution.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        double temp = carSollution.collectedStars*temp_beg;
        {
            long startBreak = System.nanoTime();
            long finish = System.nanoTime();
            //timeElapsed = finish - start;
            fileReader.saveToFile(currCarSollution.collectedStars, context, "sa_stars_tune_multi.txt");
            //fileReader.saveToFile(timeElapsed/1000000000, context, "sa_time_tune_single.txt");
            fileReader.saveToFile(currCarSollution.collectedStars, context, "sa_best_tune_multi.txt");
            fileReader.saveToFile(temp, context, "sa_temp_tune_multi.txt");
            long stopBreak = System.nanoTime();
            start += stopBreak - startBreak;
        }

        do {
            try {
                CarSollution newPath = (CarSollution) currCarSollution.clone();
                mutateMultimodal(newPath, timeMaxS);
                //float newCollectedStars = travelData.fitness4listOfAttraction(newPath, timeMaxS);
                if (newPath.collectedStars > currCarSollution.collectedStars) {
                    if(newPath.collectedStars>carSollution.collectedStars){
                        carSollution = (CarSollution) newPath.clone();
                    }
                    currCarSollution = newPath;
                }else{
                    double acc_probability = Math.exp((newPath.collectedStars - currCarSollution.collectedStars)/temp);
                    double r = random.nextDouble();
                    if(r<acc_probability)
                        currCarSollution = newPath;
                }
                temp *= boltzman;
                long finish = System.nanoTime();
                timeElapsed = finish - start;
                long startBreak = System.nanoTime();

                fileReader.saveToFile(currCarSollution.collectedStars, context, "sa_stars_tune_multi.txt");
                //fileReader.saveToFile(temp, context, "sa_temp_tune_multi.txt");
                fileReader.saveToFile(carSollution.collectedStars, context, "sa_best_tune_multi.txt");
                long stopBreak = System.nanoTime();
                start += stopBreak - startBreak;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(timeElapsed < calculation_time && temp>0.001 && !stop);
        fileReader.saveToFile(temp, context, "sa_temp_tune_multi.txt");
        return carSollution;
    }

    @NonNull
    private CarSollution mutateMultimodal(CarSollution carSolToChange, int timeMaxS){
        boolean [] isVisitedAttractionTable = new boolean[travelData.walking_matrix.length];
        for (int j=0; j<travelData.walking_matrix.length; j++)
            isVisitedAttractionTable[j] = false;
        for (Iterator<Integer> iter = carSolToChange.visitedAttractions.iterator(); iter.hasNext(); )
            isVisitedAttractionTable[iter.next()] = true;

        int mut_opt = random.nextInt(4);
        if (mut_opt==0 || mut_opt==4) mut_opt=2;  //0 losowane jest bardzo rzadko, a opisy w internecie są niejednoznaczne czy wypada 4 czy nie
        if(mut_opt==1 && carSolToChange.visitedAttractions.size()>2){
            int a = 0;
            int b = 0;
            while(a==b) {
                a = random.nextInt(carSolToChange.visitedAttractions.size() - 2) + 1;
                b = random.nextInt(carSolToChange.visitedAttractions.size() - 2) + 1;
                if(a==b && a==1)//nextInt ma problem czasem z wylosowaniem 0 i zwiesza program
                    b=2;
            }
            int buf = carSolToChange.visitedAttractions.get(b);
            carSolToChange.visitedAttractions.set(b, carSolToChange.visitedAttractions.get(a));
            carSolToChange.visitedAttractions.set(a, buf);
        }
        else if(mut_opt==2 && carSolToChange.visitedAttractions.size()>1 && carSolToChange.visitedAttractions.size() != travelData.visit_time.length)
        {  //wymiana wartości na nową
            int a = 1;
            if (carSolToChange.visitedAttractions.size()>2)
                a = random.nextInt(carSolToChange.visitedAttractions.size() - 2) + 1;
            try {
                int b = random.nextInt(travelData.visit_time.length - 2) + 1;
                boolean visited = true;
                while(visited || b==carSolToChange.visitedAttractions.get(0)) {
                    visited = isVisitedAttractionTable[b];;
                    if(visited){
                        if(++b==travelData.visit_time.length)
                            b=0;
                    }
                }
                isVisitedAttractionTable[carSolToChange.visitedAttractions.get(a)] = false;
                isVisitedAttractionTable[b] = true;
                carSolToChange.visitedAttractions.set(a, b);
            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "isVisitedAttractionTable.lenght musi wynosić minimum 3", Toast.LENGTH_SHORT).show();
                return null;
            }
        }//Zmiana trybu podróży (auto/pieszo) na przeciwny
        else if(mut_opt==3 && carSolToChange.visitedAttractions.size()>1){
            int a = 0;
            if (carSolToChange.visitedAttractions.size()>3)
                a = random.nextInt(carSolToChange.visitedAttractions.size() - 2);
            carSolToChange.travelByCar.set(a, !carSolToChange.travelByCar.get(a));
        }
        //jeśli wszystkie atrakcje w pełni na liście, to kończymy
        else if (carSolToChange.visitedAttractions.size() == travelData.visit_time.length){
            int timeMaxS2 = timeMaxS;
            timeMaxS2 -= travelData.visit_time[carSolToChange.visitedAttractions.get(0)].floatValue()*60;
            int start = carSolToChange.visitedAttractions.get(0);
            int c = start;
            for(int dest : carSolToChange.visitedAttractions){
                if(dest==carSolToChange.visitedAttractions.get(0))
                    continue;
                timeMaxS2 -= travelData.visit_time[dest]*60;
                if(carSolToChange.travelByCar.get(c++)){
                    timeMaxS2 -=  travelData.walking_matrix[start][c] + travelData.car_matrix[c][dest] + Algorithm.CAR_PARKING_TIME;
                    c = dest;
                }else
                    timeMaxS2 -= travelData.walking_matrix[start][dest];
                start = dest;
                if (timeMaxS2<=0)
                    stop = true;
            }
        }
        //jeżeli czas za krótki, by dotrzeć do jakiegokolwiek atrakcji, to kończymy
        else if(carSolToChange.visitedAttractions.size()==1){
            int start = carSolToChange.visitedAttractions.get(0);
            stop = true;
            for(int dest : carSolToChange.visitedAttractions){
                if(dest==carSolToChange.visitedAttractions.get(0))
                    continue;
                if (timeMaxS>travelData.walking_matrix[start][dest])
                    stop=false;
                else if (timeMaxS>travelData.car_matrix[start][dest] + Algorithm.CAR_PARKING_TIME)
                    stop=false;
            }
        }

        //próbujemy dołożyć nowe atrakcje na koniec listy
        int currentPathTime = travelData.countCurrentPathTimeMultimodal(carSolToChange);
        if(currentPathTime<timeMaxS){
            boolean noNew = false;
            while(!noNew){
                Integer startPoint = carSolToChange.visitedAttractions.get(carSolToChange.visitedAttractions.size()-1);
                float best_fitness = 0;
                boolean trav_by_car = false;
                Integer nextCity = -1;
                int c = carSolToChange.getCar();

                for (int i=0; i<isVisitedAttractionTable.length; i++) {
                    if(isVisitedAttractionTable[i])
                        continue;
                    float fitness = travelData.fitness(startPoint, i, timeMaxS-currentPathTime);
                    if(fitness == 0)
                        continue;
                    if (best_fitness < fitness) {
                        best_fitness = fitness;
                        nextCity = i;
                    }

                    float fitness_car = travelData.fitness_car(startPoint, i, timeMaxS-currentPathTime, c);
                    if(fitness_car == 0)
                        continue;
                    if (best_fitness < fitness_car) {
                        best_fitness = fitness_car;
                        nextCity = i;
                        c = i;
                        trav_by_car = true;
                    }
                }
                if(nextCity == -1) {
                    noNew=true;
                    break;
                }
                isVisitedAttractionTable[nextCity] = true;
                carSolToChange.visitedAttractions.add(nextCity);
                carSolToChange.travelByCar.add(trav_by_car);
                currentPathTime += travelData.visit_time[nextCity]*60;
                if(trav_by_car)
                    currentPathTime += travelData.walking_matrix[startPoint][c] + travelData.car_matrix[c][nextCity] + Algorithm.CAR_PARKING_TIME;
                else
                    currentPathTime += travelData.walking_matrix[startPoint][nextCity];

            }
        }

        carSolToChange.collectedStars = travelData.fitness4listOfAttractionMultimodal(carSolToChange, timeMaxS);

        return carSolToChange;
    }


}
