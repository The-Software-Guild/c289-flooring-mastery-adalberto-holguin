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
import java.util.List;


@Component
public class FlooringMasterServiceImpl implements FlooringMasteryService {

    private FlooringMasteryDao dao;

    @Autowired
    public FlooringMasterServiceImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    @Override
    public Order calculatePrices(Order order) {
        // Calculations for material and labor costs.
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));

        // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
        BigDecimal tax = null;
        for (State state : dao.getStatesList()) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = materialCost.add(laborCost);
                tax = costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN);
                break;
            }
        }

        // Calculations for total.
        BigDecimal materialsPlusLabor = materialCost.add(laborCost);
        BigDecimal total = materialsPlusLabor.add(tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        return order;
    }

    @Override
    public void setOrdersFile(LocalDate date, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String dateAsString = date.format(formatter);
        String fileName = ".\\orders\\Orders_" + dateAsString + ".txt";
        dao.setCurrentOrdersFile(fileName, action);
    }

    @Override
    public void loadStatesAndProducts() throws FlooringMasteryPersistenceException {
        dao.loadStatesAndProductsLists();
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
    public void enterOrder(Order order) throws FlooringMasteryPersistenceException {
        dao.addOrder(order);
    }

    @Override
    public void storeEditedOrder() throws FlooringMasteryPersistenceException {
        dao.editOrder();
    }

    @Override
    public void removeOrder(Order orderToRemove) throws FlooringMasteryPersistenceException {
        dao.removeOrder(orderToRemove);
    }

    public void exportData() throws FlooringMasteryPersistenceException, NoOrdersOnDateException{
        dao.writeToExportFile();
    }

    @Override
    public void deleteEmptyFile() {
        dao.deleteFile();
    }
}
