package ru.sigmadigital.taxipro.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.sigmadigital.taxipro.R;

public class DialogRecalculateOrder extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DialogRecalculateListener listener;


    public static DialogRecalculateOrder newInstance(DialogRecalculateListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        DialogRecalculateOrder dialog = new DialogRecalculateOrder();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (DialogRecalculateListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (DialogRecalculateListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recalculate_order, null);


        //spinner
        Spinner titleSpinner = view.findViewById(R.id.payment_spinner);

        List<String> titles = new ArrayList<>();
        titles.add("Оплата картой");
        titles.add("Оплата наличными");
        titles.add("Корпоративный");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_payment_item, titles);
        adapter.setDropDownViewResource(R.layout.spinner_payment_dropdown);
        titleSpinner.setAdapter(adapter);
        titleSpinner.setOnItemSelectedListener(this);




        view.findViewById(R.id.accept).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        Dialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept: {
                if (listener != null) {
                    listener.onRecalculateClick();
                }
                dismiss();
            }
            case R.id.close: {

                dismiss();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listener", listener);
    }


    public interface DialogRecalculateListener extends Serializable {
        void onRecalculateClick();
    }
}