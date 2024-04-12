package vn.id.houta.myapplication.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class QuestionCounting extends Question{
    private Bitmap imageBitmap;

    public QuestionCounting() {
        super();
    }

    public QuestionCounting(String questionText, ArrayList<String> options, String correctAnswer,
                            Bitmap imageBitmap) {
        super(questionText, options, correctAnswer);
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
