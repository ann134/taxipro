package ru.sigmadigital.taxipro.models;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverNotification {

    public static class Notification extends JsonParser implements Serializable{
        int id;
        String title;
        String text;
        int type;
        int deliveryType;//Отчет о доставке
        String sound; //Проигрываемый звук. Звук должен проигрываться циклично пока не закрыли диалог
        int closeAfter; //Если значение этого поля больше нуля, то диалог закроется по прошествии этого времени. В секундах
        String expireAt; //Не нужно реагировать на сообщение после этой даты. date-time
        Order.DriverOrder order;//Идентификатор заказа
        int commandId;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        public int getType() {
            return type;
        }

        public int getDeliveryType() {
            return deliveryType;
        }

        public String getSound() {
            return sound;
        }

        public int getCloseAfter() {
            return closeAfter;
        }

        public String getExpireAt() {
            return expireAt;
        }

        public Order.DriverOrder getOrder() {
            return order;
        }

        public int getCommandId() {
            return commandId;
        }
    }


    public static class HasBeenSentAsync extends JsonParser implements Serializable {
        Notification notification;

        public Notification getNotification() {
            return notification;
        }
    }


    public static class Deliver extends JsonParser { //Сообщить о доставке универсального сообщения
        int id;
        Geo geo;

        public Deliver(int id, Geo geo) {
            this.id = id;
            this.geo = geo;
        }
    }


    public static class Respond extends JsonParser { //СОтветветить на универсальное сообщение
        int id;
        int responseType;
        Geo geo;

        public Respond(int id, int responseType, Geo geo) {
            this.id = id;
            this.responseType = responseType;
            this.geo = geo;
        }
    }





    public static class Type {
        public static int Offer = 0; //Предложение заказа
        public static int ClientPickedUpRemind = 1; //Напоминание о посадке клиента
        public static int OrderConfirmationRemind = 2; //Напоминание о подтверждении заказа
    }

    public static class DeliveryType {
        public static int None = 0; //Отчет о доставке сообщения не нужен
        public static int Report = 1; //Нужен отчет о доставке сообщения
        public static int WithRespond = 2; //Нужен отчет о доставке сообщения и ответ на сообщение
    }

    public static class ResponseType {
        public static int Ok = 0;
        public static int Cancel = 1;
        public static int Timeout = 2;
    }


}
