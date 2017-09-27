package com.softians.poller.model;

/**
 * Created by HP on 11-09-2017.
 */

public class QuestionDataModel {
    private String question;
    private String answer=null;
    private int question_id;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getquestionId() {
        return question_id;
    }

    public void setquestionId(int question_id) {
        this.question_id = question_id;
    }

    public QuestionDataModel(int question_id,String question) {
        this.question = question;
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
