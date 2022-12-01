package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;


@Component
public class FlooringMasterServiceImpl implements FlooringMasteryService {

    private FlooringMasteryDao dao;


    public void selectOrderFileFromDate(LocalDate date) {
        //date format is ensured when collected from user
        String fileName = "Order_"+date+".txt";
        dao.setCurrentFile(fileName);
    }

    @Autowired
    public FlooringMasterServiceImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public Order addOrderBasedOnDate(LocalDate date, int orderNumber, String customerName, String state, String productType,
                                     BigDecimal area) throws UnsupportedOperationException {

        selectOrderFileFromDate(date);
        List<Order> orderList = dao.getOrdersList();
        Order newOrder = o

        //Get missing props
        newOrder.setDate(date);

        //ensure area is at least 100 sq ft

        List<Product> productList = dao.getProductsList();
        BigDecimal costPerSquareFoot; //get from product in productList
        BigDecimal laborCostPerSquareFoot;


        List<State> stateList = dao.getStatesList();
        String stateAbbreviation;
        String stateName;
        BigDecimal taxRate;

//        newOrder.setTaxRate = taxRate;
//        newOrder.setCostPerSquareFoot = costPerSquareFoot;
//        newOrder.setLaborCostPerSquareFoot = laborCostPerSquareFoot;
//
//        //from service
//        newOrder.setMaterialCost = new BigDecimal(calculateMaterialCost(newOrder.getArea(), newOrder.getCostPerSquareFoot()));
//        newOrder.setLaborCost = new BigDecimal(calculateLaborCost(newOrder.getArea(), newOrder.getLaborCostPerSquareFoot));
//        newOrder.setTax = new BigDecimal(calculateTax(newOrder.getMaterialCost(), newOrder.getLaborCost(), newOrder.getState()));
//
//        newOrder.setTotal = new BigDecimal(newOrder.getMaterialCost(), newOrder.getLaborCost, newOrder.getTax());

        return newOrder;
    }

    @Override
    public void calculatePricesAndStoreOrder(Order order, LocalDate orderDate) {
        BigDecimal materialCost = calculateMaterialCost(order, order.getArea(), order.getCostPerSquareFoot());
        BigDecimal laborCost = calculateLaborCost(order, order.getArea(), order.getLaborCostPerSquareFoot());
        BigDecimal tax = calculateTax(order, materialCost, laborCost, order.getState());
        BigDecimal total = calculateTotal(order, materialCost, laborCost, tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        selectOrdersFile(orderDate); // Setting correct file name in the dao before sending order to be added
        dao.addOrder(order);
    }


    @Override
    public BigDecimal calculateMaterialCost(Order order, BigDecimal area, BigDecimal costPerSquareFoot) {
        BigDecimal materialCost = area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_EVEN);
        return materialCost;
    }

    @Override
    public BigDecimal calculateLaborCost(Order order, BigDecimal area, BigDecimal laborCostPerSquareFoot) {
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_EVEN);
        return laborCost;
    }

    @Override
    public BigDecimal calculateTax(Order order, BigDecimal materialCost, BigDecimal laborCost, String stateAbbr) {
        BigDecimal tax = new BigDecimal("0");

        // Getting State from statesList that matches abbrv in the Order so we can calculate tax
        List<State> statesList = dao.getStatesList();
        for (State state : statesList) {
            if (state.getStateAbbreviation().equals(stateAbbr)) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = materialCost.add(laborCost);
                tax = costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        return tax;
    }

    @Override
    public BigDecimal calculateTotal(Order order, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        BigDecimal materialsPlusLabor = materialCost.add(laborCost);
        BigDecimal total = materialsPlusLabor.add(tax);
        order.setTotal(total);
        return total;
    }

    @Override
    public void selectOrdersFile(LocalDate date) {
        dao.setCurrentFile("Orders_" + date);
    }


    @Override
    public Order removeOrder(Order removedOrder) throws UnsupportedOperationException {
        removedOrder = FlooringMasteryDao.removeOrder(removedOrder);
        if (removedOrder != null) {
            dao.writeAuditEntry("Order #"
                    + removedOrder.getOrderNumber() + " for date"
                    + removedOrder.getDate() + " REMOVED");
            return removedOrder;
        } else {
            throw new UnsupportedOperationException("ERROR: No orders with that number exists on that date");
        }
    }

    @Override
    public List<Order> retrieveOrdersList() {
        return dao.getAllOrders();
    }

    @Override
    public List<State> retrieveStatesList() {
        return dao.getStatesList();
    }

    @Override
    public List<Product> retrieveProductsList() {
        return dao.getProductsList();
    }

    @Override
    public List<Order> getOrders(LocalDate dateChoice) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public Order getOrder(LocalDate dateChoice, int orderNumber) throws UnsupportedOperationException {
        return null;
    }
}
