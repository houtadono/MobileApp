package vn.id.houta.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import vn.id.houta.myapplication.model.Question;

public class QuestionGenerator {
    public static ArrayList<Question> generateListQuestion(Context context, int numberOfQuestions) {
        ArrayList<Question> questionList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfQuestions; i++) {
//            int randomIndex = random.nextInt(questions.length);
//
//            Question question = new Question(
//                    questions[randomIndex],
//                    imageResources[randomIndex],
//                    optionA[randomIndex],
//                    optionB[randomIndex],
//                    optionC[randomIndex],
//                    optionD[randomIndex],
//                    correctAnswers[randomIndex]
//            );

//            questionList.add(question);
        }
        return questionList;
    }

    public static Question generateQuestion(String type) {
        Question question = new Question();
        Random random = new Random();
//        if (type.equals("comparison")) {
//            question = new QuestionComparison();
//            Bitmap img1 =
//        }
//
        return question;
    }


    private Object[] generateRandomImage(Context context) {
        Object[] nameAndImage = getRandomeElementImage();

        String name = (String) nameAndImage[0];
        int imageId = context.getResources().getIdentifier(
                (String) nameAndImage[1], "drawable", context.getPackageName()
        );

        Bitmap img = BitmapFactory.decodeResource(context.getResources(), imageId);
        int maxWidth = context.getResources().getDisplayMetrics().widthPixels;
        int maxHeight = context.getResources().getDisplayMetrics().heightPixels;
        img = resizeBitmap(img, maxWidth, maxHeight);

        Random rand = new Random();
        int numRand = rand.nextInt(10) + 1;
        int numCols = 1;
        int numRows = 1;

        if (numRand % 2 == 0) {
            numRows = Math.min(numRand/2, 2);
            numCols = numRand / numRows;
        } else if (numRand == 3 || numRand == 5 || numRand == 7) {
            numRows = 2;
            numCols = (numRand + 1)/2;
        }else if(numRand == 9){
            numRows = 3;
            numCols = 3;
        }

        Bitmap resultBitmap = Bitmap.createBitmap(img.getWidth() * numCols, img.getHeight() * numRows, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);

        for (int i = 0; i < numRand; i++) {
            int row = i / numCols;
            int col = i % numCols;
            int x = col * img.getWidth();
            int y = row * img.getHeight();
            canvas.drawBitmap(img, x, y, null);
        }
        return new Object[]{name, numRand, resultBitmap};
    }

    public Object[] getRandomeElementImage(){
        String jsonString = "{\"xe ô tô\": [\"ele_car1\", \"ele_car2\", \"ele_car3\", \"ele_car4\"], " +
                "\"con rùa\": [\"ele_turtle\"], " +
                "\"con ong\": [\"ele_bee\"], " +
                "\"con cừu\": [\"ele_sheep1\", \"ele_sheep2\"], " +
                "\"quả chuối\": [\"ele_banana\"], " +
                "\"quả cam\": [\"ele_orange\"], " +
                "\"máy bay\": [\"ele_plane1\", \"ele_plane2\"], " +
                "\"cây xanh\": [\"ele_tree1\", \"ele_tree2\"], " +
                "\"cây táo\": [\"ele_apple_tree1\", \"ele_apple_tree2\"]}";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray keys = jsonObject.names();

            Random random = new Random();
            int randomIndex = random.nextInt(Objects.requireNonNull(keys).length());
            String name_ele = keys.getString(randomIndex);

            JSONArray images = jsonObject.getJSONArray(name_ele);
            int img_ele_index = random.nextInt(images.length());
            String img_ele = images.getString(img_ele_index);

            return new Object[]{name_ele, img_ele};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scaleFactor = Math.min(scaleWidth, scaleHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

}