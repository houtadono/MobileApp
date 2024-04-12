package vn.id.houta.myapplication.module4;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.model.Question;
import vn.id.houta.myapplication.model.Quiz;
import vn.id.houta.myapplication.util.QuestionGenerator;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    Question currentQuestion;
    private Quiz quiz;
    private ArrayList<Question> questionList;

    private Button btn0, btn1, btn2, btn3, nextBtn;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ_EXTRA");
        questionList = QuestionGenerator.generateListQuestion(this, quiz.getQuestionCount());
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
        long totalTimeInMillis = quiz.getTime()* 60 * 1000L;
        new CountDownTimer(totalTimeInMillis, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                ((TextView)findViewById(R.id.timer_indicator_textview)).setText(
                        String.format("%02d:%02d", minutes,remainingSeconds));

                // Update timer UI if needed
            }

            @Override
            public void onFinish() {
                // Finish the quiz
            }
        }.start();
    }

    private void loadQuestions() {
        if (currentQuestionIndex < questionList.size()) {
            ((TextView)findViewById(R.id.question_indicator_textview)).setText(
                    String.format("Question %d/%d",currentQuestionIndex+1,questionList.size()));

            currentQuestion = questionList.get(currentQuestionIndex);
            btn0.setText(currentQuestion.getOptions().get(0));
            btn1.setText(currentQuestion.getOptions().get(1));
            btn2.setText(currentQuestion.getOptions().get(2));
        } else {
            // No more questions, finish the quiz
            finishQuiz();
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.next_btn) {
                currentQuestion.setSelectedAnswer(selectedAnswer);
                // Handle next button click
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select answer to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentQuestion.isAnswerCorrect()) {
                    score++;
                    Log.i("Score of quiz", String.valueOf(score));
                }
                currentQuestionIndex++;
                loadQuestions();
        } else if (viewId == R.id.btn0 || viewId == R.id.btn1 || viewId == R.id.btn2 || viewId == R.id.btn3) {
                Button clickedBtn = (Button) view;
                selectedAnswer = clickedBtn.getText().toString();
//                clickedBtn.setBackgroundColor(Color.parseColor("#FFA500")); // Orange color
        }
    }

    private void finishQuiz() {
        // Implement finish quiz logic, similar to your previous method
        // This method should show the final score and allow the user to finish the quiz
    }
}
