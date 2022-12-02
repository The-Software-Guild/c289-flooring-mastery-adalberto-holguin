package com.sg.assessment.view;

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


    public void printMenu() {

        io.print("* * * * * * * * * * * * * * * * * ");
        io.print("* * * * * * * * * * * * * * * * * *");
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print(" 2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("Export All Data");
        io.print("Quit");

    }

    public void displayUnknownCommandBanner() {
        io.print("Entered invalid command");
    }

    public void displayExitBanner() {
        io.print("Goodbye!!");
    }

    // Whenever we need the order date for editing/adding/removing order etc.
    public LocalDate retrieveOrderDate() {
        LocalDate orderDate;

        while (true) {
            try {
                String dateString = io.readStringNoEmpty("Enter the order date (please input in MM/DD/YYYY format):");
                orderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                break;
            } catch (DateTimeParseException e) {
                io.print("The date entered was not in MM/DD/YYYY format, please try again.");
            }
        }
        return orderDate;
    }

    // Passing in states and products list to show the user our options. Passing in ordersList to be able to set each order
    // number to ordersList.size() + 1, auto-incrementing order numbers.
    public Order retrieveOrderInformation(List<Order> ordersList, List<State> statesList, List<Product> productsList) {

        // Getting customer name
        String customerName = io.readStringNoEmpty("Enter customer name");

        // Getting state abbreviation and tax rate
        io.print("Select a state from our list:");
        for (int i = 0; i < statesList.size(); i++) {
            io.print((i + 1) + " - " + statesList.get(i).getStateName());
        }
        int stateChoice = io.readInt("Enter your choice:", 1, statesList.size());
        State state = statesList.get(stateChoice - 1); // -1 because list starts from 1 while index starts from 0
        String stateAbbreviation = state.getStateAbbreviation();
        BigDecimal taxRate = state.getTaxRate();

        // Getting product type, cost per square foot, and labor cost per square foot
        io.print("Select a product from our list:");
        for (int i = 0; i < productsList.size(); i++) {
            io.print((i + 1) + " - " + productsList.get(i).getProductType());
        }
        int productChoice = io.readInt("Enter your choice:", 1, productsList.size());
        Product product = productsList.get(productChoice - 1); // -1 because list starts from 1 while index starts from 0
        String productType = product.getProductType();
        BigDecimal costPerSquareFoot = product.getCostPerSquareFoot();
        BigDecimal laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();

        // Getting area
        BigDecimal area = io.readBigDecimal("Enter desired sq ft of product (minimum 100sq ft.)", new BigDecimal("100"),
                new BigDecimal("10000000"));

        Order order = new Order();
        order.setOrderNumber(ordersList.size() + 1);
        order.setCustomerName(customerName);
        order.setState(stateAbbreviation);
        order.setTaxRate(taxRate);
        order.setProductType(productType);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);

        // At this point the order still needs the MaterialCost, LaborCost, Tax, and Total, we will get those with service layer
        return order;
    }

    public boolean confirmOrder(Order order) {
        io.print("\nOrder Review");
        io.print("\nOrder Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea() + " sq. ft.");
        io.print("Material Cost: " + order.getMaterialCost());
        io.print("Labor Cost: " + order.getLaborCost());
        io.print("Tax: " + order.getTax());
        io.print("Total: " + order.getTotal());

        int userChoice = io.readInt("\nConfirm order placement     1) YES     2) NO", 1, 2);
        if (userChoice == 1) {
            return true;
        } else {
            return false;
        }
    }

    //editorder //removeorder
    public String getdate() {
        String date = io.readString("Enter the order date");
        return date;

    }

    public int getordernumber() {
        int orderNumber = io.readInt("Enter the order number");
        return orderNumber;

    }

    public void displayEditMenu() {

    }

    public LocalDate inputDate() {

        while (true) {
            try {
                String dateString = io.readStringNoEmpty("Enter date to search orders (please input in MM/DD/YYYY format):");
                LocalDate ordersDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                return ordersDate;
            } catch (DateTimeParseException e) {
                io.print("The date entered was not in MM/DD/YYYY format, please try again.");
            }
        }
    }

    public void displayRemoveOrderBanner() {
        io.print("=== REMOVE ORDER ===");
    }

    public String askRemove() {
        return io.readString("Would you like to remove this order? Y/N", 1);
    }

    public void displayRemoverOrderSuccess(boolean success, Order o) {
        if (success == true) {
            io.print("Order #" + o.getOrderNumber() + " was successfully removed!");
        } else {
            io.print("Order was not removed.");
        }
    }

    public void displayDateBanner(LocalDate dateChoice) {
        System.out.printf("=== Orders on %s ===/n", dateChoice);
    }

    public void displayDateOrders(List<Order> ordersByDate) {
        for (Order o : ordersByDate) {
            io.print(o.getOrderNumber() + " | " + o.getCustomerName() + " | " + io.formatCurrency(o.getTotal()));
        }

        }
        public void displayErrorMessage(String errorMsg) {
            io.print("=== ERROR ===");
            io.print(errorMsg);
        }
    }

