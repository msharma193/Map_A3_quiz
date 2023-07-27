package com.example.quizes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private quizBank quizBank;
    private int total;
    private int index;
    private ProgressBar progressBar;
    private int correctCount;
    private int perGameAns;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = new Result(this);
        result.initialize();

        // Initialize the question bank and set the total number of questions
        quizBank = new quizBank(this);
        total = quizBank.getQuestions().size();
        progressBar = findViewById(R.id.progress_bar);

        correctCount = 0;
        perGameAns = 0;

        if (savedInstanceState == null) {
            // Load the first question fragment
            showQuestionFragment();
        } else {
            // Restore the state of the activity
            index = savedInstanceState.getInt("currentQuestionIndex");
        }

        Locale locale = Locale.getDefault();
        // Get the localized app name.
        String appName = getResources().getString(R.string.app_name);
        String formattedAppName = String.format("%s", appName);
        // Set the app's name to the localized app name.
        getSupportActionBar().setTitle(appName);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex", index);
    }

    private void showQuestionFragment() {
        Question question = quizBank.getNextQuestion();
        if (question != null) {
            Fragment questionFragment = quizFragment.newInstance(question.getText(), question.getColor(), index, total, quizBank, correctCount);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, questionFragment);
            transaction.commit();
            index++;
        } else {
            // Quiz finished, show completion dialog
            showQuizCompletionDialog();
        }
    }

    private void showQuizCompletionDialog() {
        perGameAns += correctCount;
        // Implement the completion dialog logic here
        messageFragment dialogFragment = new messageFragment();

        Bundle args = new Bundle();
        args.putInt(messageFragment.ARG_CORRECT_QUESTION_COUNT, correctCount);
        args.putInt(messageFragment.ARG_TOTAL_QUESTIONS, total);
        dialogFragment.setArguments(args);

        dialogFragment.show(getSupportFragmentManager(), "messageFragment");
    }

    public void saveResults() {
        perGameAns += correctCount;
        result.update(correctCount, total); // Save the result in the Result class
    }

    public void resetQuiz() {
        correctCount = 0;
        index = 0;
        perGameAns = 0;
        updateProgressBar(index, total, perGameAns);
        showQuestionFragment(); // Reset the UI
    }

    public void updateProgressBar(int currentQuestionIndex, int totalQuestions, int cqc) {
        int progress = (int) (((float) currentQuestionIndex / totalQuestions) * 100);
        progressBar.setProgress(progress);
        correctCount = cqc;
        progressBar.setProgress(progress);
    }

    private void showNumberInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_select_number_of_questions);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedNumber = input.getText().toString();
                int selectedCount = Integer.parseInt(selectedNumber);

                if (selectedCount > total) {
                    // If selected count is greater than available questions, use the available count
                    selectedCount = total;
                }
                Toast.makeText(MainActivity.this, "Selected Number: " + selectedCount, Toast.LENGTH_SHORT).show();
                total = selectedCount;
                index = 0;
                resetQuiz();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean reset() {
        correctCount = 0;
        index = 0;
        perGameAns = 0;
        updateProgressBar(index, total, perGameAns);
        showQuestionFragment(); // Reset the UI
        return true;
    }

    private void showAverageDialog() {
        String average = result.getAverage();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your Correct answers / Total number of questions = " + average);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetResults() {
        result.reset();
        Toast.makeText(this, "Results reset!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_average) {
            // Handle average menu item click
            showAverageDialog();
            return true;
        } else if (itemId == R.id.action_select_number_of_questions) {
            showNumberInputDialog();
            return true;
        } else if (itemId == R.id.action_reset_results) {
            // Handle reset results menu item click
            resetResults();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
