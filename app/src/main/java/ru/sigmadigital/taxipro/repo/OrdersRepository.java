package ru.sigmadigital.taxipro.repo;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.DistansesInArea;
import ru.sigmadigital.taxipro.models.my.OrdersInArea;

public class OrdersRepository {

    private static OrdersRepository instance;

    public static OrdersRepository getInstance() {
        if (instance == null) {
            instance = new OrdersRepository();
        }
        return instance;
    }

    //areas
    private List<Area.AreaPanel> areasList;

    public void setAreasList(List<Area.AreaPanel> areasList) {
        this.areasList = areasList;
        tryToSort();
    }


    //orders
    private List<Order.DriverOrder> ordersList;
    private List<Order.DriverOrder> preordersList;

    private MutableLiveData<List<Order.DriverOrder>> ordersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Order.DriverOrder>> preordersLiveData = new MutableLiveData<>();

    public void setOrdersList(List<Order.DriverOrder> list) {

        ordersList = new ArrayList<>();
        preordersList = new ArrayList<>();

        for (Order.DriverOrder driverOrder : list) {
            if (driverOrder.getPickupTime() == null) {
                ordersList.add(driverOrder);
            } else {
                preordersList.add(driverOrder);
            }
        }

        ordersLiveData.setValue(ordersList);
        preordersLiveData.setValue(preordersList);

        tryToSort();
    }

    public boolean removeCompleteOrder(Order.DriverOrder order){
        boolean cond1 = false;
        boolean cond2 = false;
        boolean cond3 = false;
        boolean cond4 = false;
        for(Order.DriverOrder x : ordersList){
            if(x.getId() == order.getId()){
                ordersList.remove(x);
                ordersLiveData.setValue(ordersList);
                cond1 = true;
                break;
            }
        }

        for(OrdersInArea y : ordersInAreas){
            for(Order.DriverOrder x : y.getOrders()){
                if(x.getId() == order.getId()){
                    y.getOrders().remove(x);
                    ordersInAreasLiveData.setValue(ordersInAreas);
                    cond2 = true;
                    break;
                }
            }
        }

        if(cond1 && cond2){
            return true;
        }

        for(Order.DriverOrder x : preordersList){
            if(x.getId() == order.getId()){
                ordersList.remove(x);
                ordersLiveData.setValue(ordersList);
                cond3 = true;
                break;
            }
        }

        for(OrdersInArea y : preordersInAreas){
            for(Order.DriverOrder x : y.getOrders()){
                if(x.getId() == order.getId()){
                    y.getOrders().remove(x);
                    ordersInAreasLiveData.setValue(ordersInAreas);
                    cond4 = true;
                    break;
                }
            }
        }

        return cond3 && cond4;
    }



    public MutableLiveData<List<Order.DriverOrder>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public MutableLiveData<List<Order.DriverOrder>> getPreOrdersLiveData() {
        return preordersLiveData;
    }




    // sorted orders by areas
    private List<OrdersInArea> ordersInAreas = new ArrayList<>();
    private List<OrdersInArea> preordersInAreas = new ArrayList<>();



    private MutableLiveData<List<OrdersInArea>> ordersInAreasLiveData = new MutableLiveData<>();
    private MutableLiveData<List<OrdersInArea>> preordersInAreasLiveData = new MutableLiveData<>();


    private  void tryToSort(){
        if (ordersList == null || areasList == null ){
            return;
        }

        sortOrdersByAreas(ordersList, ordersInAreas, ordersInAreasLiveData);
        sortOrdersByAreas(preordersList, preordersInAreas, preordersInAreasLiveData);
    }

    private void sortOrdersByAreas(List<Order.DriverOrder> orders, List<OrdersInArea> list, MutableLiveData<List<OrdersInArea>> liveData){
        for (Area.AreaPanel area : areasList){
            list.add(new OrdersInArea(area));
        }

        for (Order.DriverOrder driverOrder : orders){
                findOrdersInArea(list, driverOrder.getAreaId()).addOrder(driverOrder);
        }

        liveData.setValue(list);
    }

    private OrdersInArea findOrdersInArea(List<OrdersInArea> list, int areaId){
        for (OrdersInArea area : list){
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
    }

    public MutableLiveData<List<OrdersInArea>> getPreordersInAreasLiveData() {
        return preordersInAreasLiveData;
    }




    //distanses

    private List<DistansesInArea> distansesList = new ArrayList<>();

    public DistansesInArea getDistansesList(Area.AreaPanel areaPanel, boolean isPreorders) {
        for (DistansesInArea distansesInArea : distansesList){
            if (distansesInArea.getArea() == null && areaPanel == null && isPreorders == distansesInArea.isPreorder()){
                return distansesInArea;
            }
            if (distansesInArea.getArea() != null && areaPanel != null) {
                if (distansesInArea.getArea().getName().equals(areaPanel.getName()) && isPreorders == distansesInArea.isPreorder()) {
                    return distansesInArea;
                }
            }
        }
        return null;
    }

    public void addDistansesList(DistansesInArea newDistanses) {

        for (DistansesInArea distansesInArea : distansesList){

            if (distansesInArea.getArea() == null && newDistanses.getArea() == null && newDistanses.isPreorder() == distansesInArea.isPreorder()){
                distansesInArea.update(newDistanses);
                return;
            }
            if (distansesInArea.getArea() != null && newDistanses.getArea() != null && newDistanses.isPreorder() == distansesInArea.isPreorder()) {
                if (distansesInArea.getArea().getName().equals(newDistanses.getArea().getName())) {
                    distansesInArea.update(newDistanses);
                    return;
                }
            }
        }
        distansesList.add(newDistanses);
    }

}
