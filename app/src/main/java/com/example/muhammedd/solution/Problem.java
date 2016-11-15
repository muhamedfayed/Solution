package com.example.muhammedd.solution;

import java.io.Serializable;

/**
 * Created by Muhammedd on 11/6/2016.
 */

public class Problem implements Serializable {

    public int id;
    public String problemHead,
           problemBody,
           problemBy,
           soulution,
           solvedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProblemHead() {
        return problemHead;
    }

    public void setProblemHead(String problemHead) {
        this.problemHead = problemHead;
    }

    public String getProblemBody() {
        return problemBody;
    }

    public void setProblemBody(String problemBody) {
        this.problemBody = problemBody;
    }

    public String getProblemBy() {
        return problemBy;
    }

    public void setProblemBy(String problemBy) {
        this.problemBy = problemBy;
    }

    public String getSoulution() {
        return soulution;
    }

    public void setSoulution(String soulution) {
        this.soulution = soulution;
    }

    public String getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }
}
