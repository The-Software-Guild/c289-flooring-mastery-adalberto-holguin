package com.sg.assessment.dao;

import com.sg.assessment.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryDao {
    /**
     * Returns a List of all Orders regardless of status.
     *
     * @return Order List containing all orders.
     * @throws UnsupportedOperationException
     */

    List<Order> getAllOrders()
            throws UnsupportedOperationException;

    /**
     * Returns a List of all Orders based on date.
     *
     * @return Order List containing all orders based on date.
     * @throws UnsupportedOperationException
     */
    List<Order> getOrdersByDate(LocalDate date)
            throws UnsupportedOperationException;

    /**
     * Adds an Order to the database and associates it with the given order number.
     *
     * @param orderNumber order number to be associated with Order.
     * @param order order to be placed.
     *
     * @throws UnsupportedOperationException
     */

    Order addOrder(int orderNumber, Order order)
            throws UnsupportedOperationException;

    /**
     * Retrieves an Order from the database by its order number and returns it.
     * Returns null if no such order exists.
     *
     * @param orderNumber order number to be associated with Order.
     * @param date date to be associated with the Order.
     *
     * @throws UnsupportedOperationException
     */

    Order getOrder(LocalDate date, int orderNumber)
            throws UnsupportedOperationException;

    /**
     * Removes from the roster the student associated with the given id.
     * Returns the student object that is being removed or null if
     * there is no student associated with the given id
     *
     * @param  date date of order to be removed
     * @param orderNumber number of order to be removed
     * @return Order object that was removed or null if no order
     * was associated with the given order number
     * @throws UnsupportedOperationException
     */
    Order removeOrder(LocalDate date, int orderNumber)
            throws UnsupportedOperationException;
}
