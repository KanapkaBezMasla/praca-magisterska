package com.example.turystycznezaglebie;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TravelData {
    public Integer [][] walking_matrix;     //czas podróży w sekundach
    public Integer [] visit_time;           //czas zwiedzania w minutach
    public Integer [] stars;
    public Integer [][] car_matrix;

    public TravelData(Integer [][]wm, Integer [] vt, Integer [] st){
        walking_matrix = wm;
        visit_time = vt;
        stars = st;
    }

    public TravelData(Integer [][]wm, Integer [] vt, Integer [] st, Integer [][] cm){
        walking_matrix = wm;
        visit_time = vt;
        stars = st;
        car_matrix = cm;
    }

    public float fitness_per_s(int start, int destination, int time_left){
        if (walking_matrix[start][destination] > time_left)
            return 0;
        float time4attraction = visit_time[destination].floatValue()*60 + walking_matrix[start][destination].floatValue();
        if (time4attraction > time_left){
            float penalty = (time4attraction - time_left)*stars[destination].floatValue()/time4attraction;
            return (stars[destination].floatValue() - penalty) / time4attraction;
        }
        else
            return stars[destination].floatValue() / time4attraction;
    }

    public float fitness(int start, int destination, int time_leftS){
        if (walking_matrix[start][destination] > time_leftS)
            return 0;
        float time4attraction = visit_time[destination].floatValue()*60 + walking_matrix[start][destination].floatValue();
        if (time4attraction > time_leftS){
            float penalty = (time4attraction - time_leftS)*stars[destination].floatValue()/time4attraction;
            return stars[destination].floatValue() - penalty;
        }
        else
            return stars[destination].floatValue();
    }

    public float fitness4listOfAttraction(@NonNull ArrayList<Integer> visitedAttractions, int timeMaxS){
        int start = visitedAttractions.get(0);
        float collectedStars = stars[start].floatValue();
        timeMaxS -= visit_time[start].floatValue()*60;
        for(int dest : visitedAttractions){
            if(dest==start)
                continue;
            collectedStars+=fitness(start,dest, timeMaxS);
            timeMaxS -= visit_time[dest].floatValue()*60 + walking_matrix[start][dest].floatValue();
            start = dest;
            if (timeMaxS<=0)
                return collectedStars;
        }
        return collectedStars;
    }

    public int countCurrentPathTime(@NonNull ArrayList<Integer> visitedAttractions){
        int currentPathTime = 0;
        int prevAttraction = visitedAttractions.get(0);
        for (Integer atr : visitedAttractions){
            currentPathTime += visit_time[atr]*60 + walking_matrix[prevAttraction][atr];
            prevAttraction = atr;
        }
        return currentPathTime;
    }

    /////////////////////////MULTI//////////////////////////////////

    public float fitness_car(int start, int destination, int time_leftS, int car){
        if (car_matrix[car][destination] + Algorithm.CAR_PARKING_TIME + walking_matrix[start][car] > time_leftS)
            return 0;
        float time4attraction = visit_time[destination].floatValue()*60 + car_matrix[car][destination].floatValue() + walking_matrix[start][car].floatValue() + Algorithm.CAR_PARKING_TIME;
        if (time4attraction > time_leftS){
            float penalty = (time4attraction - time_leftS)*stars[destination].floatValue()/time4attraction;
            return stars[destination].floatValue() - penalty;
        }
        else
            return stars[destination].floatValue();
    }

    public float fitness_per_s_car(int start, int destination, int time_left, int car){
        if (walking_matrix[start][car] + car_matrix[car][destination] + Algorithm.CAR_PARKING_TIME > time_left)
            return 0;
        float time4attraction = visit_time[destination].floatValue()*60 + walking_matrix[start][car].floatValue()
                + car_matrix[car][destination].floatValue() + Algorithm.CAR_PARKING_TIME;
        if (time4attraction > time_left){
            float penalty = (time4attraction - time_left)*stars[destination].floatValue()/time4attraction;
            return (stars[destination].floatValue() - penalty) / time4attraction;
        }
        else
            return stars[destination].floatValue() / time4attraction;
    }

    public int countCurrentPathTimeMultimodal(@NonNull CarSollution cs){
        int currentPathTime = 0;
        int prevAttraction = cs.visitedAttractions.get(0);
        int car_ite=0;
        int car = prevAttraction;
        for (Integer atr : cs.visitedAttractions){
            if (atr == prevAttraction)
                continue;
            currentPathTime += visit_time[atr]*60;
            if(cs.travelByCar.get(car_ite++)) {
                currentPathTime += walking_matrix[prevAttraction][car] + car_matrix[car][atr] + Algorithm.CAR_PARKING_TIME;
                car = atr;
            }
            else
                currentPathTime +=  walking_matrix[prevAttraction][atr];
            prevAttraction = atr;
        }
        return currentPathTime;
    }

    public float fitness4listOfAttractionMultimodal(@NonNull CarSollution cs, int timeMaxS){
        int start = cs.visitedAttractions.get(0);
        float collectedStars = stars[start].floatValue();
        timeMaxS -= visit_time[start].floatValue()*60;
        int car = cs.visitedAttractions.get(0);
        for(int i=0; i<cs.visitedAttractions.size(); i++){
            int dest = cs.visitedAttractions.get(i);
            if(dest==start)
                continue;
            boolean byCar = cs.travelByCar.get(i-1);
            collectedStars+=(byCar) ? fitness_car(start, dest, timeMaxS, car) : fitness(start,dest, timeMaxS);
            timeMaxS -= visit_time[dest].floatValue()*60;
            if(byCar) {
                timeMaxS -= walking_matrix[start][car].floatValue() + car_matrix[car][dest].floatValue() + Algorithm.CAR_PARKING_TIME.floatValue();
                car = dest;
            }else
                timeMaxS -= walking_matrix[start][dest].floatValue();
            start = dest;
            if (timeMaxS<=0)
                return collectedStars;
        }
        return collectedStars;
    }


}
