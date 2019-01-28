package be.patricegautot.getorganized;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import be.patricegautot.getorganized.utilities.SharedPrefUtils;

public class DeleteTaskOnedayDialog extends DialogFragment {

    public interface OnedayDialogListener {
        public void onOnedayDialogPositiveClick(DialogFragment dialog);
        public void onOnedayDialogNegativeClick(DialogFragment dialog);
    }

    OnedayDialogListener mListener;

    public DeleteTaskOnedayDialog() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DeleteTaskOnedayDialog(RecyclerView.ViewHolder v) {
        super();
        setOnedayDialogListener(v);
    }

    public void setOnedayDialogListener(RecyclerView.ViewHolder v){
        mListener = (OnedayDialogListener) v;
    }

    public static final int DELETION_TYPE_EVERYDAY = 0;
    public static final int DELETION_TYPE_ONEYDAY = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        View checkBoxView = View.inflate(getContext(), R.layout.checkbox, null);
        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox_dontaskmeagain);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Log.e("AlertOneDayDialog", "Don't ask me again set to " + (!b ? "1" : "0"));
                SharedPrefUtils.setShowAlertDialog(getContext(), !b);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_oneday_dialog_message);
        builder.setTitle(R.string.alert_dialog_title)
                .setView(checkBoxView)
                .setPositiveButton(R.string.alert_dialog_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onOnedayDialogPositiveClick(DeleteTaskOnedayDialog.this);
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onOnedayDialogNegativeClick(DeleteTaskOnedayDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
