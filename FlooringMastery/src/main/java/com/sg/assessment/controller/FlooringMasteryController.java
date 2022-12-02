package com.sg.assessment.controller;

import com.sg.assessment.controller.exceptions.InvalidDateException;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import com.sg.assessment.service.FlooringMasteryService;
import com.sg.assessment.view.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
            view.displayWelcomeMessage();
            isInitialized = true;
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }

        int mainMenuSelection = 0;
        while (isInitialized) {
            view.printMainMenu();
            mainMenuSelection = view.retrieveMainMenuSelection();

            switch (mainMenuSelection) {
                case 1:
                    try {
                        displayOrders();
                    } catch (FlooringMasteryPersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                        isInitialized = false; // must quit program as this means we could not read order file
                    }
                    break;
                case 2:
                    try {
                        addOrder();
                    } catch (InvalidDateException e) {
                        view.displayErrorMessage(e.getMessage());
                    } catch (FlooringMasteryPersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                        isInitialized = false; // must quit program as this means a problem creating or writing to order file.
                    }
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportData();
                    break;
                case 6:
                    isInitialized = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        view.displayExitBanner();
    }

    private void initializeProgram() throws FlooringMasteryPersistenceException {
        service.loadStatesAndProducts();
    }

    private void displayOrders() throws FlooringMasteryPersistenceException {
        LocalDate orderDate = view.retrieveOrderDate();
        service.selectAndLoadOrdersFile(orderDate);
        List<Order> ordersList = service.retrieveOrdersList();
        view.displayViewAllBanner(orderDate);
        view.displayOrders(ordersList);
    }

    // Adalberto
    private void addOrder() throws InvalidDateException, FlooringMasteryPersistenceException {
        LocalDate orderDate = view.retrieveOrderDate(); // retrieves order date from user

        if (orderDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Invalid date. Your order date must be in the future.");
        } else {
            service.selectAndLoadOrdersFile(orderDate); // assigning order to correct file
        }

        List<Order> ordersList = service.retrieveOrdersList(); // so we can choose correct order number for new order
        List<State> statesList = service.retrieveStatesList(); // so we can show the user our states
        List<Product> productsList = service.retrieveProductsList(); // so we can show the user our products
        Order newOrder = view.retrieveOrderInformation(ordersList, statesList, productsList); // retrieves order info from user

        // Calculates order's material cost, labor cost, tax, and total
        service.calculatePrices(newOrder);

        // Confirming user order
        boolean orderIsConfirmed = view.confirmOrder(newOrder);
        if (orderIsConfirmed) {
            service.enterOrder(newOrder);
            view.displayAddOrderSuccessBanner();
        } else {
            view.displayOrderCanceledBanner();
        }
    }

    // Prantik
    private void editOrder() throws Exception {
        LocalDate date = view.retrieveOrderDate();
        boolean fileExists = service.FileExist(date);

        if (fileExists) {
            try {
                service.selectAndLoadOrdersFile(date);
                List<Order> ordersList = service.retrieveOrdersList();

                view.retrieveOrderToEdit(ordersList);
                // show the fields ..and edit
                //
                // order.setName()
            } catch (FlooringMasteryPersistenceException e) {
                throw new RuntimeException(e);

            }

        }


        // check that order numbers... handle if exists and if not...

        // ask what they wanna edit
        // FORMAT OF MENU
        // Enter customer name (Ada Lovelace) (or press enter to leave the same):
        // Enter state...
        // etc...

        // if input.equals("") leave information the same
    }

    private void removeOrder() throws UnsupportedOperationException {
        // Patrick
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.inputDate(); // getting order date
        view.displayDateBanner(dateChoice); // displayChosenOrder
        try {
            int orderNumber = view.getOrderNumber(); //getting order number
            Order o = service.retrieveOrder(dateChoice, orderNumber); // changed from getOrder to retrieveOrder
            view.displayChosenOrder(o); // can be deleted, should only display chosen order
            view.displayRemoveOrderBanner(); // shows that the order is being removed
            String response = view.askRemove(); // confirms if the user wants to remove the order
            if (response.equalsIgnoreCase("Y")) {
                service.removeOrder(o);
                view.displayRemoverOrderSuccess(true, o);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayRemoverOrderSuccess(false, o);
            } else {
                unknownCommand();
            }
        } catch (UnsupportedOperationException e) {
            view.displayErrorMessage();
        }
        private void exportData () {
        }
    }
}




