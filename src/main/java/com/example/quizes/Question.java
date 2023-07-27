package com.example.quizes;

public class Question {
    private String quizText;            // The text of the question
    private boolean answer;         // The correct answer to the question
    private int color;              // The color associated with the question
    private String translate;  // The translated text of the question (if available)

    public Question(String text, boolean answer, int color) {
        this.quizText = text;
        this.answer = answer;
        this.color = color;
        this.translate = null;  // Initialize translatedText as null by default
    }

    // Getters and setters for the fields

    public String getText() {
        return quizText;
    }

    public boolean isAnswer() {
        return answer;
    }

    public int getColor() {
        return color;
    }

    public String getTranslatedText() {
        return translate;
    }

    public void setTranslatedText(String translatedText) {
        this.translate = translatedText;
    }
}
