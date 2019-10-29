package ru.sigmadigital.taxipro.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Order;


public class ChipCloudFragment extends Fragment {

    private TextView comment;
    private ChipCloud clipCloud;

    public static ChipCloudFragment newInstance() {
        return new ChipCloudFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chip_cloud, container, false);

        comment = v.findViewById(R.id.comment);
        clipCloud = v.findViewById(R.id.chip_cloud);

        subscribeToLiveData();

        return v;
    }

    private void subscribeToLiveData() {
        LiveData<Order.DriverOrder> liveData = OrderFragment.getLiveData();
        liveData.observe(this, new Observer<Order.DriverOrder>() {
            @Override
            public void onChanged(@Nullable Order.DriverOrder value) {
                if (value != null) {
                    setData(value);
                }
            }
        });
    }

    private void setData(Order.DriverOrder order) {
        comment.setText(order.getDescription());

        List<String> adds = new ArrayList<>();
        Order.AdditionalServices additionals = order.getAdditionalServices();

        if (additionals.isAnimal())
            adds.add("животное");
        if (additionals.isLuggage())
            adds.add("багаж");
        if (additionals.isLuggageToDoor())
            adds.add("багаж до двери");
        if (additionals.isNameplate())
            adds.add("табличка");
        if (additionals.isSinistral())
            adds.add("левый руль");
        if (additionals.isTicket())
            adds.add("квитанция");

        switch (additionals.getBabyChair()){
            case Order.BabyChairTypes.Chair:{
                adds.add("детское кресло");
                break;
            }
            case Order.BabyChairTypes.Booster: {
                adds.add("удерживающее устройство");
                break;
            }
            case Order.BabyChairTypes.BoosterX2:{
                adds.add("2 удерживающих устройства");
                break;
            }
            case Order.BabyChairTypes.ChairAndBooster:{
                adds.add("удерживающее устройство");
                adds.add("детское кресло");
                break;
            }
        }

        switch (additionals.getAdv()){
            case Order.AdvType.Logo:{
                adds.add("логотип");
                break;
            }
            case Order.AdvType.Owner: {
                adds.add("частник");
                break;
            }
        }

        switch (additionals.getSmoking()){
            case Order.SmokingType.NonSmoker:{
                adds.add("некурящий водитель");
                break;
            }
            case Order.SmokingType.Smoking: {
                adds.add("курящий водитель");
                break;
            }
        }

        String[] labels = new String[adds.size()];
        adds.toArray(labels);

        clipCloud.addChips(labels);
    }

}
