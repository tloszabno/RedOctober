package model;

/**
 * Created by tomek on 23.01.15.
 */
public class ScoreDTO {
    private String team;
    private Integer score;

    public ScoreDTO(String team, Integer score){
        this.score = score;
        this.team = team;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
