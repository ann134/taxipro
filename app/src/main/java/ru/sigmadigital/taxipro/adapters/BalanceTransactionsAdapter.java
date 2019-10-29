package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;


public class BalanceTransactionsAdapter extends RecyclerView.Adapter<BalanceTransactionsAdapter.TransactionHolder> {

    private List<Driver.Transaction> list;

    public BalanceTransactionsAdapter(List<Driver.Transaction> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, null);
        return new TransactionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, final int position) {
        Driver.Transaction transaction = list.get(position);

        holder.title.setText(transaction.getTitle());
        holder.value.setText(transaction.getTotal() +"\u20BD");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class TransactionHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        TextView value;
        ImageView arrow;

        TransactionHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            value = itemView.findViewById(R.id.value);
            arrow = itemView.findViewById(R.id.arrow);
        }
    }

}