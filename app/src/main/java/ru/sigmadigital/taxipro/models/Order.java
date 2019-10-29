package ru.sigmadigital.taxipro.models;

import java.io.Serializable;
import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Order {

    public static class DriverOrderFilter extends JsonParser {
        /*  int id; //Идентификатор заказа
          boolean completed;*/
        int skip;
        int limit;

        public DriverOrderFilter(/*int id, boolean completed,*/ int skip, int limit) {
          /*  this.id = id;
            this.completed = completed;*/
            this.skip = skip;
            this.limit = limit;
        }
    }

    public static class DriverOrder extends JsonParser implements Serializable {
        int id;
        int areaId;
        String pickupTime;//если нулл то ближайшее время
        String price;

        String description;

        List<TitledGeoAddress> routeItems;
        String clientPhone; //Если есть, то звоним по нему, если нет, то вызываем команду Order.CallFromDriverToClient
        String branchPhone; //Если есть, то звоним по нему, если нет, то вызываем команду Order.CallFromDriverToOperator
        int billingType; //Тип расчета - обычный (наличные или контрагент) или банковская карта
        int state; //Статус заказа. Если водитель берет заказ из списка или заказ назначает диспетчер, то статус Appointed. Если берет по предложению, то статус Confirmed

        String canConfirmAt; //Время, после которого водитель может подтвердить предзаказ. Null, если заказ на ближайшее время date-time
        double surge; //Наценка decimal

        double totalTime; //Потраченное на заказ время в минутах с момента "Клиент сел". Поле не равно нулю только в завершенных заказах.
        double totalDistance; //Пройденный путь в метрах с момента "Клиент сел". Поле не равно нулю только в завершенных заказах.
        double driverWaitingTime; // Время ожидания клиента
        int freeWaiting;//Бесплатное ожидание

        int vehicleTypeId;//Идентификатор типа авто
        String vehicleType;
        String dismissedAt;//Время снятия  date-time
        int commandId; //Идентификатор команды

        //List<String> tags;


        double timezone;
        String surcharge;
        Order.AdditionalServices additionalServices;
        int activityIncrease; //Активность при выполнении
        int activityFine; //Активность при отказе


        public int getId() {
            return id;
        }

        public int getAreaId() {
            return areaId;
        }

        public String getPickupTime() {
            return pickupTime;
        }

        public String getPrice() {
            return price;
        }

        public List<TitledGeoAddress> getRouteItems() {
            return routeItems;
        }

        public String getDescription() {
            return description;
        }

        public String getClientPhone() {
            return clientPhone;
        }

        public String getBranchPhone() {
            return branchPhone;
        }

        public int getBillingType() {
            return billingType;
        }

        public int getState() {
            return state;
        }

        public String getCanConfirmAt() {
            return canConfirmAt;
        }

        public double getSurge() {
            return surge;
        }

        public double getTotalTime() {
            return totalTime;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public double getDriverWaitingTime() {
            return driverWaitingTime;
        }

        public int getFreeWaiting() {
            return freeWaiting;
        }

        public int getVehicleTypeId() {
            return vehicleTypeId;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public String getDismissedAt() {
            return dismissedAt;
        }

        public int getCommandId() {
            return commandId;
        }

        public double getTimezone() {
            return timezone;
        }

        public String getSurcharge() {
            return surcharge;
        }

        public AdditionalServices getAdditionalServices() {
            return additionalServices;
        }

        public int getActivityIncrease() {
            return activityIncrease;
        }

        public int getActivityFine() {
            return activityFine;
        }
    }

    public static class BillingType {
        public final static int usual = 0;
        public final static int card = 1;
        public final static int contractor = 2;
    }


    public static class DriverOrderHasBeenModifiedAsync extends JsonParser {  //Заказ назначен
        int orderId;
        int cause; //driverOrderModifyCause
        DriverOrder order;
        int commandId;

        public int getOrderId() {
            return orderId;
        }

        public int getCause() {
            return cause;
        }

        public DriverOrder getOrder() {
            return order;
        }

        public int getCommandId() {
            return commandId;
        }
    }


    public static class DriverOrderModifyCause {
        public static int AppointedByDriver = 0; //Взят водителем
        public static int AppointedByOperator = 1; //Назначен оператором
        public static int OrderConfirmed = 2; // Подтвержден
        public static int DriverArrived = 3; //Подтвержден
        public static int ClientWasPickedUp = 4; //Водитель забрал клиента
        public static int WaitedForCompletion = 5; //Ждет перерасчета
        public static int Completed = 6; //Завершен
        public static int RequirementModified = 7; //Заказ изменен
        public static int PriceModified = 8; //Стоимость заказа изменилась
        public static int BecameAvailable = 9; //Заказ стал доступен для водителя
    }


    public static class HasBeenBecomeUnavailableForDriverAsync extends JsonParser {// Водителю больше недоступен данный заказ
        int orderId;
        int cause; //order.unavailabilityCause
        int commandId;

    }

    public static class UnavailabilityCause { //Причина снятия водителя с заказа
        public static int DriverCancellation = 0; //Водитель отказался
        public static int OrderCancellation = 1; //Заказ отменили
        public static int TakedBySomeoneElse = 2; // Взят кем-то другим
    }


    //states

    public static class OrderState {
        public final static int Accepted = 1;//Принят
        public final static int Appointed = 2;//Назначен
        public final static int Confirmed = 3;//Подтвержден
        public final static int DriverArrived = 4;//По адресу
        public final static int ClientWasPickedUp = 5;//Клиент сел
        public final static int WaitedForCompletion = 6;//Ожидает завершения
        public final static int Completed = 7;//Удачно завершен
        public final static int Canceled = 8;//Отменен
    }


    public static class Confirm extends JsonParser { //Подтвердить заказа
        int orderId;
        Geo geo;

        public Confirm(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }

    public static class AppointDriver extends JsonParser {
        int orderId;
        Geo geo;
        int estimatedTime; //Время, через которое водитель подъедет

        public AppointDriver(int orderId, Geo geo, int estimatedTime) {
            this.orderId = orderId;
            this.geo = geo;
            this.estimatedTime = estimatedTime;
        }
    }

    public static class DriverArrive extends JsonParser { // Отметиться по адресу подачи
        int orderId;
        Geo geo;

        public DriverArrive(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }

    public static class DriverPickupClient extends JsonParser {//Забрать клиента
        int orderId;
        Geo geo;

        public DriverPickupClient(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }

    public static class WaitForCompletion extends JsonParser {  //Водитель завершает заказ и рассчитывается с клиентом.
        int orderId;
        Geo geo;

        public WaitForCompletion(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }


    public static class Complete extends JsonParser {  //Завершить заказ
        int orderId;
        int rating;
        Geo geo;

        public Complete(int orderId, int rating, Geo geo) {
            this.orderId = orderId;
            this.rating = rating;
            this.geo = geo;
        }
    }

    public static class HasBeenCompleted extends JsonParser {//Заказ завершен
        Driver.PaidRatePeriod paidRatePeriod;
    }


    public static class RemoveDriver extends JsonParser {
        int orderId;
        Geo geo;

        public RemoveDriver(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }


    //дополнительно
    public static class AdditionalServices extends JsonParser implements Serializable {

        boolean luggage;
        boolean luggageToDoor;
        int babyChair;
        boolean animal;
        int smoking;
        boolean nameplate; //Табличка
        int adv; //Обклейка машины
        boolean sinistral; //Левый руль
        boolean ticket; //Квитанция

        public boolean isLuggage() {
            return luggage;
        }

        public boolean isLuggageToDoor() {
            return luggageToDoor;
        }

        public int getBabyChair() {
            return babyChair;
        }

        public boolean isAnimal() {
            return animal;
        }

        public int getSmoking() {
            return smoking;
        }

        public boolean isNameplate() {
            return nameplate;
        }

        public int getAdv() {
            return adv;
        }

        public boolean isSinistral() {
            return sinistral;
        }

        public boolean isTicket() {
            return ticket;
        }
    }

    public static class AdvType {
        public final static int Unimportant = 0; //Не важно
        public final static int Logo = 1; // Логотип
        public final static int Owner = 2; //Частник
    }

    public static class BabyChairTypes {
        public final static int None = 0; //Без кресел
        public final static int Booster = 1; //  Удерживающее устройство
        public final static int BoosterX2 = 2; //2 удерживающих устройства
        public final static int Chair = 3; //Детское кресло
        public final static int ChairAndBooster = 4; //Детское кресло и удерживающее устройство
    }

    public static class SmokingType {
        public final static int Unimportant = 0;
        public final static int Smoking = 1;
        public final static int NonSmoker = 2;
    }


    //позвонить
    public static class CallToClient extends JsonParser {
        int orderId;
        Geo geo;

        public CallToClient(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }

    public static class CallToOperator extends JsonParser {
        int orderId;
        Geo geo;

        public CallToOperator(int orderId, Geo geo) {
            this.orderId = orderId;
            this.geo = geo;
        }
    }


    //Расстояние до заказа
    public static class OrderDriverDistanceFilter extends JsonParser {
        List<Integer> ids;
       /* int skip;
        int limit;*/

        public OrderDriverDistanceFilter(List<Integer> ids) {
            this.ids = ids;
        }
    }

    public static class OrderDriverDistance extends JsonParser implements Serializable {
        int id;
        double distance;// метры

        public int getId() {
            return id;
        }

        public String getDistance() {
            double d = distance/1000;

            if (d%1==0){
                return (int) d + " км";
            } else {
                return String.format("%.1f", d) + " км" ;
            }
        }
    }


}
