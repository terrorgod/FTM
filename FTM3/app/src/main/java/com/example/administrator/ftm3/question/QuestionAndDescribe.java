package com.example.administrator.ftm3.question;

/**
 * Created by Administrator on 2015/9/2 0002.
 */
public class QuestionAndDescribe {
    private String question;
    private String describe;
    private String questionID;
    private String studentID;
    private String date;
    private String n_answer;


    public String getStudentID() {
        return studentID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getN_answer() {
        return n_answer;
    }

    public void setN_answer(String n_answer) {
        this.n_answer = n_answer;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {

        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "QuestionAndDescribe{" +
                "question='" + question + '\'' +
                ", describe='" + describe + '\'' +
                ", questionID='" + questionID + '\'' +
                ", studentID='" + studentID + '\'' +
                ", date='" + date + '\'' +
                ", n_answer='" + n_answer + '\'' +
                '}';
    }
}
