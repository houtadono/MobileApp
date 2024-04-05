package vn.id.houta.myapplication.util;

import static androidx.core.content.ContextCompat.getColorStateList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import vn.id.houta.myapplication.R;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FeedbackUtils {
    public static void showFeebackVideoAlert(Context context) {
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
        dialogView.findViewById(R.id.btn_close_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout layout_content_feedback_video = dialogView.findViewById(R.id.layout_content_feedback_video);
        layout_content_feedback_video.setVisibility(View.GONE);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupOptionFeedback);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!open_content[0]) {
                    open_content[0] = true;
                    layout_content_feedback_video.setVisibility(View.VISIBLE);
                    btn_submit_feedback.setBackgroundColor(ContextCompat.getColor(context, R.color.my_primary));
                    btn_submit_feedback.setClickable(true);
//                    dialog.setCancelable(false);
                    FrameLayout bottomSheet = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setSkipCollapsed(true);
                    behavior.setHideable(false);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        dialog.show();
    }
}
