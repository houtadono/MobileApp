package vn.id.houta.myapplication.model;

public class UserLesson {
    int timesStudy = 0;
    boolean liked = false, learned = false;
//    boolean
    public UserLesson(){
    }

    public UserLesson(int timesStudy, boolean liked, boolean learned) {
        this.timesStudy = timesStudy;
        this.liked = liked;
        this.learned = learned;
    }

    public int getTimesStudy() {
        return timesStudy;
    }

    public void setTimesStudy(int timesStudy) {
        this.timesStudy = timesStudy;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    @Override
    public String toString() {
        return "UserLesson{" +
                "timesStudy=" + timesStudy +
                ", isLiked=" + liked +
                ", isLearned=" + learned +
                '}';
    }
}
