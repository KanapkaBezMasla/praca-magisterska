package com.example.turystycznezaglebie;

import android.content.Context;

public class Experiment {

    private int [] startPoints = {0, 7, 13, 20, 26, 33, 39, 46};
    private int [] startPoints_avg = {0, 7, 13, 20};
    private int [] startPoints_small = {0, 7};
    private  int [][] startPointsTables = {startPoints_small, startPoints_avg, startPoints};
    private String [] small_datasets = {"data13A.txt", "data13B.txt", "data13C.txt", "data13D.txt"};
    private String [] avg_datasets = {"data26A.txt", "data26B.txt"};
    private String [] big_datasets = {"data_52.txt"};
    private  String [][] datasets = {small_datasets, avg_datasets, big_datasets};
    private int [] dataset_sizes = {13, 26, 52};

    private int [] travel_time = {60, 120, 240, 480};

    public void greedy_single(Context context, Integer [] visit_time2, Integer [] stars_rating2){
        ReadTxtFile fileReader = new ReadTxtFile();
        for (int a=0; a<3; a++) {

            for (int time : travel_time) {
                int b = 0;
                for (String dataset : datasets[a]) {
                    Integer[] visit_time = new Integer[dataset_sizes[a]];
                    for (int i = b*dataset_sizes[a], j=0; i < (b+1)*dataset_sizes[a]; i++, j++) {
                        visit_time[j] = visit_time2[i];
                    }
                    Integer[] stars_rating = new Integer[dataset_sizes[a]];
                    for (int i = b*dataset_sizes[a], j=0; i < (b+1)*dataset_sizes[a]; i++, j++) {
                        stars_rating[j] = stars_rating2[i];
                    }
                    b++;

                    Integer[][] walk_matrix;
                    walk_matrix = fileReader.readMatrix(context, dataset, dataset_sizes[a]);
                    TravelData travelData = new TravelData(walk_matrix, visit_time, stars_rating);
                    for (int i : startPointsTables[a]) {
                        long start = System.nanoTime();
                        Greedy greedy = new Greedy(travelData);
                        float greedy_stars = greedy.findWay(i, time);
                        long finish = System.nanoTime();
                        long timeElapsed = finish - start;
                        fileReader.saveToFile(greedy_stars, context, "greedy_stars_single.txt");
                        fileReader.saveToFile(timeElapsed, context, "greedy_time_single.txt");
                    }
                }
                fileReader.saveToFile("\n", context, "greedy_stars_single.txt");
                fileReader.saveToFile("\n", context, "greedy_time_single.txt");
            }
            fileReader.saveToFile("\n=============\n", context, "greedy_stars_single.txt");
            fileReader.saveToFile("\n=============\n", context, "greedy_time_single.txt");
        }
    }



}
