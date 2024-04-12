package vn.id.houta.myapplication.model;

import androidx.annotation.NonNull;

public class Lesson {
    String lessonId;
    String title, topic, name, link, timeTotal;
    int stt, level, timesTotal, likeCount ;
    UserLesson userLesson;

    public Lesson() {
        // Default constructor
    }
    public Lesson(int stt, String name, String topic, int level, String timeTotal, String link) {
        this.stt = stt;
        this.name = name;
        this.topic = topic;
        this.level = level;
        this.timeTotal = timeTotal;
        this.link = link;
    }

    public void afterObjectCreation(String lessonId){
        this.title = String.format("%s | %s", this.topic, this.name);
        this.timesTotal =  convertTimeStringToSeconds(this.timeTotal);
        this.lessonId = lessonId;
        this.userLesson = new UserLesson();
    }

    public String getTimeStudyStr(){
        int timesStudy = this.userLesson.getTimesStudy();
        int m = timesStudy/60;
        int s = timesStudy%60;
        return m + " phút " + s + " giây";
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public UserLesson getUserLesson() {
        return userLesson;
    }

    public void setUserLesson(UserLesson userLesson) {
        this.userLesson = userLesson;
    }

    public int getPercent(){
        return this.userLesson.getTimesStudy()*100 / this.timesTotal;
    }
    public String getTimeVideo(){
        int m = this.timesTotal/60;
        int s = this.timesTotal%60;
        return m + " phút\n" + s + " giây";
    }
    public static int convertTimeStringToSeconds(String timeString) {
        String[] parts = timeString.split(":");
        int m = Integer.parseInt(parts[0]);
        int s = Integer.parseInt(parts[1]);
        return m * 60 + s;
    }
    @NonNull
    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", level=" + level +
                ", timeTotal=" + timeTotal +
                '}';
    }
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
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

    public String getTimeTotal() {
        return timeTotal;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getTimesTotal() {
        return timesTotal;
    }

    public void setTimesTotal(int timesTotal) {
        this.timesTotal = timesTotal;
    }

    public void setTimeTotal(String timeTotal) {
        this.timeTotal = timeTotal;
    }

}
