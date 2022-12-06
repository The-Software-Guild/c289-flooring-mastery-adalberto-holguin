package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao {

    @Override
    public void loadStatesAndProductsLists() throws FlooringMasteryPersistenceException {

    }

    @Override
    public void addOrder(Order order) throws FlooringMasteryPersistenceException {

    }

    @Override
    public void editOrder() throws FlooringMasteryPersistenceException {

    }

    @Override
    public void removeOrder(Order order) throws FlooringMasteryPersistenceException {

    }

    @Override
    public void loadOrdersFile() throws FlooringMasteryPersistenceException {

    }

    @Override
    public void deleteFile() {

    }

    @Override
    public List<Order> getOrdersList() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(12, null, null, null, null, null,
                null, null, null, null, null, null));
        orderList.add(new Order(34, null, null, null, null, null,
                null, null, null, null, null, null));
        return orderList;
    }

    @Override
    public List<State> getStatesList() {
        List<State> stateList = new ArrayList<>();
        stateList.add(new State("TX", "Texas", new BigDecimal("4.45")));
        stateList.add(new State("WA", "Washington", new BigDecimal("9.25")));
        stateList.add(new State("KY", "Kentucky", new BigDecimal("6.00")));
        stateList.add(new State("CA", "California", new BigDecimal("25.00")));
        return stateList;
    }

    @Override
    public List<Product> getProductsList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        productList.add(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        productList.add(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        productList.add(new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));
        return productList;
    }

    @Override
    public void setCurrentOrdersFile(String fileName, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException {

    }

    @Override
    public void writeToExportFile() throws FlooringMasteryPersistenceException, NoOrdersOnDateException {

    }
}
