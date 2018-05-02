package com.cit.challengeit.network;

import java.io.Serializable;
import java.util.ArrayList;


public class QuizInfo implements Serializable {

    public final ArrayList<Question> questions;
    public int currentQuestionNumber;
    public int score;
    public int answeredTime;
    public String roomId;

    public QuizInfo(String roomId, ArrayList<Question> questions, int currentQuestionNumber, int score) {
        this.roomId = roomId;
        this.questions = questions;
        this.currentQuestionNumber = currentQuestionNumber;
        this.score = score;
    }
}
