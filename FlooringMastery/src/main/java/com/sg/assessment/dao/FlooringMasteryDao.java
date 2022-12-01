package com.sg.assessment.dao;

import com.sg.assessment.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryDao {

    /**
     * Returns a List of all Orders regardless of status.
     *
     * @return Order List containing all orders from current file.
     * @throws UnsupportedOperationException replace later
     */

    List<Order> getAllOrders()
            throws UnsupportedOperationException;


    /**
     * Adds an Order to the file and associates it with the given order number.
     *
     * @param orderNumber order number to be associated with Order.
     * @param order order to be placed.
     *
     * @throws UnsupportedOperationException replace later
     */

    Order addOrder(LocalDate date, String customerName, String state, String productType, BigDecimal area)
            throws UnsupportedOperationException;

    /**
     * Retrieves an Order from the file by its order number and returns it.
     * Returns null if no such order exists.
     *
     * @param orderNumber order number to be associated with Order.
     *
     * @throws UnsupportedOperationException replace later
     */

    Order getOrder(int orderNumber)
            throws UnsupportedOperationException;

    /**
     * Removes the order associated with the order number.
     * Returns null if no such order exists.
     *
     * @param orderNumber order number to be associated with the order.
     *
     * @throws UnsupportedOperationException replace later
     */
    Order removeOrder(int orderNumber)
            throws UnsupportedOperationException;

    /**
     * Edits properties for the order associated with the order number.
     *
     * @param orderNumber order number to be associated with the order.
     *
     * @throws UnsupportedOperationException replace later
     */
    void editOrder(int orderNumber, String newCustomerName, String newState,
                   String newProductType, BigDecimal area)
            throws UnsupportedOperationException;
}
