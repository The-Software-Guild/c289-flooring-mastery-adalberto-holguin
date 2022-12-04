package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Component
public class FlooringMasterServiceImpl implements FlooringMasteryService {

    private FlooringMasteryDao dao;

    @Autowired
    public FlooringMasterServiceImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public void selectAndLoadOrders(LocalDate date, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException {
        //date format is ensured when collected from user
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddYYYY");

        String dateAsString = date.format(formatter);
        String fileName = ".\\orders\\Orders_" + dateAsString + ".txt";
        dao.setCurrentOrdersFile(fileName, action);
    }


    @Override
    public void loadStatesAndProducts() throws FlooringMasteryPersistenceException {
        dao.loadStatesAndProductsLists();
    }

//    public File getFile(LocalDate date) {
//        //date format is ensured when collected from user
//        File checkfile;
//        String fileName = "Orders_" + date + ".txt";
//        checkfile = dao.getFile(fileName);
//        return checkfile;
//    }


    public boolean FileExist(LocalDate date) {
//        //date format is ensured when collected from user
//        boolean checkFile;
//        String fileName = "Orders_" + date + ".txt";
//        checkFile = dao.checkFileExist(fileName);
//        return checkFile;
        return true;
    }

    @Override
    public void checkFileIsEmpty() throws IOException {
        dao.deleteFileIfEmpty();
    }

    @Override
    public void enterOrder(Order order) throws FlooringMasteryPersistenceException {
        dao.addOrder(order);
    }

    @Override
    public void storeEditedOrder() throws FlooringMasteryPersistenceException {
        dao.editOrder();
    }

    @Override
    public Order calculatePrices(Order order) {
        BigDecimal materialCost = calculateMaterialCost(order, order.getArea(), order.getCostPerSquareFoot());
        BigDecimal laborCost = calculateLaborCost(order, order.getArea(), order.getLaborCostPerSquareFoot());

        // These two change when user edits state
        BigDecimal tax = calculateTax(order, materialCost, laborCost, order.getState());
        BigDecimal total = calculateTotal(order, materialCost, laborCost, tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);

        // These two change when user edits state
        order.setTax(tax);
        order.setTotal(total);

        return order;
    }

    @Override
    public void editOrder() {
        //call the dao's writeFile
    }

//    public void loadfile(Date date) {
//        dao.loadfile(date);
//    }


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
        removedOrder = removeOrder(removedOrder);
//        if (removedOrder != null) {
//            dao.checkFileExist("Order #"
//                    + removedOrder.getOrderNumber() + " for date"
//                    + removedOrder.getDate() + " REMOVED");
//            return removedOrder;
//        } else {
//            throw new UnsupportedOperationException("ERROR: No orders with that number exists on that date");
//        }
        return removedOrder;
    }

    @Override
    public List<Order> retrieveOrdersList() {
        return dao.getOrdersList();
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
        return null;
    }
}
