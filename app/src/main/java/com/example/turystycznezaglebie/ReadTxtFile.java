package com.example.turystycznezaglebie;
import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReadTxtFile {
    public Integer[][] readMatrix(Context context, String args) {
        // JSON file path
        String filePath = args;
        Integer[][] matrix = new Integer[33][33];
        try {
            FileInputStream inputStream = context.openFileInput(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            ArrayList<String> list = new ArrayList<String>();
            for (int row=0; (line = bufferedReader.readLine()) != null; row++) {
                stringBuilder.append(line);
                String[] elements = line.split(" ");
                for(int i=0; i<elements.length; i++) {
                    matrix[row][i] = Integer.parseInt(elements[i]);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        // Create JSON parser
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON array from file
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Convert JSON array to Java ArrayList
            ArrayList<String> list = new ArrayList<String>();
            //for (Object obj : jsonArray) {
            //    list.add((String) obj);
            //}
            for (int i=0; i<jsonArray.length(); i++){
                list.add((String) jsonArray.get(i));
            }

            // Print Java ArrayList
            System.out.println(list);
        } catch (IOException | ParseException | JSONException e) {
            e.printStackTrace();
        }*/
        return matrix;
    }
}
