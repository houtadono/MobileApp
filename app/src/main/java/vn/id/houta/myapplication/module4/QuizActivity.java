package vn.id.houta.myapplication.module4;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vn.id.houta.myapplication.R;
//import vn.id.houta.myapplication.module4.QuestionModel;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

//    private List<QuestionModel> questionModelList;
    private String time = "";

    private Button btn0, btn1, btn2, btn3, nextBtn;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        nextBtn = findViewById(R.id.next_btn);

        // Set click listeners
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        // Load questions
        loadQuestions();

        // Start timer
        startTimer();
    }

    private void startTimer() {
        long totalTimeInMillis = Integer.parseInt(time) * 60 * 1000L;
        new CountDownTimer(totalTimeInMillis, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                // Update timer UI if needed
            }

            @Override
            public void onFinish() {
                // Finish the quiz
            }
        }.start();
    }

    private void loadQuestions() {
        // Load questions from wherever you retrieve them
        // For example:
        // questionModelList = getQuestionsFromDatabaseOrAPI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                // Handle next button click
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select answer to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedAnswer.equals(questionModelList.get(currentQuestionIndex).getCorrect())) {
                    score++;
                    Log.i("Score of quiz", String.valueOf(score));
                }
                currentQuestionIndex++;
                loadQuestions();
                break;
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
                // Handle option button click
                Button clickedBtn = (Button) view;
                selectedAnswer = clickedBtn.getText().toString();
                // Change background color of clicked button
                clickedBtn.setBackgroundColor(Color.parseColor("#FFA500")); // Orange color
                break;
        }
    }

    private void finishQuiz() {
        // Implement finish quiz logic, similar to your previous method
        // This method should show the final score and allow the user to finish the quiz
    }
}
