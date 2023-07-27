package com.example.quizes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Result {
    private static final String FILE_NAME = "result.txt";
    private Context context;

    public Result(Context context) {
        this.context = context;
    }

    public void initialize() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(int correctAnswers, int totalQuestions) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getResultFile(), true));
            String result = correctAnswers + " | " + totalQuestions;
            writer.write(result);
            writer.newLine(); // Add a new line after the content
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getResultFile() {
        return new File(context.getFilesDir(), FILE_NAME);
    }

    public String read() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getResultFile()));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public String getAverage() {
        String result = read();

        if (result.isEmpty()) {
            return "0/0";
        } else {
            String[] lines = result.split("\n");
            int sumCorrectAnswers = 0;
            int sumTotalQuestions = 0;
            int count = 0;

            for (String line : lines) {
                try {
                    String[] parts = line.split(" \\| ");
                    int correctAnswers = Integer.parseInt(parts[0]);
                    int totalQuestions = Integer.parseInt(parts[1]);

                    sumCorrectAnswers += correctAnswers;
                    sumTotalQuestions += totalQuestions;
                    count++;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    Log.e("Result", "Error parsing line: " + line, e);
                }
            }

            if (count > 0) {
                int averageCorrectAnswers = sumCorrectAnswers / count;
                int averageTotalQuestions = sumTotalQuestions / count;
                return averageCorrectAnswers + "/" + averageTotalQuestions;
            } else {
                return "0/0"; // Return a message indicating no valid data was found in the file
            }
        }
    }

    public void reset() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getResultFile()));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
