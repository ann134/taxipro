package ru.sigmadigital.taxipro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.setIsRecyclable(false);
        Random random = new Random();
        int x =random.nextInt(2);
        if(x == 1){
            holder.indicator.setImageDrawable(App.getAppContext().getDrawable(R.drawable.dot_green));
            holder.indicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MessageHolder extends RecyclerView.ViewHolder{
        ImageView indicator;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            indicator = itemView.findViewById(R.id.img_indicator);
        }
    }
}
