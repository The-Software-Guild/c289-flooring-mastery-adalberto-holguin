package com.sg.assessment.service;

import com.sg.assessment.service.exceptions.InvalidDateException;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import com.sg.assessment.service.exceptions.NoSuchOrderException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface FlooringMasteryService {

    /**
     * Calculates the material and labor costs, as well as the tax and total of the order.
     *
     * @param order the order we are calculating prices for
     * @return the order with all price properties added to it
     */
    Order calculatePrices(Order order);

    /**
     * Sets the name of the Orders file we should use depending on the date passed in by the user.
     *
     * @param date   the date passed in by the user
     * @param action the action being taken by the user (DISPLAY, ADD, EDIT, or REMOVE); depending on this, the DAO will create
     *               a new Orders file or not
     * @throws FlooringMasteryPersistenceException if the DAO has problems reading the current Orders file
     * @throws NoOrdersOnDateException             if the user is trying to access Orders on a date there aren't any
     */
    void setOrdersFile(LocalDate date, Action action) throws FlooringMasteryPersistenceException, NoOrdersOnDateException;

    /**
     * Validates that the user's chosen date is a date in the future when adding orders.
     *
     * @param date the date specified by the user
     */
    void validateDate(LocalDate date) throws InvalidDateException;

    /**
     * Populates the statesList and productsList with the data in the Taxes and Products files respectively.
     *
     * @throws FlooringMasteryPersistenceException if the DAO has problems reading the Taxes or Products files
     */
    void loadStatesAndProducts() throws FlooringMasteryPersistenceException;

    /**
     * Retrieves a List of orders that was populated using the Orders file for the date the user specified.
     *
     * @param action the action the user is taking
     * @return the list of Orders for the specified date
     * @throws NoOrdersOnDateException if there are no orders for the specified date (the order's list size is 0) and the
     *                                 action being taken is not ADDING; if the action is ADDING, we do not throw an exception
     *                                 even if there are no orders for the specified date, as the user should then be able to
     *                                 add new orders for that date
     */
    List<Order> retrieveOrdersList(Action action) throws NoOrdersOnDateException;

    /**
     * Retrieves a List with State data that was populated using the Taxes file.
     *
     * @return the list of states
     */
    List<State> retrieveStatesList();

    /**
     * Retrieves a List with product data that was populated using the Products file.
     *
     * @return the list of products
     */
    List<Product> retrieveProductsList();

    /**
     * Retrieves an order with the specified order number.
     *
     * @param orderNumber the order number of the order we want to retrieve
     * @return the order with the specified order number
     * @throws NoSuchOrderException if no order is found with the specified order number
     */
    Order retrieveOrder(int orderNumber) throws NoSuchOrderException;

    /**
     * Writes new order to the corresponding Orders file and writes an audit entry of the operation.
     *
     * @param newOrder the new order to be added to the Orders file
     * @param date     the date the order is being placed
     * @throws FlooringMasteryPersistenceException if the DAO or auditDAO have problems writing to the Orders or Audit files.
     */
    void enterOrder(Order newOrder, LocalDate date) throws FlooringMasteryPersistenceException;

    /**
     * Writes edited order to the corresponding Orders file and writes an audit entry of the operation.
     *
     * @param order the edited order
     * @param date  the date of the edited order
     * @throws FlooringMasteryPersistenceException if the DAO has problems writing to the current Orders file
     */
    void storeEditedOrder(Order order, LocalDate date) throws FlooringMasteryPersistenceException;

    /**
     * Removes an order from the current Orders file and writes an audit entry of the operation.
     *
     * @param order the order to be removed
     * @param date  the date of the removed order
     * @throws FlooringMasteryPersistenceException if the DAO has problems writing to the current Orders file
     */
    void removeOrder(Order order, LocalDate date) throws FlooringMasteryPersistenceException;

    /**
     * Calls the DAO method that deletes the current Orders file if there are no orders in it.
     */
    void deleteEmptyFile();

    //test
    void exportData() throws FlooringMasteryPersistenceException, NoOrdersOnDateException;
}

