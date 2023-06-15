package com.example.turystycznezaglebie;

import android.content.Context;

public class Experiment {
    private final int [] startPoints = {0, 7, 13, 20, 26, 33, 39, 46};
    private final int [] startPoints_avg = {0, 7, 13, 20};
    private final int [] startPoints_small = {0, 7};
    private final int [][] startPointsTables = {startPoints_small, startPoints_avg, startPoints};
    private final String [] small_datasets = {"data13A.txt", "data13B.txt", "data13C.txt", "data13D.txt"};
    private final String [] avg_datasets = {"data26A.txt", "data26B.txt"};
    private final String [] big_datasets = {"data_52.txt"};
    private final String [][] datasets = {small_datasets, avg_datasets, big_datasets};
    private final int [] dataset_sizes = {13, 26, 52};
    private final int [] travel_time = {60, 120, 240, 480};
    private final long [] measure_time = {1, 3, 5, 10};

    private Integer [] visit_time2;
    private Integer [] stars_rating2;
    private Context context;
    private ReadTxtFile fileReader = new ReadTxtFile();

    Experiment(Integer [] vt, Integer [] sr, Context c){
        visit_time2 = vt;
        stars_rating2 = sr;
        context = c;
    }


    public void greedy_single(){
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

    public void greedy_fihc_single(){
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
                        FirstImprovementHillClimber fihc = new FirstImprovementHillClimber(travelData);
                        greedy.findWay(i, time);
                        fihc.improve(greedy.getVisitedAttractions(), time);
                        float gr_fihc_stars = fihc.collectedStars;
                        long finish = System.nanoTime();
                        long timeElapsed = finish - start;
                        fileReader.saveToFile(gr_fihc_stars, context, "gr_fihc_stars_single.txt");
                        fileReader.saveToFile(timeElapsed, context, "gr_fihc_time_single.txt");
                    }
                }
                fileReader.saveToFile("\n", context, "gr_fihc_stars_single.txt");
                fileReader.saveToFile("\n", context, "gr_fihc_time_single.txt");
            }
            fileReader.saveToFile("\n=============\n", context, "gr_fihc_stars_single.txt");
            fileReader.saveToFile("\n=============\n", context, "gr_fihc_time_single.txt");
        }
    }

    public void random_single() {
        for (long measure_t : measure_time) {
            for (int a = 0; a < 3; a++) {
                for (int time : travel_time) {
                    int b = 0;
                    for (String dataset : datasets[a]) {
                        Integer[] visit_time = new Integer[dataset_sizes[a]];
                        for (int i = b * dataset_sizes[a], j = 0; i < (b + 1) * dataset_sizes[a]; i++, j++) {
                            visit_time[j] = visit_time2[i];
                        }
                        Integer[] stars_rating = new Integer[dataset_sizes[a]];
                        for (int i = b * dataset_sizes[a], j = 0; i < (b + 1) * dataset_sizes[a]; i++, j++) {
                            stars_rating[j] = stars_rating2[i];
                        }
                        b++;

                        Integer[][] walk_matrix;
                        walk_matrix = fileReader.readMatrix(context, dataset, dataset_sizes[a]);
                        TravelData travelData = new TravelData(walk_matrix, visit_time, stars_rating);
                        for (int i : startPointsTables[a]) {
                            RandomAlg ran_alg = new RandomAlg(travelData);
                            float greedy_stars = ran_alg.findWay(i, time, measure_t);
                            fileReader.saveToFile(greedy_stars, context, "rand_stars_single.txt");
                        }
                    }
                    fileReader.saveToFile("\n", context, "rand_stars_single.txt");
                }
                fileReader.saveToFile("\n=============\n", context, "rand_stars_single.txt");
            }
        }
    }

}
