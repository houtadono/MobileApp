package vn.id.houta.myapplication.model;

public class Quiz {
    private String quizId;
    private String title;
    private String time, image;
    private int questionCount, level;

    public Quiz() {
    }

    public Quiz(String quizId, String title, String time, String image, int questionCount, int level) {
        this.quizId = quizId;
        this.title = title;
        this.time = time;
        this.image = image;
        this.questionCount = questionCount;
        this.level = level;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
