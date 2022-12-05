package com.sg.assessment.controller;

import com.sg.assessment.controller.exceptions.InvalidDateException;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import com.sg.assessment.service.FlooringMasteryService;
import com.sg.assessment.view.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FlooringMasteryController {

    private FlooringMasteryService service;
    private FlooringMasteryView view;

    @Autowired
    public FlooringMasteryController(FlooringMasteryService service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    public void run() throws InterruptedException {
        boolean isInitialized = false;
        try {
            initializeProgram();
            view.displayWelcomeBanner();
            isInitialized = true;
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }

        int mainMenuSelection = 0;
        while (isInitialized) {
            view.printMainMenu();
            mainMenuSelection = view.retrieveMainMenuSelection();

            try {
                switch (mainMenuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        //exportData();
                        break;
                    case 6:
                        isInitialized = false;
                        break;
                }
            } catch (NoOrdersOnDateException | InvalidDateException e) {
                view.displayErrorMessage(e.getMessage());
            } catch (FlooringMasteryPersistenceException | IOException e) {
                view.displayErrorMessage(e.getMessage());
                isInitialized = false;
            }
        }
        view.displayExitBanner();
    }

    private void initializeProgram() throws FlooringMasteryPersistenceException {
        service.loadStatesAndProducts();
    }

    private void displayOrders() throws FlooringMasteryPersistenceException, NoOrdersOnDateException {
        LocalDate orderDate = view.retrieveOrderDate();
        service.selectAndLoadOrders(orderDate, Action.DISPLAY);
        List<Order> ordersList = service.retrieveOrdersList();
        if (ordersList.size() == 0) {
            throw new NoOrdersOnDateException("There are no orders for the specified date.");
        }
        view.displayViewAllOrdersBanner(orderDate);
        view.displayOrders(ordersList);
    }

    // Adalberto
    private void addOrder() throws InvalidDateException, FlooringMasteryPersistenceException, InterruptedException,
            NoOrdersOnDateException, IOException {
        view.displayAddOrderBanner();
        LocalDate orderDate = view.retrieveOrderDate();

        if (orderDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Error, invalid date. New orders cannot be added to past dates.");
        } else {
            service.selectAndLoadOrders(orderDate, Action.ADD); // assigning order to correct file, creates if does not exist
        }

        List<Order> ordersList = service.retrieveOrdersList(); // so we can assign correct order number using ordersList.size()
        List<State> statesList = service.retrieveStatesList();
        List<Product> productsList = service.retrieveProductsList();
        Order newOrder = new Order();
        newOrder = view.retrieveOrderInformation(ordersList, statesList, productsList, Action.ADD, newOrder);

        // Calculates order's material cost, labor cost, tax, and total.
        service.calculatePrices(newOrder);

        // Confirming user order.
        boolean informationIsConfirmed = view.confirmAction(newOrder, Action.ADD);
        if (informationIsConfirmed) {
            service.enterOrder(newOrder);
            view.displayAddOrderSuccessBanner(newOrder);
        } else {
            // Must delete created file if user aborted order.
            service.checkFileIsEmpty();
            view.displayOrderCanceledBanner();
        }
    }

    private void editOrder() throws NoOrdersOnDateException, FlooringMasteryPersistenceException, InterruptedException {
        view.displayEditOrderBanner();
        LocalDate date = view.retrieveOrderDate();
        service.selectAndLoadOrders(date, Action.EDIT); // will exit method if date does not exist

        List<Order> ordersList = service.retrieveOrdersList();
        if (ordersList.size() == 0) {
            throw new NoOrdersOnDateException("There are no orders for the specified date.");
        }
        List<State> statesList = service.retrieveStatesList();
        List<Product> productsList = service.retrieveProductsList();
        Order orderToEdit = view.retrieveOrder(ordersList, Action.EDIT);

        if (orderToEdit == null) {
            view.displayNoSuchOrderMessage();
        } else {
            // Creating an order with orderToEdit's information before it's edited, so we can compare and see if order was edited.
            Order orderToCompare = new Order(orderToEdit.getOrderNumber(), orderToEdit.getCustomerName(),
                    orderToEdit.getState(), orderToEdit.getTaxRate(), orderToEdit.getProductType(), orderToEdit.getArea(),
                    orderToEdit.getCostPerSquareFoot(), orderToEdit.getLaborCostPerSquareFoot(), orderToEdit.getMaterialCost(),
                    orderToEdit.getLaborCost(), orderToEdit.getTax(), orderToEdit.getTotal());
            orderToEdit = view.retrieveOrderInformation(ordersList, statesList, productsList, Action.EDIT, orderToEdit);

            if (orderToEdit.equals(orderToCompare)) {
                view.displayNoEditDoneMessage();
            } else {
                // Calculates order's material cost, labor cost, tax, and total.
                service.calculatePrices(orderToEdit);

                boolean informationIsConfirmed = view.confirmAction(orderToEdit, Action.EDIT);
                if (informationIsConfirmed) {
                    service.storeEditedOrder();
                    view.displayEditOrderSuccessBanner();
                } else {
                    view.displayCancelEditBanner();
                }
            }
        }
    }

    private void removeOrder() throws FlooringMasteryPersistenceException, NoOrdersOnDateException, InterruptedException {
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.retrieveOrderDate();
        service.selectAndLoadOrders(dateChoice, Action.REMOVE);

        List<Order> ordersList = service.retrieveOrdersList();
        if (ordersList.size() == 0) {
            throw new NoOrdersOnDateException("There are no orders for the specified date.");
        }

        Order orderToRemove = view.retrieveOrder(ordersList, Action.REMOVE);
        if (orderToRemove == null) {
            view.displayNoSuchOrderMessage();
        } else {
            boolean deletionIsConfirmed = view.confirmAction(orderToRemove, Action.REMOVE);

            if (deletionIsConfirmed) {
                service.removeOrder(orderToRemove);
                view.displayRemoveOrderSuccessBanner();
            } else {
                view.displayCancelRemoveBanner();
            }
        }
    }
}




