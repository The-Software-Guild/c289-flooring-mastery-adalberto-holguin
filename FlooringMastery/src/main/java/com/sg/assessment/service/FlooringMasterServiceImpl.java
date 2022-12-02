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

    @Autowired
    public FlooringMasterServiceImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public void selectAndLoadOrdersFile(LocalDate date) {
        //date format is ensured when collected from user
        String fileName = "Order_" + date + ".txt";
        dao.setCurrentFile(fileName);
    }

    @Override
    public void enterOrder(Order order) {
        dao.addOrder(order);
    }

    @Override
    public Order calculatePrices(Order order) {
        BigDecimal materialCost = calculateMaterialCost(order, order.getArea(), order.getCostPerSquareFoot());
        BigDecimal laborCost = calculateLaborCost(order, order.getArea(), order.getLaborCostPerSquareFoot());
        BigDecimal tax = calculateTax(order, materialCost, laborCost, order.getState());
        BigDecimal total = calculateTotal(order, materialCost, laborCost, tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        return order;
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
    public List<Order> retrieveOrdersList(LocalDate orderDate) {
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
    public Order retrieveOrder(LocalDate dateChoice, int orderNumber) throws UnsupportedOperationException {

    }
}
