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
import ru.sigmadigital.taxipro.models.GeoObject;

public class AdresssesAdapter extends RecyclerView.Adapter<AdresssesAdapter.AddressHolder> {

    OnAddressClickListener listener;
    private List<GeoObject.Item> geoItems;

    public AdresssesAdapter(OnAddressClickListener listener, List<GeoObject.Item> geoItems) {
        this.geoItems = geoItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_favorite, null);
        return new AddressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, final int position) {
        final GeoObject.Item item = geoItems.get(position);

        holder.address.setText(item.getTitle());
        holder.area.setText(item.getNote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddressClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return geoItems.size();
    }





    class AddressHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView area;
        ImageView star;

        AddressHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            area = itemView.findViewById(R.id.area);
            star = itemView.findViewById(R.id.star);
        }
    }


    public interface OnAddressClickListener {
        void onAddressClick(GeoObject.Item geoObj);
    }
}