package com.sg.assessment.view;

import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sg.assessment.dto.Order;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class FlooringMasteryView {

    private UserIO io;

    @Autowired
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    //----------------------------------------------
    // METHODS TO GET/RETRIEVE INFORMATION FROM USER
    //----------------------------------------------
    public int retrieveMainMenuSelection() {
        return io.readInt("\nPlease choose an option from the menu: ", 1, 6);
    }

    // Whenever we need the order date for editing/adding/removing order etc.
    public LocalDate retrieveOrderDate() {
        while (true) {
            try {
                String dateString = io.readStringNoEmpty("\nEnter the order date (please input in MM/DD/YYYY format):");
                LocalDate orderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                return orderDate;
            } catch (DateTimeParseException e) {
                io.print("The date entered was not in MM/DD/YYYY format, please try again.");
            }
        }
    }

    //editorder //removeorder
    public String getdate() {
        String date = io.readString("Enter the order date");
        return date;

    }

    public Order retrieveOrder(List<Order> ordersList, Action action) {
        int orderNumber = 0;
        switch (action) {
            case EDIT:
                orderNumber = io.readInt("\nEnter the number of the order you wish to edit:", 1, ordersList.size());
                break;
            case REMOVE:
                orderNumber = io.readInt("\nEnter the number of the order you wish to remove:", 1, ordersList.size());
                break;
        }
        // Look for the order with that number, if none match, return null
        for (Order order : ordersList) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    public String askRemove() {
        return io.readString("\nWould you like to remove this order? Y/N", 1);
    }

    //-------------------------------------------------
    // METHODS FOR DISPLAYING INFORMATION/MENUS TO USER
    //-------------------------------------------------
    public void printMainMenu() {
        io.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void displayOrders(List<Order> orderList) {
        for (Order order : orderList) {
            io.print("Order " + order.getOrderNumber() + ": " + order.getCustomerName());
            displayDivider();
            io.print("State: " + order.getState());
            io.print("Product Type: " + order.getProductType());
            io.print("Area: " + order.getArea() + " sq. ft.");
            io.print("Material Cost: $" + order.getMaterialCost());
            io.print("Labor Cost: $" + order.getLaborCost());
            io.print("Tax: $" + order.getTax());
            io.print("Total: $" + order.getTotal() + "\n");
        }
    }

    public boolean confirmOrderInformation(Order order) {
        io.print("\nOrder Review");
        io.print("\nOrder Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea() + " sq. ft.");
        io.print("Material Cost: $" + order.getMaterialCost());
        io.print("Labor Cost: $" + order.getLaborCost());
        io.print("Tax: $" + order.getTax());
        io.print("Total: $" + order.getTotal());

        int userChoice = io.readInt("\nConfirm order information     1) YES     2) NO", 1, 2);
        if (userChoice == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void displayDivider() {
        io.print("-------------------");
    }

    //----------------------------------------
    // METHODS FOR DISPLAYING MESSAGES/BANNERS
    //----------------------------------------
    public void displayErrorMessage(String errorMsg) throws InterruptedException {
        io.print("\n" + errorMsg);
        Thread.sleep(1500);
    }

    public void displayWelcomeBanner() {
        io.print("\n~*~*~*~*~*~* WELCOME TO THE FLOORING MASTERY PROGRAM *~*~*~*~*~*~");
    }

    public void displayExitBanner() {
        io.print("\nThank you for using the Flooring Mastery Program! Goodbye!!");
    }

    public void displayNoSuchOrderMessage() throws InterruptedException {
        io.print("\nNo order exists with that order number for the selected date.");
        Thread.sleep(1500);
    }

    public void displayNoEditDoneMessage() throws InterruptedException {
        io.print("None of the order's information was changed.");
        Thread.sleep(1500);
    }

    public void displayRemoverOrderSuccess(boolean success, Order o) {
        if (success == true) {
            io.print("Order #" + o.getOrderNumber() + " was successfully removed!");
        } else {
            io.print("Order was not removed.");
        }
    }

    public void displayViewAllOrdersBanner(LocalDate currentDate) {
        io.print("\n===== ORDERS FOR " + currentDate + " =====");
    }

    public void displayAddOrderBanner() {
        io.print("\n================ ADD ORDER ================");
    }

    public void displayAddOrderSuccessBanner(Order order) throws InterruptedException {
        io.print("\nOrder added successfully. Order number: " + order.getOrderNumber());
        Thread.sleep(1500);
    }

    public void displayOrderCanceledBanner() throws InterruptedException {
        io.print("\nOrder has been cancelled.");
        Thread.sleep(1500);
    }

    public void displayEditOrderBanner() {
        io.print("\n================ EDIT ORDER ================");
    }

    public void displayEditOrderSuccessBanner() throws InterruptedException {
        io.print("\nOrder edited successfully.");
        Thread.sleep(1500);
    }

    public void displayCancelEditBanner() throws InterruptedException {
        io.print("\nOrder editing cancelled.");
        Thread.sleep(1500);
    }

    public void displayRemoveOrderBanner() {
        io.print("\n================ REMOVE ORDER ================");
    }

    public void displayDateBanner(LocalDate dateChoice) {
        System.out.printf("===== Orders on %s =====/n", dateChoice);
    }

    public void displayChosenOrder(Order o) {
        if (o != null) {
            io.print(o.getProductType());
        }
    }

    public Order retrieveOrderInformation(List<Order> ordersList, List<State> statesList, List<Product> productsList,
                                          Action action, Order order) {
        int stateChoice; // will get index from statesList to select State
        int productChoice; // will get index from productsList to select Product

        switch (action) {
            case ADD:
                // Setting order number.
                order.setOrderNumber(ordersList.size() + 1);

                // Getting customer name.
                String customerName = io.readNames("Enter customer name");

                // Getting state abbreviation and tax rate.
                io.print("Select a state from our list:");
                for (int i = 0; i < statesList.size(); i++) {
                    io.print((i + 1) + " - " + statesList.get(i).getStateName());
                }
                stateChoice = io.readInt("Enter your choice:", 1, statesList.size());
                State state = statesList.get(stateChoice - 1); // -1 because list starts from 1 while index starts from 0
                String stateAbbreviation = state.getStateAbbreviation();
                BigDecimal taxRate = state.getTaxRate();

                // Getting product type, cost per square foot, and labor cost per square foot
                io.print("Select a product from our list:");
                for (int i = 0; i < productsList.size(); i++) {
                    io.print((i + 1) + " - " + productsList.get(i).getProductType());
                }
                productChoice = io.readInt("Enter your choice:", 1, productsList.size());
                Product product = productsList.get(productChoice - 1); // -1 because list starts from 1 while index starts from 0
                String productType = product.getProductType();
                BigDecimal costPerSquareFoot = product.getCostPerSquareFoot();
                BigDecimal laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();

                // Getting area
                BigDecimal area = io.readBigDecimal("Enter desired sq. ft. of product (minimum 100sq ft.):", new BigDecimal(
                        "100"), new BigDecimal("10000000"));

                order.setCustomerName(customerName);
                order.setState(stateAbbreviation);
                order.setTaxRate(taxRate);
                order.setProductType(productType);
                order.setArea(area);
                order.setCostPerSquareFoot(costPerSquareFoot);
                order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
                return order;
            case EDIT:
                // Changing customer name if user entered new customer name information.
                io.print("\nEnter new data to edit, or leave blank and press enter to leave intact:");
                String newCustomerName = io.readNamesAllowEmpty("Enter customer name (" + order.getCustomerName() + "):");
                if (!newCustomerName.equals("")) {
                    order.setCustomerName(newCustomerName);
                }

                // Changing state abbreviation and tax rate if user entered new state information.
                io.print("Select a state from our list:");
                for (int i = 0; i < statesList.size(); i++) {
                    io.print((i + 1) + " - " + statesList.get(i).getStateName());
                }
                stateChoice = io.readIntAllowEmpty("Enter state (" + order.getState() + "):", 1, statesList.size());
                if (stateChoice != 0) { // If user changed state
                    state = statesList.get(stateChoice - 1); // -1 because list starts from 1 while index starts from 0
                    order.setState(state.getStateAbbreviation());
                    order.setTaxRate(state.getTaxRate());
                }

                // Changing product type, cost per sq. ft., and labor cost per sq. ft. if user entered new product information.
                io.print("Select a product from our list:");
                for (int i = 0; i < productsList.size(); i++) {
                    io.print((i + 1) + " - " + productsList.get(i).getProductType());
                }
                productChoice = io.readIntAllowEmpty("Enter product type (" + order.getProductType() + "):", 1,
                        productsList.size());
                if (productChoice != 0) {
                    product = productsList.get(productChoice - 1); // -1 because list starts from 1 while index starts from 0
                    order.setProductType(product.getProductType());
                    order.setCostPerSquareFoot(product.getCostPerSquareFoot());
                    order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
                }

                // Changing area if user entered new area information.
                BigDecimal newArea = io.readBigDecimalAllowEmpty("Enter area (" + order.getArea() + "):", new BigDecimal("100")
                        , new BigDecimal("10000000"));
                if (newArea != null) {
                    order.setArea(newArea);
                }

                break;
        }
        return order;
    }
}

