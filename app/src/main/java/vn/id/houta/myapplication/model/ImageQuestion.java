package vn.id.houta.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class ImageQuestion implements Serializable {
    byte[] bitmapBytes;
    transient Bitmap bitmap;
    String name;
    int count;

    public ImageQuestion(Bitmap bitmap, String name, int count) {
//        this.bitmap = bitmap;
        this.bitmapBytes = convertBitmapToByteArray(bitmap);
        this.name = name;
        this.count = count;
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void createBitmapFromBytes() {
        if (bitmap == null && bitmapBytes != null) {
            bitmap = convertByteArrayToBitmap(bitmapBytes);
        }
    }

    // Phương thức chuyển đổi mảng byte thành bitmap
    private Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public Bitmap getBitmap() {
        if (bitmap == null && bitmapBytes != null) {
            createBitmapFromBytes();
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return String.format("%d %s", count, name);
    }
}
