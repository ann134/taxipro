package ru.sigmadigital.taxipro.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.sigmadigital.taxipro.R;

public class DialogStatus extends DialogFragment implements View.OnClickListener {

    private StatusDialogListener listener;

    public static DialogStatus newInstance(StatusDialogListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        DialogStatus fragment = new DialogStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (StatusDialogListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (StatusDialogListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_status, null);

        view.findViewById(R.id.status_on).setOnClickListener(this);
        view.findViewById(R.id.status_home).setOnClickListener(this);
        view.findViewById(R.id.status_off).setOnClickListener(this);
        view.findViewById(R.id.cansel).setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        Dialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_on: {
                if (listener != null) {
                    listener.onStatusSelected(100);
                }
                dismiss();
                break;
            }
            case R.id.status_home: {
                if (listener != null) {
                    listener.onStatusSelected(400);
                }
                dismiss();
                break;
            }
            case R.id.status_off: {
                if (listener != null) {
                    listener.onStatusSelected(0);
                }
                dismiss();
                break;
            }
            case R.id.cansel: {
                dismiss();
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listener", listener);
    }

    public interface StatusDialogListener extends Serializable {
        void onStatusSelected(int status);
    }
}