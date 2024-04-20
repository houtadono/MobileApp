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
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import vn.id.houta.myapplication.model.ImageQuestion;
import vn.id.houta.myapplication.model.Question;

public class QuestionGenerator {
    public static ArrayList<Question> generateListQuestion(Context context, int numberOfQuestions, String type) {
        ArrayList<Question> questionList = new ArrayList<>();
        int count_image = 0;
        if(type.equals("comparison")){
            count_image = 2;
        }else if(type.equals("counting")){
            count_image = 1;
        }

        for (int i = 0; i < numberOfQuestions; i++) {
            if(count_image == 0){
                questionList.add(generateQuestion(context, new Random().nextInt(2)+1));
            }else
                questionList.add(generateQuestion(context, count_image));
        }
        return questionList;
    }

//    public Question generateQuestion(String type, Context context) {
//        Question question = new Question();
//        String title_quest = null;
//        if (type.equals("comparison")) {
//            question = new QuestionComparison();
//            Object[] questionInfo1 = generateRandomImage(context);
//            String name1 = (String) questionInfo1[0];
//            int count1 = (int) questionInfo1[1];
//            Bitmap img1 = (Bitmap) questionInfo1[2];
//
//            Object[] questionInfo2 = generateRandomImage(context);
//            String name2 = (String) questionInfo2[0];
//            int count2 = (int) questionInfo2[1];
//            Bitmap img2 = (Bitmap) questionInfo2[2];
//            ((QuestionComparison) question).setImageRightBitmap(img2);
//
//            title_quest = String.format("So sánh số lượng %s và %s", name1, name2);
//        }
//        question.setQuestionText(title_quest);
//
////
//        return question;
//    }

    public static Question generateQuestion(Context context, int count_image) {
        Random random = new Random();
        ArrayList<String> options = new ArrayList<>();
        ArrayList<ImageQuestion> imageQuestions = new ArrayList<ImageQuestion>();
        String questionText = null, correctAnswer=null;
        Object[] nameAndImage1 = getRandomeElementImage();
        String name1 = (String) nameAndImage1[0];
        String source1 = (String) nameAndImage1[1];
        int count1 =  random.nextInt(10) + 1;
        Bitmap img1 = generateBitmapFromEleImg(context, source1, count1, count_image == 1);
        imageQuestions.add(new ImageQuestion(img1,name1,count1));
        if(count_image == 1){
            questionText = "Đếm số lượng " + name1+"?";

            ArrayList<Integer> numbers = new ArrayList<>();
            while(numbers.size() < 3){
                int randomNumber = random.nextInt(10) + 1;
                if (randomNumber != count1 & !numbers.contains(randomNumber)) {
                    numbers.add(randomNumber);
                }
            }
            int randomIndex = random.nextInt(numbers.size() + 1);
            numbers.add(randomIndex, count1);

            for(int i = 0; i < 4; i++){
                options.add(String.format("%s. %d",(char) ('A' + i), numbers.get(i)));
            }
            correctAnswer = String.valueOf((char)('A'+randomIndex));
        }
        if(count_image == 2){
            Object[] nameAndImage2 = getRandomeElementImage();
            String name2 = (String) nameAndImage2[0];
            String source2 = (String) nameAndImage2[1];
            while(name2.equals(name1)){
                nameAndImage2 = getRandomeElementImage();
                name2 = (String) nameAndImage2[0];
                source2 = (String) nameAndImage2[1];
            }
            int count2 =  random.nextInt(10) + 1;
            Bitmap img2 = generateBitmapFromEleImg(context, source2, count2, false);
            imageQuestions.add(new ImageQuestion(img2,name2,count2));

            questionText = String.format("So sánh số lượng %s và %s ?", name1, name2);
            options.addAll(Arrays.asList("A. <", "B. >", "C. ="));
            correctAnswer = (count1 < count2) ? "A" : (count1 > count2) ? "B" : "C";
        }

        return new Question(questionText, options, correctAnswer, imageQuestions);
    }



    private static Bitmap generateBitmapFromEleImg(Context context, String source, int numRand, boolean horizontal) {
        int imageId = context.getResources().getIdentifier(
                source, "drawable", context.getPackageName()
        );

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

        if(!horizontal & numRand>6){ // dọc
            numRows = 3 + (numRand / 10);
            numCols = 3;
        }

        Bitmap img = BitmapFactory.decodeResource(context.getResources(), imageId);
        int maxWidth = context.getResources().getDisplayMetrics().widthPixels/4;
        int maxHeight = context.getResources().getDisplayMetrics().heightPixels/4;

        img = resizeBitmap(img, maxWidth, maxHeight);
        Bitmap resultBitmap = Bitmap.createBitmap(img.getWidth() * numCols, img.getHeight() * numRows, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        if(numRand==1){
            img = resizeBitmap(img, img.getWidth() / 2, img.getHeight() / 2);
            canvas.drawBitmap(img, resultBitmap.getWidth()/4, resultBitmap.getHeight()/4, null);
        }else {
            for (int i = 0; i < numRand; i++) {
                int row = i / numCols;
                int col = i % numCols;
                int x = col * img.getWidth();
                int y = row * img.getHeight();
                canvas.drawBitmap(img, x, y, null);
            }
        }
        return resultBitmap;
    }

    public static Object[] getRandomeElementImage(){
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

    private static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
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
