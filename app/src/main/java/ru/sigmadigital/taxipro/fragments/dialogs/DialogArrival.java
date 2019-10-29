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

public class DialogArrival extends DialogFragment implements View.OnClickListener {

    private ArrivalDialogListener listener;

    public static DialogArrival newInstance(ArrivalDialogListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        DialogArrival fragment = new DialogArrival();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (ArrivalDialogListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (ArrivalDialogListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_arrival, null);

        view.findViewById(R.id.min5).setOnClickListener(this);
        view.findViewById(R.id.min10).setOnClickListener(this);
        view.findViewById(R.id.min15).setOnClickListener(this);
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
            case R.id.min5: {
                if (listener != null) {
                    listener.onArrivalSelected(5);
                }
                dismiss();
                break;
            }
            case R.id.min10: {
                if (listener != null) {
                    listener.onArrivalSelected(10);
                }
                dismiss();
                break;
            }
            case R.id.min15: {
                if (listener != null) {
                    listener.onArrivalSelected(15);
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

    public interface ArrivalDialogListener extends Serializable {
        void onArrivalSelected(int time);
    }
}