package com.sg.assessment.service;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public interface FlooringMasteryService {

    void enterOrder(Order order) throws FlooringMasteryPersistenceException;

//    public File getFile(LocalDate date);

//    public void checkFile(LocalDate date);

    void storeEditedOrder() throws FlooringMasteryPersistenceException;

    Order calculatePrices(Order order);

//    public void loadfile(Date date);

    void checkFileIsEmpty() throws IOException;

    BigDecimal calculateMaterialCost(Order order, BigDecimal area, BigDecimal costPerSquareFoot);

    BigDecimal calculateLaborCost(Order order, BigDecimal area, BigDecimal laborCostPerSquareFoot);

    BigDecimal calculateTax(Order order, BigDecimal materialCost, BigDecimal laborCost, String stateAbbr);

    BigDecimal calculateTotal(Order order, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    void selectAndLoadOrders(LocalDate date, Action action) throws FlooringMasteryPersistenceException, NoOrdersOnDateException;

    void loadStatesAndProducts() throws FlooringMasteryPersistenceException;

    List<Order> retrieveOrdersList();

    List<State> retrieveStatesList();

    List<Product> retrieveProductsList();

    Order retrieveOrder(LocalDate dateChoice, int orderNumber)
        throws UnsupportedOperationException;

    void removeOrder(Order order) throws FlooringMasteryPersistenceException;

}

