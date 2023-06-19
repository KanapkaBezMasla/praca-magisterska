package com.example.turystycznezaglebie;

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

    //public float findWay(int startPoint0, int timeMaxMin, long calculation_time){return 0;}

    @Override
    public float findWay(int startPoint0, int timeMaxMin, long calculation_time){//, Context context){
        //ReadTxtFile fileReader = new ReadTxtFile();
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
        /*{
            long startBreak = System.nanoTime();
            long finish = System.nanoTime();
            //timeElapsed = finish - start;
            fileReader.saveToFile(collectedStars, context, "sa_stars_tune_single.txt");
            //fileReader.saveToFile(timeElapsed/1000000000, context, "sa_time_tune_single.txt");
            fileReader.saveToFile(collectedStars, context, "sa_best_tune_single.txt");
            fileReader.saveToFile(temp, context, "sa_temp_tune_single.txt");
            long stopBreak = System.nanoTime();
            start += stopBreak - startBreak;
        }*/
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
            /*if(++n==100) {
                n=0;
                fileReader.saveToFile(currCollectedStars, context, "sa_stars_tune_single.txt");
                //fileReader.saveToFile(timeElapsed/1000000000.0, context, "sa_time_tune_single.txt");
                fileReader.saveToFile(collectedStars, context, "sa_best_tune_single.txt");
                long stopBreak = System.nanoTime();
                start += stopBreak - startBreak;
            }*/
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
                    break;
            }
            stop = true;
        }
        //jeżeli czas za krótki, by dojść do jakiegokolwiek atrakcji, to kończymy
        else if(swap && atrListToChange.size()==1){
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

    @Override
    public CarSollution findWayMultimodal(int startPoint0, int timeMax, long calculation_time) {
        return null;
    }
}
