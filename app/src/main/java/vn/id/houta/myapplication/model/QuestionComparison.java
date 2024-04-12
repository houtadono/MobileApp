package vn.id.houta.myapplication.model;

import android.graphics.Bitmap;

public class QuestionComparison extends Question{
    private Bitmap imageLeftBitmap;
    private Bitmap imageRightBitmap;

    public QuestionComparison() {
        super();
    }

    public QuestionComparison(String questionText, String optionA, String optionB, String optionC, String correctAnswer,
                              Bitmap imageLeftBitmap, Bitmap imageRightBitmap) {
        super(questionText, optionA, optionB, optionC, null, correctAnswer); // Không cần optionD
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
