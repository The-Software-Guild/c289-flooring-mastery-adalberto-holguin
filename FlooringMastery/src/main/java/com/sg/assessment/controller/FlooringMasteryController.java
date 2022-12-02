package com.sg.assessment.controller;

import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import com.sg.assessment.service.FlooringMasteryService;
import com.sg.assessment.view.FlooringMasteryView;
import com.sg.assessment.view.UserIO;
import com.sg.assessment.view.UserIOConsoleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    private UserIO io = new UserIOConsoleImpl();

    public void run() {


        boolean keepGoing = true;
        int menuSelection = 0;
        while (keepGoing) {
            /*io.print("<<Flooring Program>>");
            io.print("1. Display Orders");
            io.print("2. Add an Order");
            io.print("3. Edit an Order");
            io.print("4. Remove an Order");
            io.print("5. Export All Data");
            io.print("6. Quit");
*/
            view.printMenu();

            menuSelection = io.readInt("Please select from the above choices.", 1, 6);


            switch (menuSelection) {
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
                    exportData();
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
    }

    private void displayOrders() {


    }

    private void addOrder() {
        LocalDate orderDate = view.retrieveOrderDate(); // retrieves order date from user
        service.selectAndLoadOrdersFile(orderDate); // assigning order to correct file

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
            // message from view saying order entered
        } else {
            // message from view saying order aborted
        }
    }

    private void editOrder() {
        view.displayEditMenu();

    }

    private void removeOrder() throws UnsupportedOperationException {
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.inputDate();
        view.displayDateBanner(dateChoice);
        try {
            view.displayDateOrders(service.retrieveOrdersList(dateChoice)); // changed from getOrders to retrieveOrdersList
            int orderNumber = view.getordernumber();
            Order o = service.retrieveOrder(dateChoice, orderNumber); // changed from getOrder to retrieveOrder
            view.displayRemoveOrderBanner();
            view.displayOrders();
            String response = view.askRemove();
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
        private void exportData() {
        }
    }
}




