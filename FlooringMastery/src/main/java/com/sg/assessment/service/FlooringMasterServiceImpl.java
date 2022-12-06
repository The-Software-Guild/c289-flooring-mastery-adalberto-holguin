package com.sg.assessment.service;

import com.sg.assessment.service.exceptions.InvalidDateException;
import com.sg.assessment.dao.FlooringMasteryAuditDAO;
import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import com.sg.assessment.service.exceptions.InvalidStateException;
import com.sg.assessment.service.exceptions.NoSuchOrderException;
import com.sg.assessment.service.exceptions.NoSuchProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class FlooringMasterServiceImpl implements FlooringMasteryService {

    @Autowired
    FlooringMasteryDao dao;
    @Autowired
    FlooringMasteryAuditDAO auditDao;

    @Override
    public Order calculatePrices(Order order) {
        // Calculations for material and labor costs.
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN);

        // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
        BigDecimal tax = new BigDecimal("0");
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
    public void validateDate(LocalDate orderDate) throws InvalidDateException {
        if (orderDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Error, invalid date. New orders cannot be added to past dates.");
        }
    }

    @Override
    public int generateOrderNumber() {
        // Generates an order number for the user's order depending on the highest order number of the list of orders for that
        // date. If there are no orders for that date, assigns number 1 to the user's order.
        List<Order> ordersList = dao.getOrdersList();
        if (ordersList.size() == 0) {
            return 1;
        } else {
            int maxOrderNumber = 0;
            for (Order order : ordersList) {
                if (order.getOrderNumber() > maxOrderNumber) {
                    maxOrderNumber = order.getOrderNumber();
                }
            }
            return maxOrderNumber + 1;
        }
    }

    @Override
    public void validateState(String stateAbbrv) throws InvalidStateException {
        boolean stateIsAvailable = false;
        for (State state : dao.getStatesList()) {
            if (state.getStateAbbreviation().equals(stateAbbrv)) {
                stateIsAvailable = true;
            }
        }
        if (!stateIsAvailable) {
            throw new InvalidStateException("The chosen state is not on our list of available states.");
        }
    }

    @Override
    public void validateProduct(String productType) throws NoSuchProductException {
        boolean productIsAvailable = false;
        for (Product product : dao.getProductsList()) {
            if (product.getProductType().equals(productType)) {
                productIsAvailable = true;
            }
        }
        if (!productIsAvailable) {
            throw new NoSuchProductException("No product called " + productType + " found in our list of available products.");
        }
    }

    @Override
    public void loadStatesAndProducts() throws FlooringMasteryPersistenceException {
        dao.loadStatesAndProductsLists();
    }

    @Override
    public List<Order> retrieveOrdersList(Action action) throws NoOrdersOnDateException {
        List<Order> ordersList = dao.getOrdersList();
        if (ordersList.size() == 0 && !(action == Action.ADD)) {
            throw new NoOrdersOnDateException("No orders found for the specified date.");
        }
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
    public Order retrieveOrder(int orderNumber) throws NoSuchOrderException {
        for (Order order : dao.getOrdersList()) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        throw new NoSuchOrderException("No order found with that order number for the selected date.");
    }

    @Override
    public void enterOrder(Order order, LocalDate date) throws FlooringMasteryPersistenceException {
        dao.addOrder(order);

        // Writing audit entry.
        String dateAsString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = ".\\audits\\Audit_" + dateAsString + ".txt";
        auditDao.writeAuditEntry(LocalDateTime.now() + " -- placed order number " + order.getOrderNumber(), fileName);
    }

    @Override
    public void storeEditedOrder(Order editedOrder, LocalDate date) throws FlooringMasteryPersistenceException {
        dao.editOrder();

        // Writing audit entry.
        String dateAsString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = ".\\audits\\Audit_" + dateAsString + ".txt";
        auditDao.writeAuditEntry(LocalDateTime.now() + " -- edited order number " + editedOrder.getOrderNumber(), fileName);
    }

    @Override
    public void removeOrder(Order orderToRemove, LocalDate date) throws FlooringMasteryPersistenceException {
        dao.removeOrder(orderToRemove);

        // Writing audit entry.
        String dateAsString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = ".\\audits\\Audit_" + dateAsString + ".txt";
        auditDao.writeAuditEntry(LocalDateTime.now() + " -- removed order number " + orderToRemove.getOrderNumber(), fileName);
    }

    @Override
    public void exportData() throws FlooringMasteryPersistenceException, NoOrdersOnDateException {
        dao.writeToExportFile();
    }

    @Override
    public void deleteEmptyFile() {
        dao.deleteFile();
    }
}
