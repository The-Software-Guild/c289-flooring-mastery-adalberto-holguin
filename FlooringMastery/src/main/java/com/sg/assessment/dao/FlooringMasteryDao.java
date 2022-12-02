package com.sg.assessment.dao;

import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringMasteryDao {

    void setCurrentFile(String fileName);
    /**
     * Returns a List of all Orders regardless of status.
     *
     * @return Order List containing all orders from current file.
     * @throws UnsupportedOperationException replace later
     */

    List<Order> getOrdersList()
            throws UnsupportedOperationException;


    Order addOrder(Order order);

    List<State> getStatesList();

    List<Product> getProductsList();

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
     * @throws UnsupportedOperationException replace later
     */
    static Order removeOrder(Order o)
            throws UnsupportedOperationException {
        return null;
    }

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
//
//    /**
//     * Edits customer name for the order associated with the order number.
//     *
//     * @param orderNumber order number to be associated with the order.
//     * @param newCustomerName new Customer name to replace old.
//     *
//     * @throws UnsupportedOperationException replace later
//     */
//    void editCustomerName(int orderNumber, String newCustomerName)
//            throws UnsupportedOperationException;
//
//    /**
//     * Edits state for the order associated with the order number.
//     *
//     * @param orderNumber order number to be associated with the order.
//     * @param newState new state to replace old.
//     *
//     * @throws UnsupportedOperationException replace later
//     */
//    void editState(int orderNumber, String newState)
//            throws UnsupportedOperationException;
//
//    /**
//     * Edits the product type for the order associated with the order number.
//     *
//     * @param orderNumber order number to be associated with the order.
//     * @param newProductType new product to replace old.
//     *
//     * @throws UnsupportedOperationException replace later
//     */
//    void editProductType(int orderNumber, String newProductType)
//            throws UnsupportedOperationException;
//
//    /**
//     * Edits customer name for the order associated with the order number.
//     *
//     * @param orderNumber order number to be associated with the order.
//     * @param newArea new area to replace old.
//     *
//     * @throws UnsupportedOperationException replace later
//     */
//    void editArea(int orderNumber, BigDecimal newArea)
//            throws UnsupportedOperationException;
}
