package vn.id.houta.myapplication.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionComparison extends Question{
    private Bitmap imageLeftBitmap;
    private Bitmap imageRightBitmap;

    public QuestionComparison() {
        super();
    }

    public QuestionComparison(String questionText, String correctAnswer,
                              Bitmap imageLeftBitmap, Bitmap imageRightBitmap) {
        super(questionText, new ArrayList<>(Arrays.asList("A. <", "B. >", "C. =")), correctAnswer);
        this.imageLeftBitmap = imageLeftBitmap;
        this.imageRightBitmap = imageRightBitmap;
    }

    // Getter và setter cho imageLeftBitmap và imageRightBitmap
    public Bitmap getImageLeftBitmap() {
        return imageLeftBitmap;
    }

    public void setImageLeftBitmap(Bitmap imageLeftBitmap) {
        this.imageLeftBitmap = imageLeftBitmap;
    }

    public Bitmap getImageRightBitmap() {
        return imageRightBitmap;
    }

    public void setImageRightBitmap(Bitmap imageRightBitmap) {
        this.imageRightBitmap = imageRightBitmap;
    }
}
