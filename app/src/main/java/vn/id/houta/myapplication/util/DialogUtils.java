package vn.id.houta.myapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import vn.id.houta.myapplication.R;

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
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryAction.run();
                dialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
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
