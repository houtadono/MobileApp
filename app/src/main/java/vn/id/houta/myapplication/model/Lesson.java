package vn.id.houta.myapplication.model;

public class Lesson {
    int lessonId;
    String title, topic, name;
    int level, timeStudy, timeTotal;

    public Lesson(int lessonId, String name, String topic, int level, int timeStudy, int timeTotal) {
        this.lessonId = lessonId;
        this.name = name;
        this.topic = topic;
        this.title = String.format("%s | %s", topic, name);
        this.level = level;
        this.timeStudy = timeStudy;
        this.timeTotal = timeTotal;
    }

    public int getPercent(){
        int percent = timeStudy * 100 / timeTotal;
        return  percent;
    }

    public String getTimeStudyStr(){
        return String.format("%d phút %d giây", this.timeStudy / 60, this.timeStudy % 60);
    }

    public String getTimeVideo(){
        return String.format("%d phút\n%d giây", this.timeTotal / 60, this.timeTotal % 60);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String chapter) {
        this.topic = chapter;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimeStudy() {
        return timeStudy;
    }

    public void setTimeStudy(int timeStudy) {
        this.timeStudy = timeStudy;
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }
}
