package ru.sigmadigital.taxipro.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.DialogCityAdapter;
import ru.sigmadigital.taxipro.models.City;

public class DialogChoose extends DialogFragment implements View.OnClickListener {

    private TextView title;
    private RecyclerView recyclerView;
    private Button accept;
    private Button cancel;
    private DialogCityAdapter adapter;

    private OnAcceptClickListener listener;

    private List<String> dataList;



    public static DialogChoose newInstance(OnAcceptClickListener listener, String title) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        args.putString("title", title);
        DialogChoose dialog = new DialogChoose();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("listener")) {
                listener = (OnAcceptClickListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("listener")) {
                listener = (OnAcceptClickListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose, null);

        title = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.rv);
        accept = view.findViewById(R.id.bnt_accept);
        cancel = view.findViewById(R.id.btn_cancel);

        if(getArguments()!= null && getArguments().containsKey("title")){
            title.setText(getArguments().getString("title"));
        }

        adapter = new DialogCityAdapter();
        List<String> list = new ArrayList<>();
        if(dataList != null){
            list = dataList;
        }
        adapter.setCitiesList(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_accept:
                if (listener != null){
                    if (adapter.getPickedCity() != null){
                        listener.OnAcceptClick(adapter.getPickedCity());
                    } else {
                        listener.OnAcceptClick(dataList.get(0));
                    }
                }
                dismiss();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("listener", listener);
    }



    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public interface OnAcceptClickListener extends Serializable {
        void OnAcceptClick(String cityName);
    }

}
