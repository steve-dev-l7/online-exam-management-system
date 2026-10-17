package com.exammanagement.model;

public class Questions {
    
    private String questionText;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String correctAnswer;

    public Questions(){}

    public String getQuestionText(){ return questionText; }
    public void setQuestionText(String questionTet){this.questionText=questionTet;}

    public String getOptA() { return optA; }
    public void setOptA(String optA) { this.optA = optA; }

    public String getOptB() { return optB; }
    public void setOptB(String optB) { this.optB = optB; }

    public String getOptC() { return optC; }
    public void setOptC(String optC) { this.optC = optC; }

    public String getOptD() { return optD; }
    public void setOptD(String optD) { this.optD = optD; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
