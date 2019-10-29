package ru.sigmadigital.taxipro.models.my;

import java.util.List;

public class ReceivedResponse<T> {

    private String senderFragment;
    private String senderRequest;
    private T data;

    public ReceivedResponse(String senderFragment, String senderRequest,  T data) {
        this.senderFragment = senderFragment;
        this.senderRequest = senderRequest;
        this.data = data;
    }

    public String getSenderFragment() {
        return senderFragment;
    }

    public String getSenderRequest() {
        return senderRequest;
    }

    public T getData() {
        return data;
    }
}
