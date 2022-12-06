package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;

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
        return null;
    }

    @Override
    public List<State> getStatesList() {
        return null;
    }

    @Override
    public List<Product> getProductsList() {
        return null;
    }

    @Override
    public void setCurrentOrdersFile(String fileName, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException {

    }

    @Override
    public void writeToExportFile() throws FlooringMasteryPersistenceException, NoOrdersOnDateException {

    }
}
