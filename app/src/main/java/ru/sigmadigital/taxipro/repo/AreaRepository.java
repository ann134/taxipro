package ru.sigmadigital.taxipro.repo;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.OrdersInArea;

public class AreaRepository {

   /* private static AreaRepository instance;
    private MutableLiveData<List<Area.AreaPanel>> areasLiveData = new MutableLiveData<>();

    public static AreaRepository getInstance() {
        if (instance == null) {
            instance = new AreaRepository();
        }
        return instance;
    }

    public void setAreasList(List<Area.AreaPanel> areasList) {
        areasLiveData.setValue(areasList);
    }

    public List<Area.AreaPanel> getAreas() {
        return areasLiveData.getValue();
    }

    public MutableLiveData<List<Area.AreaPanel>> getAreasLiveData() {
        return areasLiveData;
    }



    // sorted orders by areas
    private List<OrdersInArea> ordersInAreas = new ArrayList<>();
    private MutableLiveData<List<OrdersInArea>> ordersInAreasLiveData = new MutableLiveData<>();

    public void sortOrdersByAreas(){
        for (Area.AreaPanel area : getAreas()){
            ordersInAreas.add(new OrdersInArea(area));
        }

        for (Order.DriverOrder driverOrder : OrdersRepository.getInstance().getOrders()){
            getOrdersInArea(driverOrder.getAreaId()).addOrder(driverOrder);
        }

        ordersInAreasLiveData.setValue(ordersInAreas);
    }

    private OrdersInArea getOrdersInArea(int areaId){
        for (OrdersInArea area : ordersInAreas){
            for (int id : area.getIds()){
                if (id == areaId){
                    return area;
                }
            }
        }
        return  null;
    }


    public MutableLiveData<List<OrdersInArea>> getOrdersInAreasLiveData() {
        return ordersInAreasLiveData;
    }*/
}
