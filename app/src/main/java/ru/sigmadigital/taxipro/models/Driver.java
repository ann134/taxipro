package ru.sigmadigital.taxipro.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Driver {


    //profile
    public static class ProfileFilter extends JsonParser {

    }

    public static class Profile extends JsonParser {
        String callsign;
        String name;
        String fullName;
        String phone;
        String vehicleName;
        int cityId;
        int state;
        boolean isSos;
        int activity;


        DriverSetting.DriverSettingsValue settings;
        TitledGeoAddress homeAddress;
        int commandId;

        public String getCallsign() {
            return callsign;
        }

        public String getName() {
            return name;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhone() {
            return phone;
        }

        public String getVehicleName() {
            return vehicleName;
        }

        public int getCityId() {
            return cityId;
        }

        public int getState() {
            return state;
        }

        public boolean isSos() {
            return isSos;
        }

        public DriverSetting.DriverSettingsValue getSettings() {
            return settings;
        }

        public TitledGeoAddress getHomeAddress() {
            return homeAddress;
        }

        public int getCommandId() {
            return commandId;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setSos(boolean sos) {
            isSos = sos;
        }

        public int getActivity() {
            return activity;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }
    }


    public static class StateHasBeenModifiedAsync extends JsonParser {
        int previousState;
        int state;
        int commandId;

        public int getPreviousState() {
            return previousState;
        }

        public int getState() {
            return state;
        }

        public int getCommandId() {
            return commandId;
        }
    }

    public static class DriverState {
        public static final int created = 0;
        public static final int moderating = 1;
        public static final int offline = 2;
        public static final int online = 3;
        public static final int onWayHome = 4;
        public static final int blocked = 5;
        public static final int fired = 6;
    }

    public static class NavigationType {
        public static int internal = 0;
        public static int doubleGis = 1;
        public static int yandex = 2;
        public static int google = 3;
    }

    public static class NightMode {
        public static int auto = 0;
        public static int enabled = 1;
        public static int disabled = 2;
    }



    //Feed
    public static class FeedFilter extends JsonParser {

    }

    public static class Feed extends JsonParser {
        List<Order.DriverOrder> orders;
        Driver.FeedNews news;
        DriverNotification.Notification notification;
        Driver.PaidRatePeriod paidRatePeriod;


        public List<Order.DriverOrder> getOrders(){
            List<Order.DriverOrder>  list = new ArrayList<>();
            for (Order.DriverOrder driverOrder : orders) {
                if (driverOrder.getPickupTime() == null) {
                    list.add(driverOrder);
                }
            }
            return list;
        }

        public List<Order.DriverOrder> getPreOrders(){
            List<Order.DriverOrder>  list = new ArrayList<>();
            for (Order.DriverOrder driverOrder : orders) {
                if (driverOrder.getPickupTime() != null) {
                    list.add(driverOrder);
                }
            }
            return list;
        }

        public void addOrder(Order.DriverOrder order) {
            orders.add(order);
        }


        /*public List<Order.DriverOrder> getOrders() {
            return orders;
        }*/

        public FeedNews getNews() {
            return news;
        }

        public DriverNotification.Notification getNotification() {
            return notification;
        }

        public PaidRatePeriod getPaidRatePeriod() {
            return paidRatePeriod;
        }
    }

    public static class FeedNews extends JsonParser {
        String date;
        String title;
        String body;
        int commandId;

    }


    //paid rate period
    public static class PaidRatePeriod extends JsonParser {
        String name;
        String from;
        String to;
        double price;
        boolean isPaid;


        public String getName() {
            return name;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public double getPrice() {
            return price;
        }

        public boolean isPaid() {
            return isPaid;
        }
    }


    public static class PaidRatePeriodFilter extends JsonParser {
        int skip;
        int limit;

        public PaidRatePeriodFilter(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
    }


    public static class TakeOffRatePeriod extends JsonParser {

    }


    public static class TakeRatePeriod extends JsonParser {
        int index;

        public TakeRatePeriod(int index) {
            this.index = index;
        }
    }








    //balance
    public static class BalanceFilter extends JsonParser {

    }

    public static class Balance extends JsonParser implements Serializable {
        String weekTotal;
        int weekOrderCount;
        String currentWeekStart;
        String currentBalance;
        String currentPaidPeriod; //Текущая смена
        int currentPaidPeriodState; //Состояние смены
        String previousWeekTotal;
        String currentMonthTotal;

        public String getWeekTotal() {
            return weekTotal;
        }

        public int getWeekOrderCount() {
            return weekOrderCount;
        }

        public String getCurrentWeekStart() {
            return currentWeekStart;
        }

        public String getCurrentBalance() {
            return currentBalance;
        }

        public String getCurrentPaidPeriod() {
            return currentPaidPeriod;
        }

        public int getCurrentPaidPeriodState() {
            return currentPaidPeriodState;
        }

        public String getPreviousWeekTotal() {
            return previousWeekTotal;
        }

        public String getCurrentMonthTotal() {
            return currentMonthTotal;
        }
    }


    public static class CurrentPaidPeriodState {
        public static int None = 0;//Нет
        public static int Took = 1;//Куплена
        public static int Activated = 2;//Активирована
    }





    //DayEarn
    public static class DayEarnFilter extends JsonParser {
        String day;

        public DayEarnFilter(String day) {
            this.day = day;
        }
    }

    public static class DayEarn extends JsonParser {
        String date;
        String orderTotal;//Сумма по заказам
        String surgeTotal;//Сумма по наценкам
        String paidPeriodTotal;//Сумма по оплаченым сменам
        String feeTotal;//Сумма по комиссии
        String totalEarn;// Общий доход за день
        double  timeOnline;//Время в сети в минутах
        int orderCount;
        List<Driver.DayOrder> orders;

        public String getDate() {
            return date;
        }

        public String getOrderTotal() {
            return orderTotal;
        }

        public String getSurgeTotal() {
            return surgeTotal;
        }

        public String getPaidPeriodTotal() {
            return paidPeriodTotal;
        }

        public String getFeeTotal() {
            return feeTotal;
        }

        public String getTotalEarn() {
            return totalEarn;
        }

        public double getTimeOnline() {
            return timeOnline;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public List<DayOrder> getOrders() {
            return orders;
        }
    }

    public static class DayOrder extends JsonParser { //Заказ в суточном доходе
        String date;
        String price;

        public String getDate() {
            return date;
        }

        public String getPrice() {
            return price;
        }
    }



    //weekEarn
    public static class WeekEarnFilter extends JsonParser {
        int skip;
        int limit;

        public WeekEarnFilter(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
    }

    public static class WeekEarn extends JsonParser {
        String start;
        String total;
        double timeOnline;
        int orderCount;
        List<Driver.WeekDay> weekDays;
        List<Driver.WeekPaidPeriod> weekPaidPeriods;

        public String getStart() {
            return start;
        }

        public String getTotal() {
            return total;
        }

        public double getTimeOnline() {
            return timeOnline;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public List<WeekDay> getWeekDays() {
            return weekDays;
        }

        public List<Driver.WeekPaidPeriod> getWeekPaidPeriods() {
            return weekPaidPeriods;
        }
    }

    public static class WeekDay extends JsonParser implements Serializable {
        String date;
        String total;
        int orderCount;

        public String getDate() {
            return date;
        }

        public String getTotal() {
            return total;
        }

        public int getOrderCount() {
            return orderCount;
        }
    }

    public static class WeekPaidPeriod extends JsonParser {
        String date;
        String price;
        String title;

        public String getDate() {
            return date;
        }

        public String getPrice() {
            return price;
        }

        public String getTitle() {
            return title;
        }
    }



    //transactions
    public static class TransactionFilter extends JsonParser {
        String from;
        String to;
       /* int skip;
        int limit;*/

        /*public TransactionFilter(String from, String to, int skip, int limit) {
            this.from = from;
            this.to = to;
            this.skip = skip;
            this.limit = limit;
        }*/

        public TransactionFilter() {
        }

        public TransactionFilter(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }


    public static class Transaction extends JsonParser {
        String date;
        String title;
        String total;

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public String getTotal() {
            return total;
        }
    }




    //status
    public static class GoHome extends JsonParser {

        Geo geo;
        GeoAddress point;

        public GoHome(Geo geo, GeoAddress point) {
            this.geo = geo;
            this.point = point;
        }
    }

    public static class GoOffline extends JsonParser {

        Geo geo;

        public GoOffline(Geo geo) {
            this.geo = geo;
        }
    }

    public static class GoOnline extends JsonParser {

       // int vehicleId;
        Geo geo;

        public GoOnline(/*int vehicleId,*/ Geo geo) {
            /*this.vehicleId = vehicleId;*/
            this.geo = geo;
        }
    }


    //ModifyPhone
    public static class ModifyPhone extends JsonParser {
        String newPhone;

        public ModifyPhone(String newPhone) {
            this.newPhone = newPhone;
        }
    }

    public static class ConfirmModifyingPhone extends JsonParser {
        String confirmationCode;

        public ConfirmModifyingPhone(String confirmationCode) {
            this.confirmationCode = confirmationCode;
        }
    }



    //sos
    public static class SendSos extends JsonParser {

    }

    public static class CancelSos extends JsonParser {
        Geo geo;

        public CancelSos(Geo geo) {
            this.geo = geo;
        }
    }



    public static class CallToOperator extends JsonParser {
        Geo geo;

        public CallToOperator(Geo geo) {
            this.geo = geo;
        }
    }

    public static class ReadLastNews extends JsonParser {

    }


    //value

    public static class Activity extends  JsonParser{
        int value;

        public Activity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }


    public static class ActivityFilter extends JsonParser {

    }

    public static class ActivityHistory extends JsonParser {
        String date;
        int activityValue;
        int activityDelta;
        String description;

        public ActivityHistory(String date, int activityValue, int activityDelta, String description) {
            this.date = date;
            this.activityValue = activityValue;
            this.activityDelta = activityDelta;
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getActivityValue() {
            return activityValue;
        }

        public void setActivityValue(int activityValue) {
            this.activityValue = activityValue;
        }

        public int getActivityDelta() {
            return activityDelta;
        }

        public void setActivityDelta(int activityDelta) {
            this.activityDelta = activityDelta;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class ActivityHistoryFilter extends JsonParser {
        int skip;
        int limit;

        public ActivityHistoryFilter() {
        }

        public ActivityHistoryFilter(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
    }






    public static class TransferToQiwi extends JsonParser {
        int value;

        public TransferToQiwi(int value) {
            this.value = value;
        }
    }



}
