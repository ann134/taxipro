package ru.sigmadigital.taxipro.fragments.training;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.sigmadigital.taxipro.R;


public class TrainingFragment extends Fragment implements View.OnClickListener {

    private OnStartClickListener listener;

    private int type;
    private int image;
    private String tittle;
    private String text;


    public interface OnStartClickListener {
        void onStartClick();
    }

    public static TrainingFragment newInstance(int type, OnStartClickListener listener, int image, String title, String text) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("image", image);
        args.putString("title", title);
        args.putString("text", text);
        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    private void setListener(OnStartClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("type")) {
                type = getArguments().getInt("type");
            }
            if (getArguments().containsKey("image")) {
                image = getArguments().getInt("image");
            }
            if (getArguments().containsKey("title")) {
                tittle = getArguments().getString("title");
            }
            if (getArguments().containsKey("text")) {
                text = getArguments().getString("text");
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (type == 1) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_training_first, null);
            return view;
        }

        if (type == 4) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_training_last, null);
            view.findViewById(R.id.button).setOnClickListener(this);
            return view;
        }


        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_training, null);

        ImageView pic = view.findViewById(R.id.image);
        TextView tittle = view.findViewById(R.id.title);
        TextView text = view.findViewById(R.id.text);
        if (getContext() != null) {
            pic.setImageDrawable(getContext().getDrawable(image));
        }
        tittle.setText(this.tittle);
        text.setText(this.text);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onStartClick();
        }
    }
}
