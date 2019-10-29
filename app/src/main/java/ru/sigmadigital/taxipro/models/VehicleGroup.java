package ru.sigmadigital.taxipro.models;

import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class VehicleGroup {


    public static class DriverRatePeriod extends JsonParser {
        //Покупная смена
        String name;
        String price;
        int hours;

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public int getHours() {
            return hours;
        }
    }


    public static class DriverRatePeriodsForSale extends JsonParser {
        List<DriverRatePeriod> periods;

        public List<DriverRatePeriod> getPeriods() {
            return periods;
        }
    }


    public static class DriverRatePeriodsForSaleFilter extends JsonParser {

    }

}