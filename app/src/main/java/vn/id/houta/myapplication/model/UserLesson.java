package vn.id.houta.myapplication.model;

public class UserLesson {
    int timesStudy = 0;
    boolean isLiked = false, isLearned = false;
//    boolean
    public UserLesson(){
    }

    public int getTimesStudy() {
        return timesStudy;
    }

    public void setTimesStudy(int timesStudy) {
        this.timesStudy = timesStudy;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }
}
