package com.example.quizes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class messageFragment extends DialogFragment {
    public static final String ARG_CORRECT_QUESTION_COUNT = "arg_correct_question_count";
    public static final String ARG_TOTAL_QUESTIONS = "arg_total_questions";

    private int correctQuestion;
    private int total;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            correctQuestion = args.getInt(ARG_CORRECT_QUESTION_COUNT, 0);
            total = args.getInt(ARG_TOTAL_QUESTIONS, 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_quiz_completed_title);
        String message = getString(R.string.dialog_quiz_completed_message) +
                "\n\nYour score is: " + correctQuestion + " out of " + total;
        builder.setMessage(message);

        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the results to the file system
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.saveResults(); // Call the saveResults() method in MainActivity
                }
                Toast.makeText(getActivity(), R.string.toast_results_saved, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.ignore_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ignore the results and reset the quiz
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.resetQuiz();
                }
                Toast.makeText(getActivity(), R.string.toast_results_ignored, Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
