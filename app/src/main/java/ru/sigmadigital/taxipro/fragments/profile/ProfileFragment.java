package ru.sigmadigital.taxipro.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfo;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogTransparentButton;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.Token;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.SettingsHelper;
import ru.sigmadigital.taxipro.view.StatisticView;
import ru.sigmadigital.taxipro.view.TextWithIconView;

public class ProfileFragment extends BaseNavigatorFragment implements View.OnClickListener, DialogTransparentButton.DialogListener, DialogInfo.DialogListener {

    private ActionBarListener actionBarListener;

    private StatisticView callName;
    private StatisticView car;
    private StatisticView phone;
    private Button sosButton;

    private Driver.Profile profile;

    public static ProfileFragment newInstance(ActionBarListener listener) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setActionBarListener(listener);
        return fragment;
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_profile, null);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("профиль");
            actionBarListener.onShowBackButton(false);
        }

        callName = v.findViewById(R.id.call_name);
        callName.setTitle("позывной");
        callName.setVisibleArrow(false);
        //callName.setOnClickListener(this);

        car = v.findViewById(R.id.car);
        car.setTitle("автомобиль");
        car.setVisibleArrow(false);
        //car.setOnClickListener(this);

        phone = v.findViewById(R.id.phone);
        phone.setTitle("контактный телефон");
        phone.setOnClickListener(this);

        /*TextWithIconView messages = v.findViewById(R.id.messages);
        messages.setTitle("Сообщения");
        messages.setIconCount("3");
        messages.setOnClickListener(this);*/

        TextWithIconView news = v.findViewById(R.id.news);
        news.setTitle("Новости");
        news.setArrow();
        //news.setIconCount("5");
        news.setOnClickListener(this);

       /* TextWithIconView chats = v.findViewById(R.id.chats);
        chats.setTitle("Чаты");
        chats.setIconCount("6");
        chats.setOnClickListener(this);*/

        TextWithIconView help = v.findViewById(R.id.help);
        help.setTitle("справка");
        help.setArrow();
        help.setOnClickListener(this);

        TextWithIconView instruction = v.findViewById(R.id.instruction);
        instruction.setTitle("инструкция");
        instruction.setArrow();
        instruction.setOnClickListener(this);

        TextWithIconView userAgreement = v.findViewById(R.id.user_agreement);
        userAgreement.setTitle("пользовательское соглашение");
        userAgreement.setArrow();
        userAgreement.setOnClickListener(this);


        sosButton = v.findViewById(R.id.sos);
        sosButton.setOnClickListener(this);
        v.findViewById(R.id.logout).setOnClickListener(this);

        subscribeToLiveData();
        subscribeToProfile();

        return v;
    }


    private void subscribeToProfile() {
        final LiveData<Driver.Profile> liveData = ProfileRepository.getInstance().getProfileLiveData();
        liveData.observe(this, new Observer<Driver.Profile>() {
            @Override
            public void onChanged(Driver.Profile profile) {
                fillData(profile);

            }
        });
    }


    private void fillData(Driver.Profile profile) {
        this.profile = profile;

        callName.setCount(profile.getCallsign());
        callName.setText(profile.getFullName());

        car.setCount(profile.getVehicleName());

        phone.setCount(profile.getPhone());

        if (profile.isSos()) {
            sosButton.setText("отменить sos");
        } else {
            sosButton.setText("sos");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.call_name: {
                showFragment(DocumentsFragment.newInstance(actionBarListener, DocumentsFragment.TYPE_DOCS));
                break;
            }
            case R.id.car: {
                showFragment(DocumentsFragment.newInstance(actionBarListener, DocumentsFragment.TYPE_CAR));
                break;
            }*/
            case R.id.phone: {
                showFragment(ModifyPhone.newInstance(actionBarListener));
                break;
            }
            /*case R.id.messages: {
                //showFragment(ActivityFragment.newInstance(actionBarListener));
                //NotificationHelper.createNot();
                break;
            }*/
            case R.id.news: {
                showFragment(NewsListFragment.newInstance(actionBarListener));
                break;
            }
            /*case R.id.chats: {
                showDialogRequestSended();
                break;
            }*/
            case R.id.help: {
                showFragment(HelpFragment.newInstance(actionBarListener));
                break;
            }
            case R.id.instruction: {
                showFragment(ReferenceFragment.newInstance(actionBarListener));
                break;
            }
            case R.id.user_agreement: {
                //showDialogChangeCar();

                break;
            }
            case R.id.sos: {
                if (profile != null) {
                    if (profile.isSos()) {
                        cancelSos();
                    } else {
                        sendSos();
                    }
                }
                break;
            }
            case R.id.logout: {
                signOut();
                break;
            }
        }
    }

    private void showDialogChangeCar() {
        DialogTransparentButton dialog = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                "Внимание!",
                "При смене автомобиля Вы не сможете получать новые заказы до тех пор, пока оператор не подтвердит изменение транспортного средства",
                getString(R.string.cancell),
                "изМЕНИТЬ");
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showDialogRequestSended() {
        DialogInfo dialog = DialogInfo.newInstance(this,
                R.drawable.ic_done,
                "Заявка отправлена!",
                "После утверждения вы получите уведомление и сможете принимать заказы.",
                getString(R.string.ok));
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onActionClick(int action) {

    }

    private void showFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                fragment,
                true);
    }


    private void sendSos() {
        ProfileRepository.getInstance().setSos(true);
        Sender.getInstance().send("driver.sendSos", new Driver.SendSos().toJson(), this.getClass().getSimpleName());
    }

    private void cancelSos() {
        ProfileRepository.getInstance().setSos(false);
        Sender.getInstance().send("driver.cancelSos", new Driver.CancelSos(new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }

    private void signOut() {
        Sender.getInstance().send("token.signOut", new Token.SignOut().toJson(), this.getClass().getSimpleName());
        SettingsHelper.clearToken();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).startSplashActivity();
        }
    }


    private void subscribeToLiveData() {
       /* City.ItemFilter cityItemFilter = new City.ItemFilter(new Geo(59.931107,30.437443), 0, 999);
        Sender.getInstance().send("city.item", cityItemFilter.toJson(), this.getClass().getSimpleName());*/

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

    private void onDataReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            /*if (responce.getData().getClass().equals(City.Item.class)) {
                Log.e("City", "City");

                return;
            }
*/
            if (responce.getData().getClass().equals(Ok.class)) {
                Log.e("Ok", "ok");
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                return;
            }

            Log.e("unknownDataProfileFr", responce.getData().getClass().toString());
        }
    }

}
