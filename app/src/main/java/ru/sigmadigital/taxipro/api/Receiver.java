package ru.sigmadigital.taxipro.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.models.AcquiringBank;
import ru.sigmadigital.taxipro.models.Address;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.AuthSession;
import ru.sigmadigital.taxipro.models.City;
import ru.sigmadigital.taxipro.models.Country;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.DriverNews;
import ru.sigmadigital.taxipro.models.DriverNotification;
import ru.sigmadigital.taxipro.models.DriverPosition;
import ru.sigmadigital.taxipro.models.DriverSetting;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.GeoObject;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.PageTotal;
import ru.sigmadigital.taxipro.models.Token;
import ru.sigmadigital.taxipro.models.VehicleGroup;
import ru.sigmadigital.taxipro.models.my.Header;
import ru.sigmadigital.taxipro.models.my.JsonParser;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.models.my.SendedRequest;
import ru.sigmadigital.taxipro.repo.AreaRepository;
import ru.sigmadigital.taxipro.repo.OrdersRepository;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.NotificationHelper;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class Receiver {

    private static Receiver receiver;
    private MutableLiveData<ReceivedResponse> liveData = new MutableLiveData<>();
    private List<Class> listOfClasses;


    public static Receiver getInstance() {
        if (receiver == null) {
            receiver = new Receiver();
            receiver.createListOfClasses();
        }
        return receiver;
    }


    private void createListOfClasses() {
        listOfClasses = new ArrayList<>();
        listOfClasses.add(Token.DriverAppSignedIn.class);
        listOfClasses.add(Token.HasBeenConfirmed.class);
        listOfClasses.add(AuthSession.HasBeenContinued.class);
        listOfClasses.add(Error.class);
        listOfClasses.add(Ok.class);
        listOfClasses.add(PageTotal.class);

        listOfClasses.add(Area.AreaPanel.class);

        listOfClasses.add(Driver.Feed.class);
        listOfClasses.add(Driver.Profile.class);
        listOfClasses.add(Driver.Balance.class);
        listOfClasses.add(Driver.WeekEarn.class);
        listOfClasses.add(Driver.DayEarn.class);
        listOfClasses.add(Driver.Transaction.class);
        listOfClasses.add(Driver.Activity.class);
        listOfClasses.add(Driver.ActivityHistory.class);
        listOfClasses.add(VehicleGroup.DriverRatePeriodsForSale.class);
        listOfClasses.add(Driver.PaidRatePeriod.class);

        listOfClasses.add(GeoObject.Item.class);
        listOfClasses.add(Address.HasBeenRespond.class);

        listOfClasses.add(Order.DriverOrder.class);
        listOfClasses.add(Order.OrderDriverDistance.class);
        listOfClasses.add(Order.HasBeenCompleted.class);

        listOfClasses.add(City.Item.class);
        listOfClasses.add(Country.AuthenticationCountry.class);

        listOfClasses.add(DriverNews.ListItem.class);

        listOfClasses.add(AcquiringBank.HasResponded.class);

        listOfClasses.add(Driver.StateHasBeenModifiedAsync.class);
        listOfClasses.add(DriverNotification.HasBeenSentAsync.class);
        listOfClasses.add(DriverNews.HaveBeenAppearedAsync.class);
        listOfClasses.add(DriverPosition.AreaPanelHasBeenModifiedAsync.class);
        listOfClasses.add(Order.DriverOrderHasBeenModifiedAsync.class);
        listOfClasses.add(Order.HasBeenBecomeUnavailableForDriverAsync.class);
    }


    <T> void parse(String json) {
        List<String> jsonArray = new ArrayList<>(Arrays.asList(json.split("\\n")));

        //parse header
        Header.Response headerResponse = null;
        if (jsonArray.size() > 0) {
            headerResponse = parseHeader(jsonArray.get(0));
            jsonArray.remove(0);
        }

        //parse body
        T data = null;
        if (headerResponse != null && headerResponse.getMessages() != null) {
            List<Header.Response.Message> messages = headerResponse.getMessages();
            data = parseBody(messages, jsonArray);
        }

        //get senderfragment
        String senderFragment = null;
        String senderRequest = null;
        String bodyRequest = null;
        String reqName = null;
        if (headerResponse != null) {
            SendedRequest sendedRequest = getSendedRequest(headerResponse.getId());
            if (sendedRequest != null) {
                senderFragment = sendedRequest.getSenderFragment();
                senderRequest = sendedRequest.getSenderRequest();
                bodyRequest = sendedRequest.getBody();
                reqName = sendedRequest.getReqName();
            }
        }

        if (data != null && data.getClass().equals(Error.class)){
            Handler handler = new Handler(Looper.getMainLooper());
            T finalData = data;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getAppContext(), ((Error) finalData).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        //if осуществите вход
        if (data != null && data.getClass().equals(Error.class) && ((Error) data).getStatusCode() == 401) {
            Log.e("Receiver", "catch error 401");
            if(reqName != null && reqName.equals("authSession.continueSession")){
//                Sender.getInstance().send("token.signOut", new Token.SignOut().toJson(), this.getClass().getSimpleName());
                SettingsHelper.clearToken();
                MainActivity.startSplashActivity();
            } else {
                MainActivity.continueSessionRequest();
                if (reqName != null && bodyRequest != null && senderFragment != null) {
                    Sender.getInstance().send(reqName, bodyRequest, senderFragment);
                }
            }
            return;
        }


        final ReceivedResponse receivedResponse = new ReceivedResponse(senderFragment, senderRequest, data);

        //ассинхронные
        if (data != null && data.getClass().getName().contains("Async")) {
            Log.e("Receiver", "catch Async");
            asyncFilter(receivedResponse);
            return;
        }

        //put to live data
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                setLiveData(receivedResponse);
            }
        });
    }



    //parse
    private Header.Response parseHeader(String json) {
        return JsonParser.fromJson(json, Header.Response.class);
    }

    private <T, G> T parseBody(List<Header.Response.Message> messages, List<String> jsonArray) {
        List<G> data = new ArrayList<>();

        int currentJson = 0;
        for (Header.Response.Message message : messages) { //перебор типов
            for (int c = 0; c < message.getCount(); c++) { //перебор count типа
                data.add((G) parseData(message.getType(), jsonArray.get(currentJson)));
                currentJson++;
            }
        }

        //return list if count of data > 1, else return object
        if (data.size() == 1) {
            return (T) data.get(0);
        } else {
            return (T) data;
        }
    }

    private <T> T parseData(String type, String json) {
        Class currentClass = getClass(type);
        if (currentClass != null) {
            T data = (T) JsonParser.fromJson(json, currentClass);
            //Log.e("parseData", data.getClass().getCanonicalName());
            return data;
        }

        Log.e("parseData", "null");
        return null;
    }

    private Class getClass(String type) {
        for (Class currentClass : listOfClasses) {

            String className = currentClass.getCanonicalName().toLowerCase();
            String cropClassName;
            if (type.contains(".")) {
                cropClassName = currentClass.getCanonicalName().toLowerCase().substring(className.lastIndexOf(".", className.lastIndexOf(".") - 1) + 1);
            } else {
                cropClassName = currentClass.getCanonicalName().toLowerCase().substring(className.lastIndexOf(".") + 1);
            }

            //Log.e("compare, type+ "  -  " +cropClassName);

            if (cropClassName.equals(type.toLowerCase())) {
                //Log.e("getClass", cropClassName);
                return currentClass;
            }
        }

        Log.e("getClass", "null");
        return null;
    }

    private SendedRequest getSendedRequest(int id) {
        for (SendedRequest sendedRequest : Sender.getInstance().getSendedRequests()) {
            if (id == sendedRequest.getRequestId()) {
                Sender.getInstance().getSendedRequests().remove(sendedRequest);
                //Log.e("getSenderFragment", sendedRequest.getSenderFragment());
                return sendedRequest;
            }
        }
        Log.e("getSendedRequest", "null");
        return null;
    }



    //live data
    public MutableLiveData<ReceivedResponse> getLiveData() {
        return liveData;
    }

    public void clearLiveData(){
        liveData.setValue(null);
    }

    private void setLiveData(ReceivedResponse receivedResponse) {
        liveData.setValue(receivedResponse);
    }



    //async
    private void asyncFilter(ReceivedResponse response) {

        if (response.getData().getClass().equals(Driver.StateHasBeenModifiedAsync.class)) {
            Log.e("StateHasBeenModified", "StateHasBeenModifiedAsync");

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ProfileRepository.getInstance().setStateFromAsync(((Driver.StateHasBeenModifiedAsync) response.getData()));
                }
            });

            return;
        }

        if (response.getData().getClass().equals(DriverPosition.AreaPanelHasBeenModifiedAsync.class)) {
            Log.e("DriverPosition", "AreaPanelHasBeenModifiedAsync");
            DriverPosition.AreaPanelHasBeenModifiedAsync data = (DriverPosition.AreaPanelHasBeenModifiedAsync) response.getData();
            if (!data.getPanels().isEmpty()) {
                OrdersRepository.getInstance().setAreasList(data.getPanels());
            }
        }

        if (response.getData().getClass().equals(DriverNotification.HasBeenSentAsync.class)) {
            Log.e("DriverNotification", "HasBeenSentAsync");

            Sender.getInstance().send("driverNotification.deliver", new DriverNotification.Deliver(((DriverNotification.HasBeenSentAsync)response.getData()).getNotification().getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());

            NotificationHelper.createDriverNotification(((DriverNotification.HasBeenSentAsync) response.getData()).getNotification().getOrder());

            //Sender.getInstance().send("driverNotification.respond", new DriverNotification.Respond(((DriverNotification.HasBeenSentAsync)response.getData()).getNotification().getId(),DriverNotification.ResponseType.Timeout, new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());

        }

        if (response.getData().getClass().equals(DriverNews.HaveBeenAppearedAsync.class)) {
            Log.e("DriverNews", "HaveBeenAppearedAsync");
            NotificationHelper.createSimpleNotification("DriverNews.HaveBeenAppearedAsync");
        }

        if (response.getData().getClass().equals(Order.DriverOrderHasBeenModifiedAsync.class)) {
            Log.e("Order", "DriverOrderHasBeenModifiedAsync");
            NotificationHelper.createSimpleNotification("Order.DriverOrderHasBeenModifiedAsync");
        }

        if (response.getData().getClass().equals(Order.HasBeenBecomeUnavailableForDriverAsync.class)) {
            Log.e("Order", "HasBeenBecomeUnavailableForDriverAsync");
            NotificationHelper.createSimpleNotification("Order.HasBeenBecomeUnavailableForDriverAsync");
        }
    }


}
