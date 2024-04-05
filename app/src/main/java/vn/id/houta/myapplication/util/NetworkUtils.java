package vn.id.houta.myapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import vn.id.houta.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NetworkUtils {
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.")
//                .setPositiveButton("Thử lại", (dialog, which) -> {
//                    retryAction.run();
//                })
//                .setNegativeButton("Huỷ", (dialog, which) -> {
//                    dialog.dismiss();
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }
}
