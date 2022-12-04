package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface FlooringMasteryDao {

    /**
     * Reads the Products and Taxes files when the program starts up to populate the productsList and statesLists.
     *
     * @throws FlooringMasteryPersistenceException if it cannot read the files
     */
    void loadStatesAndProductsLists() throws FlooringMasteryPersistenceException;

    /**
     * Sets the correct name of file to use for the orders, as well as populate the ordersList with orders from that file, if
     * it contains any.
     * If the user is adding orders for a particular date, and that date does not exist, it creates a file for them. This
     * action only occurs when the user is adding orders, and will not occur if the user is displaying, editing, or removing.
     *
     * @param fileName the name of the file we want to use for the orders
     * @param action   the action the user is taking (adding, editing, deleting, or removing order)
     * @throws FlooringMasteryPersistenceException if we cannot access the storage the user is specifying
     * @throws NoOrdersOnDateException             if the user is trying to display orders from a particular storage, but
     */
    void setCurrentOrdersFile(String fileName, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException;

    /**
     * Returns a List of all Orders.
     *
     * @return Order List containing all orders from current Orders file.
     */
    List<Order> getOrdersList();

    /**
     * Returns a List of all States.
     *
     * @return State List containing all state data from the Taxes file.
     */
    List<State> getStatesList();

    /**
     * Returns a List of all Products.
     *
     * @return Product List containing all product data from the Products file.
     */
    List<Product> getProductsList();

    void loadfile(Date date) throws FlooringMasteryPersistenceException;

    /**
     * Adds a new order to the ordersList as well the current Orders file.
     *
     * @param order the order to be added
     * @throws FlooringMasteryPersistenceException if there is a problem writing to the desired Orders file.
     */
    void addOrder(Order order) throws FlooringMasteryPersistenceException;

    /**
     * Deletes the current Orders file if the user created a new file to add an order, but then decided to abort the order.
     * Also deletes the current Orders file in the case that the user has decided to delete all orders in the file.
     * @throws IOException if there is a problem accessing the desired Orders file.
     */
    void deleteFileIfEmpty() throws IOException;

    /**
     * Retrieves an Order from the file by its order number and returns it.
     * Returns null if no such order exists.
     *
     * @param orderNumber order number to be associated with Order.
     * @throws UnsupportedOperationException replace later
     */

    Order getOrder(int orderNumber)
            throws UnsupportedOperationException, FlooringMasteryPersistenceException;

    /**
     * Removes the order associated with the order number.
     * Returns null if no such order exists.
     *
     * @param o order number to be associated with the order.
     * @throws UnsupportedOperationException replace later
     */
    Order removeOrder(Order o);

//    /**
//     * Edits properties for the order associated with the order number.
//     *
//     * @param orderNumber order number to be associated with the order.
//     * @throws UnsupportedOperationException replace later
//     */
//    void editOrder(int orderNumber, String newCustomerName, String newState,
//                   String newProductType, BigDecimal area)
//            throws UnsupportedOperationException;

    void editOrder() throws FlooringMasteryPersistenceException;

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
