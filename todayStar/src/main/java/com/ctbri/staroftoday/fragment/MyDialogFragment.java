package com.ctbri.staroftoday.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by Murphy on 16/8/30.
 */
public class MyDialogFragment extends DialogFragment {
    public static final int CHOSE_PIC = 0;
    public static final int LOADING = 1;
    public static final int DATEPICKER = 2;

    public static MyDialogFragment choosePicInstance(String name, int style) {
        MyDialogFragment f = new MyDialogFragment();
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putInt("style", style);
        f.setArguments(b);
        return f;
    }

    public static MyDialogFragment loadingInstance() {
        MyDialogFragment f = new MyDialogFragment();
        Bundle b = new Bundle();
        b.putInt("style", LOADING);
        f.setArguments(b);
        return f;
    }

    public static MyDialogFragment datePickerInstance() {
        MyDialogFragment f = new MyDialogFragment();
        Bundle b = new Bundle();
        b.putInt("style", DATEPICKER);
        f.setArguments(b);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        switch (getArguments().getInt("style")) {
            case CHOSE_PIC:
                //return PhotoUtil.pickPhoto(getActivity());
            case LOADING:
                return super.onCreateDialog(savedInstanceState);
            case DATEPICKER:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), mYear, mMonth,
                        mDay);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (getArguments().getInt("style")) {
            case LOADING:
                //return inflater.inflate(R.layout.loading_layout, container, false);
            default:
                return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
