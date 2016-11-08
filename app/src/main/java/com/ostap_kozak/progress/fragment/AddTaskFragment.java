package com.ostap_kozak.progress.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.util.SpinnerHelper;

/**
 * Created by ostapkozak on 13/09/2016.
 */
public class AddTaskFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, CountdownTimerDialogFragment.CountDownTimerListener {
    private SpinnerHelper time_spinner;
    private EditText taskNameEditText;
    private int hours, minutes, seconds;

    public AddTaskFragment() {}

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task_layout, null);

        time_spinner = new SpinnerHelper(view.findViewById(R.id.time_chooser_spinner));
        initializeSpinner();

        taskNameEditText = (EditText) view.findViewById(R.id.task_name_editbox);
        taskNameEditText.requestFocus();
        showSoftKeyboard(taskNameEditText);

        builder.setTitle("Create new Task")
                .setView(view)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // on success passes data to the MainActivity
                        hideSotKeyboard(taskNameEditText);
                        AddActivityDialogListener listener = (AddActivityDialogListener) getActivity();
                        listener.onFinishAddTask(taskNameEditText.getText().toString(),
                                hours, minutes, seconds);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideSotKeyboard(taskNameEditText);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public void initializeSpinner() {

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.time_spinner_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        time_spinner.setAdapter(adapter);
        time_spinner.setOnItemSelectedListener(this);
    }
    /*
        Listens to item selected in time_spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        if (position == 1) {
            hideSotKeyboard(taskNameEditText);
            FragmentManager fm = getFragmentManager();
            CountdownTimerDialogFragment dialog = CountdownTimerDialogFragment.newInstance();
            dialog.setTargetFragment(AddTaskFragment.this, 300);
            dialog.setRetainInstance(true);
            dialog.show(fm, "TAG");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCountDownTimerFinish(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface AddActivityDialogListener {
        // This method will be implemented by the Activity
        void onFinishAddTask(String taskName, int hour, int minutes, int seconds);
    }


    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    public void hideSotKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}







