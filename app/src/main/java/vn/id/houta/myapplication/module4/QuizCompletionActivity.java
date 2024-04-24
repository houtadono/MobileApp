package vn.id.houta.myapplication.module4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.model.Question;
import vn.id.houta.myapplication.model.Quiz;

public class QuizCompletionActivity extends AppCompatActivity {
    private Quiz quiz;
    private ArrayList<Question> listQuestion;
    private ConstraintLayout layout_present_quiz;
    private ListView listViewQuestion;
    private FrameLayout fragment_bxh;
    private QuestionListViewAdapter questionListViewAdapter;

    private boolean showReviewQuiz = false, showLeaderboard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_completion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(showReviewQuiz){
                    layout_present_quiz.setVisibility(View.VISIBLE);
                    listViewQuestion.setVisibility(View.GONE);
                    showReviewQuiz = false;
                }else if(showLeaderboard){
                    layout_present_quiz.setVisibility(View.VISIBLE);
                    fragment_bxh .setVisibility(View.GONE);
                    showLeaderboard = false;
                }else{
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        toolbar.setNavigationOnClickListener(v -> {
            callback.handleOnBackPressed();
        });
        layout_present_quiz = findViewById(R.id.layout_present_quiz);

        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ_EXTRA");
        try {
            listQuestion = (ArrayList<Question>) getIntent().getSerializableExtra("LISTQUEST_EXTRA");
        } catch (Exception e){
            listQuestion = null;
        }

        ((TextView) findViewById(R.id.textViewCorrectTotal)).setText(quiz.getTotalCorrect());
        int correct = quiz.getCorrect();
        int error = quiz.getError();
        int warning = quiz.getWarning();
        ((TextView) findViewById(R.id.textViewCorrect)).setText(String.valueOf(correct));
        ((TextView) findViewById(R.id.textViewError)).setText(String.valueOf(error));
        ((TextView) findViewById(R.id.textViewWarning)).setText(String.valueOf(warning));
        ((TextView) findViewById(R.id.textViewTimeUse)).setText(String.valueOf(quiz.getTimeUse()));
        int score = correct * 1000;
        ((TextView) findViewById(R.id.textViewScore)).setText("Điểm số: "+ new DecimalFormat("#,###").format(score));


        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(correct));
        entries.add(new PieEntry(error));
        entries.add(new PieEntry(warning));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getColor(R.color.active));
        colors.add(getColor(R.color.red_600));
        colors.add(getColor(R.color.orange));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        ValueFormatter formatter = new IntValueFormatter();
        data.setValueFormatter(formatter);
        pieChart.setData(data);

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000);
        pieChart.invalidate();


        findViewById(R.id.btn_play_again).setOnClickListener(v->{
            finish();
            Quiz newQuiz = new Quiz(quiz.getType(), quiz.getTime(), quiz.getQuestionCount());
            Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
            intent.putExtra("QUIZ_EXTRA", newQuiz);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        Button btn_review_quiz = findViewById(R.id.btn_review_quiz);
        if(listQuestion == null){
            btn_review_quiz.setVisibility(View.GONE);
        }
        btn_review_quiz.setOnClickListener(v->{
            if(listViewQuestion == null){
                listViewQuestion = findViewById(R.id.view_pager_review_again);
                questionListViewAdapter = new QuestionListViewAdapter(getApplicationContext(), listQuestion);
                listViewQuestion.setAdapter(questionListViewAdapter);
            }
            layout_present_quiz.setVisibility(View.GONE);
            listViewQuestion.setVisibility(View.VISIBLE);
            showReviewQuiz = true;
        });

        Button btn_leaderboard = findViewById(R.id.btn_leaderboard);
        if(quiz.getType().equals("random")){
            btn_leaderboard.setVisibility(View.GONE);
        }
        btn_leaderboard.setOnClickListener(v -> {
            if(fragment_bxh == null){
                fragment_bxh = findViewById(R.id.fragment_bxh);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_bxh, new Module43Fragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
            layout_present_quiz.setVisibility(View.GONE);
            fragment_bxh.setVisibility(View.VISIBLE);
            showLeaderboard = true;
        });
        findViewById(R.id.btn_back).setOnClickListener(v->{
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
    private static class IntValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }
    class QuestionListViewAdapter extends BaseAdapter {
        ArrayList<Question> listQuestion;

        public QuestionListViewAdapter(Context context, ArrayList<Question> listQuestion) {
            this.listQuestion = listQuestion;
        }

        @Override
        public int getCount() {
            //Cần trả về số phần tử mà ListView hiện thị
            return this.listQuestion.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listQuestion.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewQuest;
            if (convertView == null) {
                viewQuest = View.inflate(parent.getContext(), R.layout.item_question_review, null);
            } else viewQuest = convertView;

            final Question question = (Question) getItem(position);
            ((TextView)viewQuest.findViewById(R.id.textViewQuestNumber)).setText(String.format("Câu hỏi %d:", position+1));
            if(question.getImageQuestions() != null){
                ((ImageView)viewQuest.findViewById(R.id.quest_img1)).setImageBitmap(question.getImageQuestions().get(0).getBitmap());
                int count_img = question.getImageQuestions().size();
                if( count_img == 2){
                    ((ImageView)viewQuest.findViewById(R.id.quest_img2)).setImageBitmap(question.getImageQuestions().get(1).getBitmap());
                }
                viewQuest.findViewById(R.id.card_img2).setVisibility(count_img != 2 ? View.GONE : View.VISIBLE);
            }


            ((TextView)viewQuest.findViewById(R.id.question_textview)).setText(
                    question.getQuestionText()
            );

            ArrayList<String> ops = question.getOptions();
            ArrayList<TextView> textViewsOP = new ArrayList<>();
            textViewsOP.add(viewQuest.findViewById(R.id.op1));
            textViewsOP.add(viewQuest.findViewById(R.id.op2));
            textViewsOP.add(viewQuest.findViewById(R.id.op3));
            textViewsOP.add(viewQuest.findViewById(R.id.op4));
            for(int i=0; i<ops.size(); i++){
                String text = ops.get(i);
                textViewsOP.get(i).setText(text);
                textViewsOP.get(i).setTextColor(getColor(R.color.black));
            }
            if(ops.size()==3)
                textViewsOP.get(3).setVisibility(View.GONE);
            TextView textViewResult = viewQuest.findViewById(R.id.textViewResult);
            String select = question.getSelectedAnswer();
            if(question.isAnswerCorrect()){
                textViewResult.setText("Trả lời đúng");
                textViewResult.setTextColor(getColor(R.color.active));
                textViewsOP.get((int)(select.charAt(0)-'A')).setTextColor(getColor(R.color.active));
            }else{
                if(select == null){
                    textViewResult.setText("Chưa chọn câu trả lời! Câu trả lời đúng là: " + question.getCorrectAnswer());
                    textViewResult.setTextColor(getColor(R.color.orange));
                }else{
                    textViewResult.setText("Trả lời sai! Câu trả lời đúng là: " + question.getCorrectAnswer());
                    textViewResult.setTextColor(getColor(R.color.red_600));
                    textViewsOP.get((int)(select.charAt(0)-'A')).setTextColor(getColor(R.color.red_600));
                }
            }

            return viewQuest;
        }
    }

}