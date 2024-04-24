package vn.id.houta.myapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.model.Lesson;
import vn.id.houta.myapplication.model.Question;

public class DialogUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static void showNetworkAlert(Context context, Runnable retryAction) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        // Inflate layout từ file XML
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_check_internet, null);
        builder.setView(dialogView);

        Button retryButton = dialogView.findViewById(R.id.btn_retry);
        Button closeButton = dialogView.findViewById(R.id.btn_close);
        AlertDialog dialog = builder.create();
        retryButton.setOnClickListener(v -> {
            retryAction.run();
            dialog.dismiss();
        });
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    public static void showFeebackVideoAlert(Context context, Runnable runnable) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback_video, null);
        dialog.setContentView(dialogView);
        dialogView.setBackgroundResource(R.drawable.bottom_sheet_background);

        FrameLayout bottomSheet = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setSkipCollapsed(true);
        behavior.setHideable(false);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        final Button btn_submit_feedback = dialogView.findViewById(R.id.btn_submit_feedback);
        btn_submit_feedback.setClickable(false);
        final boolean[] open_content = {false};
        dialogView.findViewById(R.id.btn_close_feedback).setOnClickListener(v -> {
            runnable.run();
            dialog.dismiss();
        });
        btn_submit_feedback.setOnClickListener(v -> {
            runnable.run();
            dialog.dismiss();
        });

        LinearLayout layout_content_feedback_video = dialogView.findViewById(R.id.layout_content_feedback_video);
        layout_content_feedback_video.setVisibility(View.GONE);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupOptionFeedback);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (!open_content[0]) {
                open_content[0] = true;
                layout_content_feedback_video.setVisibility(View.VISIBLE);
                btn_submit_feedback.setBackgroundColor(ContextCompat.getColor(context, R.color.my_primary));
                btn_submit_feedback.setClickable(true);
//                    dialog.setCancelable(false);
                FrameLayout bottomSheet1 = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior1 = BottomSheetBehavior.from(bottomSheet1);
                behavior1.setSkipCollapsed(true);
                behavior1.setHideable(false);
                behavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        dialog.show();
    }

    public static void showQuestionVideoAlert(Context context, int count, Lesson lesson, Runnable runnable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quiz_video, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        final boolean[] isAnswer = {false};


        Question question = QuestionGenerator.generateQuestionVideo(context, count);
        ((TextView)dialogView.findViewById(R.id.question_textview)).setText(question.getQuestionText());
        ((ImageView)dialogView.findViewById(R.id.quest_img1)).setImageBitmap(question.getImageQuestions().get(0).getBitmap());

        ((TextView)dialogView.findViewById(R.id.btn0)).setText(question.getOptions().get(0));
        ((TextView)dialogView.findViewById(R.id.btn1)).setText(question.getOptions().get(1));
        ((TextView)dialogView.findViewById(R.id.btn2)).setText(question.getOptions().get(2));
        ((TextView)dialogView.findViewById(R.id.btn3)).setText(question.getOptions().get(3));
        RadioGroup group_option = dialogView.findViewById(R.id.group_option);

        dialogView.findViewById(R.id.skipButton).setOnClickListener(v->{
            runnable.run();
            dialog.dismiss();
        });
        TextView textViewNoti = dialogView.findViewById(R.id.textViewNoti);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v->{
            int selectedRadioButtonId = group_option.getCheckedRadioButtonId();
            if(selectedRadioButtonId == -1){
                textViewNoti.setText("Chưa chọn");
                textViewNoti.setTextColor(context.getColor(R.color.orange));
            }else{
                if(isAnswer[0]){
                    lesson.getUserLesson().setLearned(true);
                    runnable.run();
                    dialog.dismiss();
                }
                String youranswer = ((TextView)dialogView.findViewById(selectedRadioButtonId)).getText().toString();
                question.setSelectedAnswer(youranswer);
                if(question.isAnswerCorrect()){
                    group_option.setClickable(false);
                    group_option.setFocusable(false);
                    textViewNoti.setText("Chính xác");
                    textViewNoti.setTextColor(context.getColor(R.color.blue_cyan2));
                    submitButton.setText("Tiếp tục");
                    isAnswer[0] = true;
                }else{
                    textViewNoti.setText("Trả lời sai");
                    textViewNoti.setTextColor(context.getColor(R.color.red_600));
                    submitButton.setText("Gửi lại");
                }
            }
        });
        dialog.show();
    }
}
