package ru.sigmadigital.taxipro.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.api.http.SendImageTask;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.DriverDocFile;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.models.http.ImageResponse;
import ru.sigmadigital.taxipro.repo.ProfileRepository;

public class DocumentsFragment extends BaseNavigatorFragment implements View.OnClickListener, SendImageTask.OnGetImageResponseListener {

    public static int TYPE_DOCS = 9;
    public static int TYPE_CAR = 7;

    private int type;
    private ActionBarListener actionBarListener;

    private ArrayMap<Integer, File> files = new ArrayMap<>();


    private TextView title;
    private TextView textCard1;
    private TextView textCard2;
    private TextView textCard3;
    private TextView textCard4;
    private Button buttonNext;

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;

    private ImageView clear1;
    private ImageView clear2;
    private ImageView clear3;
    private ImageView clear4;


    public static DocumentsFragment newInstance(ActionBarListener listener, int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        DocumentsFragment fragment = new DocumentsFragment();
        fragment.setArguments(args);
        fragment.setActionBarListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("type")) {
                type = getArguments().getInt("type");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("type")) {
                type = savedInstanceState.getInt("type");
            }
        }
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_registration_fotos, null);

        if (actionBarListener != null) {
            actionBarListener.onShowBackButton(true);
        }


        title = view.findViewById(R.id.tv_title);
        textCard1 = view.findViewById(R.id.tv_card1);
        textCard2 = view.findViewById(R.id.tv_card2);
        textCard3 = view.findViewById(R.id.tv_card3);
        textCard4 = view.findViewById(R.id.tv_card4);
        buttonNext = view.findViewById(R.id.btn_next);

        cardView1 = view.findViewById(R.id.cv_1);
        cardView2 = view.findViewById(R.id.cv_2);
        cardView3 = view.findViewById(R.id.cv_3);
        cardView4 = view.findViewById(R.id.cv_4);

        photo1 = view.findViewById(R.id.img_1);
        photo2 = view.findViewById(R.id.img_2);
        photo3 = view.findViewById(R.id.img_3);
        photo4 = view.findViewById(R.id.img_4);

        clear1 = view.findViewById(R.id.imv_cancel_1);
        clear2 = view.findViewById(R.id.imv_cancel_2);
        clear3 = view.findViewById(R.id.imv_cancel_3);
        clear4 = view.findViewById(R.id.imv_cancel_4);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);

        clear1.setOnClickListener(this);
        clear2.setOnClickListener(this);
        clear3.setOnClickListener(this);
        clear4.setOnClickListener(this);

        buttonNext.setOnClickListener(this);

        setText();

        return view;
    }

    private void setText() {

        if (type == TYPE_DOCS) {
            if (actionBarListener != null) {
                actionBarListener.onSetTitle("документы");
            }
            title.setText(getString(R.string.reg_step1_title));
            textCard1.setText(getString(R.string.reg_ts_front));
            textCard2.setText(getString(R.string.reg_ts_back));
            textCard3.setText(getString(R.string.driver_license_front));
            textCard4.setText(getString(R.string.driver_license_back));
        }

        if (type == TYPE_CAR) {
            if (actionBarListener != null) {
                actionBarListener.onSetTitle("автомобиль");
            }
            title.setText(getString(R.string.reg_step2_title));
            textCard1.setText(getString(R.string.car_front));
            textCard2.setText(getString(R.string.car_back));
            textCard3.setText(getString(R.string.interior_of_car));
            textCard4.setText(getString(R.string.extra_info));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_1:
                setImage(photo1, clear1);
                break;
            case R.id.cv_2:
                setImage(photo2, clear2);
                break;
            case R.id.cv_3:
                setImage(photo3, clear3);
                break;
            case R.id.cv_4:
                setImage(photo4, clear4);
                break;

            case R.id.imv_cancel_1:
                clearImage(photo1, clear1);
                break;
            case R.id.imv_cancel_2:
                clearImage(photo2, clear2);
                break;
            case R.id.imv_cancel_3:
                clearImage(photo3, clear3);
                break;
            case R.id.imv_cancel_4:
                clearImage(photo4, clear4);
                break;

            case R.id.btn_next:
                new SendImageTask(this, new ArrayList<>(files.values())).execute();
                break;
        }
    }

    private void setImage(final ImageView photo, final ImageView clear) {
        PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult pickResult) {
                photo.setImageBitmap(pickResult.getBitmap());
                photo.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);

                if (photo.getId() == R.id.img_1) {
                    files.put(1, new File(pickResult.getPath()));
                }

                if (photo.getId() == R.id.img_2) {
                    files.put(2, new File(pickResult.getPath()));
                }

                if (photo.getId() == R.id.img_3) {
                    files.put(3, new File(pickResult.getPath()));
                }

                if (photo.getId() == R.id.img_4) {
                    files.put(4, new File(pickResult.getPath()));
                }
            }
        }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {

            }
        }).show(getFragmentManager());

    }

    private void clearImage(ImageView photo, ImageView clear) {
        photo.setImageResource(android.R.color.transparent);
        photo.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
    }

    @Override
    public void onGetItemsImageResponse(ImageResponse imageResponse) {
        if (imageResponse != null) {
            DriverDocFile.DriverDocFileValue driverDocFileValue = new DriverDocFile.DriverDocFileValue();
            for (int i = 0; i < imageResponse.getIds().length; i++) {
                DriverDocFile.DocFile docFile = new DriverDocFile.DocFile(imageResponse.getIds()[i], getType(files.keyAt(i)));
                driverDocFileValue.addFile(docFile);
            }

            Sender.getInstance().send("driverDocFile.uploadDocFiles", new DriverDocFile.UploadDocFiles(ProfileRepository.getInstance().getProfile().getCityId(), driverDocFileValue).toJson(), this.getClass().getSimpleName());

            LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
            liveData.observe(this, new Observer<ReceivedResponse>() {
                @Override
                public void onChanged(@Nullable ReceivedResponse responce) {
                    if (responce != null) {
                        onUploadReceived(responce);
                    }
                }
            });
        }
    }


    private <T> void onUploadReceived(ReceivedResponse responce) {
        if ( responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(Ok.class)) {
                Log.e("Ok", "Ok");

                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");


                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }


    private int getType(int key) {
        if (type == TYPE_DOCS) {
            if (key == 1) {
                return DriverDocFile.DocType.vehicleDocumentTopSide;
            }
            if (key == 2) {
                return DriverDocFile.DocType.vehicleDocumentBackSide;
            }
            if (key == 3) {
                return DriverDocFile.DocType.driverLicense;
            }
            if (key == 4) {
                return DriverDocFile.DocType.driverLicenseBackSide;
            }
        }

        if (type == TYPE_CAR) {
            if (key == 1) {
                return DriverDocFile.DocType.vehicleFront;
            }
            if (key == 2) {
                return DriverDocFile.DocType.vehicleBack;
            }
            if (key == 3) {
                return DriverDocFile.DocType.VehicleInterrior;
            }
            if (key == 4) {
                //не сделано на сервере
                // дополнительная фотка
                return 7;
            }
        }
        return -1;
    }

}
