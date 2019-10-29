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

public class DialogNavigator extends DialogFragment implements View.OnClickListener {

    private NavigatorDialogListener listener;

    public static DialogNavigator newInstance(NavigatorDialogListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        DialogNavigator fragment = new DialogNavigator();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (NavigatorDialogListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (NavigatorDialogListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_navigator, null);

        view.findViewById(R.id.standart).setOnClickListener(this);
        view.findViewById(R.id.gis).setOnClickListener(this);
        view.findViewById(R.id.yandex).setOnClickListener(this);
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
            case R.id.standart: {
                if (listener != null) {
                    listener.onNavigatorSelected(0);
                }
                dismiss();
                break;
            }
            case R.id.gis: {
                if (listener != null) {
                    listener.onNavigatorSelected(1);
                }
                dismiss();
                break;
            }
            case R.id.yandex: {
                if (listener != null) {
                    listener.onNavigatorSelected(2);
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

    public interface NavigatorDialogListener extends Serializable {
        void onNavigatorSelected(int status);
    }
}