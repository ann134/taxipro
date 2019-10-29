package ru.sigmadigital.taxipro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.City;

public class DialogCityAdapter extends RecyclerView.Adapter<DialogCityAdapter.CityHolder> {
    private List<String> citiesList;
    private Integer checkPosition;
    private String pickedCity;

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, null);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityHolder holder, final int position) {

        String item = citiesList.get(position);

        holder.setIsRecyclable(false);

        if(checkPosition != null && checkPosition == position){
            holder.radioButton.setChecked(true);
            checkPosition = null;
        }

        holder.city.setText(item);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPosition = position;
                pickedCity = item;
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }


    public class CityHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView city;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_btn);
            city = itemView.findViewById(R.id.city_name);
        }
    }

    public void setCitiesList(List<String> citiesList) {
        this.citiesList = citiesList;
    }




    public String getPickedCity(){
        return pickedCity;
    }
}
