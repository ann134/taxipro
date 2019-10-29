package ru.sigmadigital.taxipro.fragments.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.AdresssesAdapter;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Address;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.GeoAddress;
import ru.sigmadigital.taxipro.models.GeoObject;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.ProfileRepository;

public class HomeAddressFragment extends BaseNavigatorFragment implements View.OnClickListener, AdresssesAdapter.OnAddressClickListener {

    private EditText editText;
    private RecyclerView recyclerView;

    private boolean shouldSendGoHome = false;
    private GeoObject.Item geoObjectItem;


    public static HomeAddressFragment newInstance() {
        return new HomeAddressFragment();
    }

    public static HomeAddressFragment newInstance(boolean shouldSendGoHome) {
        Bundle args = new Bundle();
        args.putBoolean("shouldSendGoHome", shouldSendGoHome);
        HomeAddressFragment fragment = new HomeAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("shouldSendGoHome")) {
                shouldSendGoHome = getArguments().getBoolean("shouldSendGoHome");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_address, container, false);

        //tittle bar
        TextView title = v.findViewById(R.id.tv_title);
        title.setText("домашний адрес");
        v.findViewById(R.id.imv_back_button).setOnClickListener(this);
        v.findViewById(R.id.imv_locate).setVisibility(View.VISIBLE);
        v.findViewById(R.id.imv_locate).setOnClickListener(this);


        //edit text
        editText = v.findViewById(R.id.ed_query);
        addTextListener();


        //recycler
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));


        subscribeToResponces();

        return v;
    }

    private void addTextListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendGeoItem(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendGeoItem(String term) {
        GeoObject.ItemFilter itemFilter = new GeoObject.ItemFilter(term, new Geo(MyLocationListener.getInstance().getLocation()));
        Sender.getInstance().send("geoObject.item", itemFilter.toJson(), this.getClass().getSimpleName());
    }


    //requests
    private void subscribeToResponces() {

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onDataReceived(responce);
                }
            }
        });
    }

    private <T> void onDataReceived(ReceivedResponse responce) {
        if ( responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null ) {

            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                List<GeoObject.Item> geoItems = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof GeoObject.Item) {
                        geoItems.add((GeoObject.Item) item);
                    }
                }

                Log.e("ArrayList", "ArrayList");
                initRecycler(geoItems);

                return;
            }

            if (responce.getData().getClass().equals(Address.HasBeenRespond.class)) {

                GeoAddress geoAddress = new GeoAddress(geoObjectItem.getGeo(), ((Address.HasBeenRespond) responce.getData()).getAddressId());
                ProfileRepository.getInstance().setGeoAddressHome(geoAddress);


                if (shouldSendGoHome) {
                    Driver.GoHome goHome = new Driver.GoHome(new Geo(MyLocationListener.getInstance().getLocation()), ProfileRepository.getInstance().getGeoAddressHome());
                    Sender.getInstance().send("driver.goHome", goHome.toJson(), MainActionBarFragment.class.getSimpleName() + " " + MainActionBarFragment.goHomeSender);
                }

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }

                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                return;
            }

            Log.e("unknownDataHomeAdr", responce.getData().getClass().toString());
        }
    }

    private void initRecycler(List<GeoObject.Item> geoItems) {
        AdresssesAdapter adapter = new AdresssesAdapter(this, geoItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onAddressClick(GeoObject.Item geoObj) {
        geoObjectItem = geoObj;
        Sender.getInstance().send("address.getOrAdd", new Address.GetOrAdd(geoObj.getId()).toJson(), this.getClass().getSimpleName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back_button: {
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            }
            case R.id.imv_locate: {
                replaceCurrentFragmentWith(getFragmentManager(),
                        getParentContainer(this.getView()),
                        HomeAddressMapFragment.newInstance(),
                        true);
                break;
            }
        }
    }

}
