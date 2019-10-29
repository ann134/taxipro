package ru.sigmadigital.taxipro.api;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.my.Header;
import ru.sigmadigital.taxipro.models.my.SendedRequest;


public class Sender {

    private static Sender sender;

    public static Sender getInstance() {
        if (sender == null) {
            sender = new Sender();

        }
        return sender;
    }


    private int nextRequestId = 0;
    private List<SendedRequest> sendedRequests = new ArrayList<>();
    private List<String> queueRequests = new ArrayList<>();



    public void send(String reqName, String body, String senderFragment) {

        Header.Request headerRequest = makeHeader(reqName);

        StringBuilder requestBilder = new StringBuilder();
        requestBilder.append(headerRequest.toJson());
        requestBilder.append("\n");
        requestBilder.append(body);

        SendedRequest sendedRequest = new SendedRequest(headerRequest.getId(), reqName, senderFragment, body);
        getSendedRequests().add(sendedRequest);

        addToQueueRequests(requestBilder.toString());
        //Log.e("isopen", TaxiProService.isConnectionOpen+"");
        if (!TaxiProService.isConnectionOpen){
            App.showToast(R.string.error_connection_toast);
        }
    }


    public synchronized Header.Request makeHeader(String reqName) {
        Header.Request headerRequest = new Header.Request(nextRequestId, reqName);
        nextRequestId++;
        return headerRequest;
    }






    public synchronized List<String> getQueueRequests() {
        return queueRequests;
    }

    public synchronized void addToQueueRequests(String request) {
        queueRequests.add(request);
    }

    public synchronized void removeFromQueueRequests(int index) {
        queueRequests.remove(index);
    }


    public synchronized List<SendedRequest> getSendedRequests() {
        return sendedRequests;
    }

    public synchronized void addToSendedRequests(SendedRequest request) {
        sendedRequests.add(request);
    }

    public synchronized void removeFromSendedRequests(SendedRequest request) {
        sendedRequests.remove(request);
    }
}
