package ru.sigmadigital.taxipro.models;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class AcquiringBank {


    public static class BankCardsOperation {
        public static int GetCards = 0;
        public static int RegisterOrder = 1;
        public static int BindCard = 2;
        public static int GetOrderState = 3;
        public static int UnbindCard = 4;
        public static int PaymentWithBinding = 5;
        public static int RegisterOrderWithPreAuth = 6;
        public static int Deposit = 7;
    }


    public static class HasResponded extends JsonParser implements Serializable {
        int errorCode;
        String errorMessage;
        String formUrl; //URL банка для ввода данных карты
        String orderId; //Идентификатор банковской заявки

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getFormUrl() {
            return formUrl;
        }

        public String getOrderId() {
            return orderId;
        }
    }


    public static class ReplenishDriverAccount extends JsonParser {

        double amount;

        public ReplenishDriverAccount(double amount) {
            this.amount = amount;
        }
    }




}
