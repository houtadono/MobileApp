package vn.id.houta.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    private String quizId;
    private String title;
    private String image, type;
    private int time, questionCount, level;
    private ArrayList<Question> listQuest;

    public Quiz() {
    }

    public Quiz(String type, int time, int questionCount) {
        this.type = type;
        this.time = time;
        this.questionCount = questionCount;
    }

    public Quiz(String quizId, String title, int time, String image, String type, int questionCount, int level) {
        this.quizId = quizId;
        this.title = title;
        this.time = time;
        this.image = image;
        this.type = type;
        this.questionCount = questionCount;
        this.level = level;
    }

    private int correct, error, warning;
    private String timeUse, totalCorrect;
    public void setCompletion(int correct, int error, int warning, int totalTime) {
        this.correct = correct;
        this.error = error;
        this.warning = warning;
        int minutes = totalTime/60;
        int seconds = totalTime%60;
        this.timeUse = String.format("Thời gian: %02d:%02d", minutes,seconds);
        this.totalCorrect = String.format("%02d/%02d", correct, this.questionCount);
    }

    public ArrayList<Question> getListQuest() {
        return listQuest;
    }

    public void setListQuest(ArrayList<Question> listQuest) {
        this.listQuest = listQuest;
    }

    public int getCorrect() {
        return correct;
    }

    public int getError() {
        return error;
    }

    public int getWarning() {
        return warning;
    }

    public String getTimeUse() {
        return timeUse;
    }

    public String getTotalCorrect() {
        return totalCorrect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
