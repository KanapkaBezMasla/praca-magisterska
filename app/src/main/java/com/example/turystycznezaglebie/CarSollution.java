package com.example.turystycznezaglebie;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CarSollution {
    public int car;  //miejsce parkowania auta
    public ArrayList<Boolean> travelByCar = new ArrayList<Boolean>();
    public ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    public float collectedStars;

    public CarSollution(@NonNull ArrayList<Boolean> travByCar, ArrayList<Integer> visitedAtt, float stars){
        travelByCar= (ArrayList<Boolean>) travByCar.clone();
        visitedAttractions = visitedAtt;
        collectedStars = stars;
    }


}
