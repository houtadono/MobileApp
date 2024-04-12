package vn.id.houta.myapplication.model;

import java.util.ArrayList;

public class Question {
    private String questionText;
    private ArrayList<String> options;
    private String correctAnswer, selectedAnswer;


    public Question(String questionText, ArrayList<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public Question() {
    }

    public boolean isAnswerCorrect() {
        return selectedAnswer != null && selectedAnswer.equals(correctAnswer);
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    // Getter và setter cho questionText và correctAnswer

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}

