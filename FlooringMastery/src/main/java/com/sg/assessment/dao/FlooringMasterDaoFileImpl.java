package com.sg.assessment.dao;

import com.sg.assessment.dto.Order;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.stereotype.Component;

@Component
public class FlooringMasterDaoFileImpl implements FlooringMasteryDao {

    private String CURRENT_ORDERS;
    private String STATE_FILE;
    private String PRODUCT_FILE;
    public static final String DELIMITER = ",";

    private List<Order> ordersList = new ArrayList<>();
    private List<State> statesList = new ArrayList<>();
    private List<Product> productsList = new ArrayList<>();

    public FlooringMasterDaoFileImpl() {
        CURRENT_ORDERS = "default.txt";
        STATE_FILE = "Taxes.txt";
        PRODUCT_FILE = "Products.txt";
    }

    public FlooringMasterDaoFileImpl(String currentTextFile) {
        CURRENT_ORDERS = currentTextFile;
    }

    //adding setter to be able to switch between different files
    //file param will have date in the file name
    public void setCurrentFile(String fileName) {
        File checkFile = new File(".\\orders\\" + fileName);
        if (checkFile.exists()) {
            CURRENT_ORDERS = fileName;
        } else {
            createNewOrdersFile(fileName);
            CURRENT_ORDERS = fileName;
        }
        loadOrdersFile();
    }

    private File createNewOrdersFile(String fileName) {
        File newFile = new File(".\\orders\\" + fileName);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    //check by date?
    //@Selena date will be set through the service method .setOrdersFile
    @Override
    public List<Order> getOrdersList() throws UnsupportedOperationException {
        return ordersList;
    }

    @Override
    public List<State> getStatesList() {
        return statesList;
    }

    @Override
    public List<Product> getProductsList() {
        return productsList;
    }

    @Override
    public Order addOrder(Order newOrder) throws UnsupportedOperationException {
//        loadOrdersFile();
//        //get previous order for order number
//        Order previousOrder = ordersList.get(ordersList.size() - 1);
//        int prevOrderNumber = previousOrder.getOrderNumber();
//
//        //creating a new order from required props
//        Order newOrder = new Order(prevOrderNumber + 1, customerName, state, productType, area);
        ordersList.add(newOrder);
        return newOrder;
    }

    @Override
    public Order getOrder(int orderNumber) throws UnsupportedOperationException {
        loadOrdersFile();
        return ordersList.get(orderNumber);
    }

    @Override
    public Order removeOrder(Order chosenOrder) throws UnsupportedOperationException {

        int orderNumber = chosenOrder.getOrderNumber();

        List<Order> order = loadOrders(chosenOrder.getDate());
        Order removedOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (removedOrder != null) {
            order.remove(removedOrder);
            writeOrders(orders, chosenOrder.getDate());
            return removedOrder;
        } else {
            return null;
        }
    }

    @Override
    public void editOrder(int orderNumber, String newCustomerName, String newState,
                          String newProductType, BigDecimal newArea)
            throws UnsupportedOperationException {
        Order orderToEdit = getOrder(orderNumber);
        orderToEdit.setCustomerName(newCustomerName);
        orderToEdit.setState(newState);
        orderToEdit.setProductType(newProductType);
        orderToEdit.setArea(newArea);
        writeOrdersFile();
    }

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);
        Order currentOrder = new Order();
        currentOrder.setOrderNumber(Integer.parseInt(orderTokens[0]));
        currentOrder.setCustomerName(orderTokens[1]);
        currentOrder.setState(orderTokens[2]);
        currentOrder.setTaxRate(new BigDecimal(orderTokens[3]));
        currentOrder.setProductType(orderTokens[4]);
        currentOrder.setArea(new BigDecimal(orderTokens[5]));
        currentOrder.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        currentOrder.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        currentOrder.setMaterialCost(new BigDecimal(orderTokens[8]));
        currentOrder.setLaborCost(new BigDecimal(orderTokens[9]));
        currentOrder.setTax(new BigDecimal(orderTokens[10]));
        currentOrder.setTotal(new BigDecimal(orderTokens[11]));

        return currentOrder;
    }

    private State unmarshallState(String stateAsText) {
        String[] stateTokens = stateAsText.split(DELIMITER);
        State currentState = new State();
        currentState.setStateAbbreviation(stateTokens[0]);
        currentState.setStateName(stateTokens[1]);
        currentState.setTaxRate(new BigDecimal(stateTokens[2]));

        return currentState;
    }

    private Product unmarshallProduct(String productText) {
        String[] productToken = productText.split(DELIMITER);
        Product product = new Product();
        product.setProductType(productToken[0]);
        product.setCostPerSquareFoot(new BigDecimal(productToken[1]));
        product.setCostPerSquareFoot(new BigDecimal(productToken[2]));

        return product;
    }

    private void loadOrdersFile() throws UnsupportedOperationException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(CURRENT_ORDERS)));
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not load file into memory.");
        }

        String currentLine;
        Order currentOrder;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            ordersList.add(currentOrder);
        }
        scanner.close();
    }

    private void loadStateFile() {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(STATE_FILE)));
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not load file into memory.");
        }

        String currentLine;
        State currentState;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            statesList.add(currentState);
        }
        scanner.close();
    }

    private void loadProductFile() throws UnsupportedOperationException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not load file into memory.");
        }

        String currentLine;
        Product currentProduct;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            productsList.add(currentProduct);
        }
        scanner.close();
    }

    private String marshallOrder(Order order) {
        String orderAsText = order.getOrderNumber() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        orderAsText += order.getTaxRate() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();

        return orderAsText;
    }

    private void writeOrdersFile() throws UnsupportedOperationException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(CURRENT_ORDERS));
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }

        String orderAsText;
        List<Order> orderList = this.getAllOrders();
        for (Order order : orderList) {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }

        out.close();
    }

}
