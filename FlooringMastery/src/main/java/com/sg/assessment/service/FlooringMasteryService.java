package com.sg.assessment.service;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
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
     *              a new Orders file or not
     * @throws FlooringMasteryPersistenceException if the DAO has problems reading the current Orders file
     * @throws NoOrdersOnDateException if the user is trying to access Orders on a date there aren't any
     */
    void setOrdersFile(LocalDate date, Action action) throws FlooringMasteryPersistenceException, NoOrdersOnDateException;

    /**
     * Populates the statesList and productsList with the data in the Taxes and Products files respectively.
     *
     * @throws FlooringMasteryPersistenceException if the DAO has problems reading the Taxes or Products files
     */
    void loadStatesAndProducts() throws FlooringMasteryPersistenceException;

    /**
     * Retrieves a List of orders that was populated using the Orders file for the date the user specified.
     *
     * @return the list of orders
     */
    List<Order> retrieveOrdersList();

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
     * Calls the DAO method that enters the new order data into the current Orders file.
     *
     * @param newOrder the new order to be added to the Orders file
     * @throws FlooringMasteryPersistenceException if the DAO has problems writing to the current Orders file
     */
    void enterOrder(Order newOrder) throws FlooringMasteryPersistenceException;

    /**
     * Calls the DAO method that will write the current Orders file with the information of an order the user edited.
     *
     * @throws FlooringMasteryPersistenceException if the DAO has problems writing to the current Orders file
     */
    void storeEditedOrder() throws FlooringMasteryPersistenceException;

    /**
     * Calls the DAO method that removes an order from the current Orders file.
     *
     * @param order the order to be removed
     * @throws FlooringMasteryPersistenceException if the DAO has problems writing to the current Orders file
     */
    void removeOrder(Order order) throws FlooringMasteryPersistenceException;

    /**
     * Calls the DAO method that deletes the current Orders file if there are no orders in it.
     */
    void deleteEmptyFile();
}

