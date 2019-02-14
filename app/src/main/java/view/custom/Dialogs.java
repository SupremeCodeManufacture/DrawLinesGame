package view.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.SupremeManufacture.DrawLines.R;

import data.App;

public class Dialogs {

    public static void showSimpleDialog(final Context context, String title, String msg, boolean hasCancel, String customOkTxt, String customCancelTxt, final SimpleDialogPopUpListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        String okTxt = App.getAppCtx().getResources().getString(android.R.string.yes);
        if (customOkTxt != null)
            okTxt = customOkTxt;

        builder.setPositiveButton(okTxt,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        if (listener != null)
                            listener.onConfirmButtonClick();
                    }
                });

        if (hasCancel) {
            String negativeText = context.getResources().getString(android.R.string.cancel);
            if (customCancelTxt != null)
                negativeText = customCancelTxt;

            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (listener != null)
                                listener.onCancelButtonClick();
                        }
                    });
        }

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showShareToUserDialog(final Activity context, String res, final OnGoNextLevelListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_done_level);

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvRes = (TextView) dialog.findViewById(R.id.tv_res);
        tvRes.setText(res);

        TextView btnNext = (TextView) dialog.findViewById(R.id.txt_btn_done);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                listener.onOpenNextLevel();
            }
        });

        dialog.show();
    }

}