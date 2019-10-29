package ru.sigmadigital.taxipro.models.my;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.Order;

public class OrdersInArea {

    private Area.AreaPanel area;
    private List<Order.DriverOrder> orders;

    public OrdersInArea(Area.AreaPanel area) {
        this.area = area;
        orders = new ArrayList<>();
    }

    public void addOrder(Order.DriverOrder order) {
        orders.add(order);
    }

    public int[] getIds(){
        return area.getIds();
    }

    public String getAreaName() {
        return area.getName();
    }

    public List<Order.DriverOrder> getOrders() {
        return orders;
    }

    public int getOrdersCount(){
        return orders.size();
    }

    public Area.AreaPanel getArea() {
        return area;
    }
}
