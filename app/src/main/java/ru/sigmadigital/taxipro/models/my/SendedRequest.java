package ru.sigmadigital.taxipro.models.my;

public class SendedRequest {

    private int requestId;
    private String reqName;
    private String senderFragment;
    private String body;

    public SendedRequest(int requestId, String reqName, String senderFragment, String body) {
        this.requestId = requestId;
        this.reqName = reqName;
        this.senderFragment = senderFragment;
        this.body = body;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getSenderFragment() {
        if (senderFragment.contains(" ")){
            return senderFragment.substring(0, senderFragment.indexOf(" "));
        } else {
            return senderFragment;
        }
    }

    public String getSenderRequest() {
        if (senderFragment.contains(" ")) {
            return senderFragment.substring(senderFragment.indexOf(" ")+1);
        } else {
            return null;
        }
    }

    public String getBody() {
        return body;
    }

    public String getReqName() {
        return reqName;
    }
}
