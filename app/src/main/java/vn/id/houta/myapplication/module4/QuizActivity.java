package vn.id.houta.myapplication.module4;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.rachitgoyal.segmented.SegmentedProgressBar;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Locale;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;
import vn.id.houta.myapplication.model.Question;
import vn.id.houta.myapplication.model.Quiz;
import vn.id.houta.myapplication.util.QuestionGenerator;

public class QuizActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    Question currentQuestion;
    private Quiz quiz;
    private ArrayList<Question> questionList;

    private RadioButton btn0, btn1, btn2, btn3;
    private RadioGroup group_option;
    private ArrayList<RadioButton> listButton;

    private Button nextBtn;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0, correct = 0, error = 0, totalTime = 0;
    private SegmentedProgressBar progressBar;
    private ArrayList<Integer> enableDivisions;
    private TextToSpeech tts;
    private CountDownTimer timer;
    private TextView textViewTimer;
    private boolean click = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ_EXTRA");
        listButton = new ArrayList<>();
        tts = new TextToSpeech(this, this);
        questionList = QuestionGenerator.generateListQuestion(this, quiz.getQuestionCount(), quiz.getType());
        enableDivisions = new ArrayList<>();
        progressBar = findViewById(R.id.segmented_progress_bar);
        progressBar.setDivisions(quiz.getQuestionCount());
        textViewTimer = findViewById(R.id.timer_indicator_textview);
        nextBtn = findViewById(R.id.next_btn);
        if(quiz.getType().equals("random")){ // random
            findViewById(R.id.group_option_have_next).setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            group_option = findViewById(R.id.group_option);
            btn0 = findViewById(R.id.btn0);
            btn1 = findViewById(R.id.btn1);
            btn2 = findViewById(R.id.btn2);
            btn3 = findViewById(R.id.btn3);
            listButton.add(btn0);
            listButton.add(btn1);
            listButton.add(btn2);
            listButton.add(btn3);
            for(int i = 0; i < 4; i++){
                final int index = i;
                listButton.get(i).setOnClickListener(view -> {
                    if(click){
                        click = false;
                        for(int j = 0; j < 4; j++){
                            RadioButton r = listButton.get(j);
                            r.setClickable(false);
                            r.setFocusable(false);
                            r.setChecked(j==index);
                        }
                        selectedAnswer = ((RadioButton)view).getText().toString();
                        currentQuestion.setSelectedAnswer(selectedAnswer);
                        timer.cancel();
                        animateCheckAnswer(listButton.get(index));
                    }
                });
            }
        }
        else {
            if(quiz.getType().equals("comparison")){
                findViewById(R.id.group_option_have_next).setVisibility(View.GONE);
                btn0 = findViewById(R.id.btn0);
                btn1 = findViewById(R.id.btn1);
                btn2 = findViewById(R.id.btn2);
                btn3 = findViewById(R.id.btn3);
                btn3.setVisibility(View.GONE);
            }else{
                findViewById(R.id.group_option).setVisibility(View.GONE);
                btn0 = findViewById(R.id.bt0);
                btn1 = findViewById(R.id.bt1);
                btn2 = findViewById(R.id.bt2);
                btn3 = findViewById(R.id.bt3);
            }
            listButton.add(btn0);
            listButton.add(btn1);
            listButton.add(btn2);
            listButton.add(btn3);
            for(int i = 0; i < 4; i++){
                final int index = i;
                listButton.get(i).setOnClickListener(view -> {
                    for(int j = 0; j < 4; j++){
                        listButton.get(j).setChecked(j==index);
                    }
                    selectedAnswer = ((RadioButton)view).getText().toString();
                    nextBtn.setEnabled(true);
                });
            }

            View.OnClickListener clickNextListener = view -> {
                currentQuestion.setSelectedAnswer(selectedAnswer);
                if (currentQuestion.isAnswerCorrect()) {
                    correct += 1;
                }else error += 1;
                currentQuestionIndex++;
                ((RadioGroup)findViewById(R.id.group_option)).clearCheck();
                loadQuestions();
            };
            nextBtn.setOnClickListener(clickNextListener);
        }

        findViewById(R.id.question_textview).setOnClickListener(v->{
            speakText(currentQuestion.getQuestionText());
        });
        findViewById(R.id.card_img1).setOnClickListener(v->{
            speakText((currentQuestion).getImageQuestions().get(0).getDescription());
        });
        findViewById(R.id.card_img2).setOnClickListener(v->{
            speakText((currentQuestion).getImageQuestions().get(1).getDescription());
        });

        loadQuestions();
        if(!quiz.getType().equals("random"))
            startTimerQuiz();

    }

    private void startTimerQuiz() {
        long totalTimeInMillis = quiz.getTime()* 60 * 1000L;
        timer = new CountDownTimer(totalTimeInMillis, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                textViewTimer.setText(
                        String.format("%02d:%02d", minutes,remainingSeconds));
                totalTime += 1;
            }
            @Override
            public void onFinish() {
                // Finish the quiz
            }
        }.start();
    }

    private void startTimerQuest(){
        long totalTimeInMillis = quiz.getTime() * 1000L;
        timer = new CountDownTimer(totalTimeInMillis, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                textViewTimer.setText(
                        String.format("%02d:%02d", minutes,remainingSeconds));
                totalTime += 1;
            }
            @Override
            public void onFinish() {
                animateCheckAnswer(null);
            }
        }.start();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("vi"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(QuizActivity.this, "Ngôn ngữ Tiếng Việt không được hỗ trợ.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(QuizActivity.this, "Không thể khởi động Text-to-Speech.", Toast.LENGTH_SHORT).show();
        }
    }
    private void speakText(String text) {
        if (tts != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void loadQuestions() {
        if (currentQuestionIndex < questionList.size()) {
            enableDivisions.add(currentQuestionIndex);
            progressBar.setEnabledDivisions(enableDivisions);
            ((TextView)findViewById(R.id.question_indicator_textview)).setText(
                    String.format("Câu hỏi: %d/%d",currentQuestionIndex+1,questionList.size())
            );
            selectedAnswer = "";
            currentQuestion = questionList.get(currentQuestionIndex);

            Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

            findViewById(R.id.card_question).startAnimation(slideInRight);


            ((TextView) findViewById(R.id.question_textview)).setText(currentQuestion.getQuestionText());

            listButton.forEach(button -> {
                if(quiz.getType().equals("random")){
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.light_gray)));
                    button.setTextColor(getColor(R.color.gray));
                }
                button.setClickable(true);
                button.setChecked(false);
            });
            btn0.setText(currentQuestion.getOptions().get(0));
            btn1.setText(currentQuestion.getOptions().get(1));
            btn2.setText(currentQuestion.getOptions().get(2));
            boolean has4options = currentQuestion.getOptions().size() == 4;
            if(btn3.getVisibility() != View.GONE){
                btn3.setVisibility(has4options ? View.VISIBLE : View.INVISIBLE);
                btn3.setText(has4options ? currentQuestion.getOptions().get(3) : null);
            }

            ((TextView)findViewById(R.id.question_textview)).setText(
                    currentQuestion.getQuestionText()
            );
            ((ImageView)findViewById(R.id.quest_img1)).setImageBitmap(currentQuestion.getImageQuestions().get(0).getBitmap());
            int count_img = currentQuestion.getImageQuestions().size();
            if( count_img == 2){
                ((ImageView)findViewById(R.id.quest_img2)).setImageBitmap(currentQuestion.getImageQuestions().get(1).getBitmap());
            }
            findViewById(R.id.card_img2).setVisibility(count_img != 2 ? View.GONE : View.VISIBLE);

            if(quiz.getType().equals("random"))
                startTimerQuest();
            if(currentQuestionIndex+1 == questionList.size()){
                nextBtn.setText("Hoàn thành");
            }
            nextBtn.setEnabled(false);
        } else {
            timer.cancel();
            finishQuiz();
        }
    }

    private void animateCheckAnswer(RadioButton radioButton) {
        boolean no_select = radioButton == null;

        boolean is_correct = currentQuestion.isAnswerCorrect();
        if (!no_select){
            if(is_correct) correct+=1;
            else error += 1;
        }
        if(is_correct) score += 1;
        int startColor = ContextCompat.getColor(getApplicationContext(), R.color.light_gray);
        int endColor = is_correct ?
                ContextCompat.getColor(getApplicationContext(), R.color.active) :
                ContextCompat.getColor(getApplicationContext(), R.color.red_600);

        FancyToast.makeText(this,
                no_select ? "Hết thời gian" : is_correct ? "Câu trả lời chính xác" : "Câu trả lời sai",
                FancyToast.LENGTH_SHORT,
                no_select ? FancyToast.WARNING :is_correct ? FancyToast.SUCCESS : FancyToast.ERROR,false
        ).show();
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.setDuration(800);
        if(!no_select){
            colorAnimator.addUpdateListener(animator -> {
                int animatedColor = (int) animator.getAnimatedValue();
                radioButton.setTextColor(getColor(R.color.white));
                radioButton.setBackgroundTintList(ColorStateList.valueOf(animatedColor));
            });
        }
       
        colorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(() -> {
                    currentQuestionIndex++;
                    loadQuestions();
                    click = true;
                }, 1000);
            }
        });
        colorAnimator.start();
    }


    private void finishQuiz() {
        int warning = quiz.getQuestionCount() - correct - error;
        quiz.setCompletion(correct, error, warning, totalTime);
        new FirebaseHelper().updateRankUser(correct*1000, totalTime);
//        for(Question q : questionList){
//            q.setImageQuestions(null);
//        }
        finish();
        try{
            Intent intent = new Intent(getApplicationContext(), QuizCompletionActivity.class);
            intent.putExtra("QUIZ_EXTRA", quiz);
            intent.putExtra("LISTQUEST_EXTRA", questionList);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }catch (Exception e){
            Intent intent = new Intent(getApplicationContext(), QuizCompletionActivity.class);
            intent.putExtra("QUIZ_EXTRA", quiz);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
