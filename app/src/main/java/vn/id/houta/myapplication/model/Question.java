package vn.id.houta.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String questionText;
    private ArrayList<String> options;
    private String correctAnswer, selectedAnswer;
    private ArrayList<ImageQuestion> imageQuestions;

    public Question(String questionText, ArrayList<String> options, String correctAnswer, ArrayList<ImageQuestion> imageQuestions) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imageQuestions = imageQuestions;
    }

    public Question() {
    }

    public boolean isAnswerCorrect() {
        System.out.println("selected: " + selectedAnswer + ", correctAnswer: " + correctAnswer);
        return selectedAnswer != null && selectedAnswer.startsWith(correctAnswer);
    }

    public ArrayList<ImageQuestion> getImageQuestions() {
        return imageQuestions;
    }

    public void setImageQuestions(ArrayList<ImageQuestion> imageQuestions) {
        this.imageQuestions = imageQuestions;
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

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", imageQuestions=" + imageQuestions +
                '}';
    }
}

