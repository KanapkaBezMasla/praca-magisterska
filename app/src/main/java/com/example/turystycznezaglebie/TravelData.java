package com.example.turystycznezaglebie;

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
        if (walking_matrix[start][destination] > time_left*60)
            return -1;
        float time4attraction = visit_time[destination].floatValue()*60 + walking_matrix[start][destination].floatValue();
        if (time4attraction < time_left){
            float penalty = (time4attraction - time_left)*stars[destination].floatValue()/time4attraction;
            return (stars[destination].floatValue() - penalty) / time4attraction;
        }
        else
            return stars[destination].floatValue() / time4attraction;
    }

    public float fitness(int start, int destination, int time_left){
        if (walking_matrix[start][destination] > time_left*60)
            return -1;
        float time4attraction = visit_time[destination].floatValue()*60 + walking_matrix[start][destination].floatValue();
        if (time4attraction > time_left){
            float penalty = (time4attraction - time_left)*stars[destination].floatValue()/time4attraction;
            return stars[destination].floatValue() - penalty;
        }
        else
            return stars[destination].floatValue();
    }

}
