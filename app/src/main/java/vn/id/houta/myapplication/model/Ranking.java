package vn.id.houta.myapplication.model;

public class Ranking {
    private String id, name;
    private long score, time;

    public void setId(String id) {
        this.id = id;
    }
    public Ranking() {
    }
    public Ranking(String id, String name, long score, long time) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
