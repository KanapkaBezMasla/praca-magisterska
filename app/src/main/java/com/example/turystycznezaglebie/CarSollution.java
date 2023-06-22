package com.example.turystycznezaglebie;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CarSollution implements Cloneable{
    public int car;  //miejsce parkowania auta
    public ArrayList<Boolean> travelByCar = new ArrayList<Boolean>();
    public ArrayList<Integer> visitedAttractions = new ArrayList<Integer>();
    public float collectedStars;

    public CarSollution(@NonNull ArrayList<Boolean> travByCar, @NonNull ArrayList<Integer> visitedAtt, float stars){
        travelByCar= (ArrayList<Boolean>) travByCar.clone();
        visitedAttractions = (ArrayList<Integer>) visitedAtt.clone();
        collectedStars = stars;
    }

    @Override
    public CarSollution clone() throws CloneNotSupportedException {

        CarSollution cs = new CarSollution(this.travelByCar, this.visitedAttractions, this.collectedStars);
        return cs;
    }

    public int getCar(){
        int c = visitedAttractions.get(0);
        for(int i=travelByCar.size()-1; i>=0; i--)
            if(travelByCar.get(i)) {
                c = visitedAttractions.get(i);
                break;
            }
        return c;
    }

}
