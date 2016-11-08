package com.ostap_kozak.progress.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.ostap_kozak.progress.R;

/**
 * Created by ostapkozak on 12/09/2016.
 */
public class CountdownTimerDialogFragment extends DialogFragment implements View.OnClickListener{
    NumberPicker numberPicker_hours;
    NumberPicker numberPicker_minutes;
    NumberPicker numberPicker_seconds;
    Button clearNumberPickerButton;


    public CountdownTimerDialogFragment() {
        // Public constructor is required for DialogFragment
    }

    public static CountdownTimerDialogFragment newInstance() {
        CountdownTimerDialogFragment dialog = new CountdownTimerDialogFragment();
        return dialog;
    }

    public interface CountDownTimerListener {
        void onCountDownTimerFinish(int hours, int minutes, int seconds);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.countdowntimer_dialog, null);
        initializeNumberPicker(view);

        clearNumberPickerButton = (Button) view.findViewById(R.id.clearNumberPickerButton);
        clearNumberPickerButton.setOnClickListener(this);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // on success
                        CountDownTimerListener listener = (CountDownTimerListener) getTargetFragment();
                        listener.onCountDownTimerFinish(numberPicker_hours.getValue(), numberPicker_minutes.getValue(),
                                numberPicker_seconds.getValue());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }



    private void initializeNumberPicker(View view) {
        numberPicker_hours = (NumberPicker) view.findViewById(R.id.hours_numberPicker);
        numberPicker_minutes = (NumberPicker) view.findViewById(R.id.minute_numberPicker);
        numberPicker_seconds = (NumberPicker) view.findViewById(R.id.seconds_numberPicker);

        numberPicker_hours.setMinValue(0);
        numberPicker_hours.setMaxValue(23);
        numberPicker_minutes.setMinValue(0);
        numberPicker_minutes.setMaxValue(59);
        numberPicker_seconds.setMinValue(0);
        numberPicker_seconds.setMaxValue(59);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearNumberPickerButton:
                numberPicker_hours.setValue(0);
                numberPicker_minutes.setValue(0);
                numberPicker_seconds.setValue(0);
        }
    }



}
