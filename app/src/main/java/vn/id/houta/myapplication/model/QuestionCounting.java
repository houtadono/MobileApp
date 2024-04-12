package vn.id.houta.myapplication.model;

import android.graphics.Bitmap;

public class QuestionCounting extends Question{
    private Bitmap imageBitmap;

    public QuestionCounting() {
        super();
    }

    public QuestionCounting(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer,
                            Bitmap imageBitmap) {
        super(questionText, optionA, optionB, optionC, optionD, correctAnswer);
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
