package com.sg.assessment.service;

import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public interface FlooringMasteryService {

    void enterOrder(Order order);

    Order calculatePrices(Order order);

    BigDecimal calculateMaterialCost(Order order, BigDecimal area, BigDecimal costPerSquareFoot);

    BigDecimal calculateLaborCost(Order order, BigDecimal area, BigDecimal laborCostPerSquareFoot);

    BigDecimal calculateTax(Order order, BigDecimal materialCost, BigDecimal laborCost, String stateAbbr);

    BigDecimal calculateTotal(Order order, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    void selectAndLoadOrdersFile(LocalDate date);

    List<Order> retrieveOrdersList();

    List<State> retrieveStatesList();

    List<Product> retrieveProductsList();

    Order retrieveOrder(LocalDate dateChoice, int orderNumber)
        throws UnsupportedOperationException;

    Order removeOrder(Order removedOrder)
            throws UnsupportedOperationException;

}

