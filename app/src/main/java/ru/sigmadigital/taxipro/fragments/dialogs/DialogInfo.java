package ru.sigmadigital.taxipro.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ru.sigmadigital.taxipro.R;

public class DialogInfo extends DialogFragment implements View.OnClickListener {

    public static int PRESS_CANCEL = 0;

    private DialogListener listener;

    private int picId;
    private String tittleSt;
    private String textSt;
    private String noSt;


    public static DialogInfo newInstance(DialogListener listener, int image, String title, String text, String no) {
        Bundle args = new Bundle();
        args.putInt("pic", image);
        args.putString("title", title);
        args.putString("text", text);
        args.putString("no", no);
        args.putSerializable("listener", listener);
        DialogInfo fragment = new DialogInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (DialogListener) getArguments().getSerializable("listener");
            }
            if (getArguments().containsKey("pic")) {
                picId = getArguments().getInt("pic");
            }
            if (getArguments().containsKey("title")) {
                tittleSt = getArguments().getString("title");
            }
            if (getArguments().containsKey("text")) {
                textSt = getArguments().getString("text");
            }
            if (getArguments().containsKey("no")) {
                noSt = getArguments().getString("no");
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (DialogListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_info, null);

        ImageView pic = view.findViewById(R.id.pic);
        TextView tittle = view.findViewById(R.id.title);
        TextView text = view.findViewById(R.id.text);
        Button no = view.findViewById(R.id.no);


        pic.setImageDrawable(getContext().getDrawable(picId));
        tittle.setText(tittleSt);
        text.setText(textSt);
        no.setText(noSt);

        no.setOnClickListener(this);


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
            case R.id.no: {
                if (listener != null) {
                    listener.onActionClick(PRESS_CANCEL);
                    dismiss();
                }
                dismiss();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listener", listener);
        /*outState.putInt("pic", picId);
        outState.putString("title", tittleSt);
        outState.putString("text", textSt);
        outState.putString("no", noSt);*/
    }

    public interface DialogListener extends Serializable {
        void onActionClick(int action);
    }
}
