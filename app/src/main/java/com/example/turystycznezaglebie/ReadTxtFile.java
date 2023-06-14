package com.example.turystycznezaglebie;
import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class ReadTxtFile {
    public Integer[][] readMatrix(Context context, String args, int size) {
        // JSON file path
        String filePath = args;
        Integer[][] matrix = new Integer[size][size];
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

    public void saveToFile(Object valToSave, Context context, String FILENAME) {
        try {
            // Otwórz strumień do zapisu pliku
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);

            // Zapisz wartości w formacie tekstowym
            String dataToWrite = valToSave + "";

            // Zapisz wartości do pliku
            printWriter.println(dataToWrite);
            printWriter.flush();

            // Zamknij strumienie
            printWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("FileWrite", "Błąd podczas zapisu do pliku: " + e.getMessage());
        }
    }

}
