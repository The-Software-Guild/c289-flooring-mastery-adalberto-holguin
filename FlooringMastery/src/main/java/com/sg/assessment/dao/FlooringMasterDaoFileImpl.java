package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dao.exceptions.NoOrdersOnDateException;
import com.sg.assessment.dto.Action;
import com.sg.assessment.dto.Order;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.springframework.stereotype.Component;

@Component
public class FlooringMasterDaoFileImpl implements FlooringMasteryDao {

    private final String STATE_FILE;
    private final String PRODUCT_FILE;
    private final String DELIMITER = ",";
    private String currentOrdersFileName;
    private File currentOrdersFile; // Currently this is only used when checking if user aborted order to delete file if empty
    private List<Order> ordersList = new ArrayList<>();
    private List<State> statesList = new ArrayList<>();
    private List<Product> productsList = new ArrayList<>();

    public FlooringMasterDaoFileImpl() {
        STATE_FILE = ".\\data\\Taxes.txt";
        PRODUCT_FILE = ".\\data\\Products.txt";
    }

    @Override
    public void loadStatesAndProductsLists() throws FlooringMasteryPersistenceException {
        loadStateFile();
        loadProductFile();
    }

    // We will use this when adding files because we WANT TO create one if it doesn't exist
    public void setCurrentOrdersFile(String fileName, Action action) throws FlooringMasteryPersistenceException,
            NoOrdersOnDateException {
        File checkFile = new File(fileName);

        // If the user wants to add an order, and the Orders file for that date doesn't exist, creates it.
        if (action == Action.ADD) {
            if (!checkFile.exists()) {
                createNewOrdersFile(fileName); // creates file if it doesn't exist
            }
            // The currentOrdersFile variable is only used in the deleteFileIfEmpty method, so we can delete the file that is
            // created when a user chooses the option to add an order if the user decides to abort the order, so we do not
            // leave a blank Orders file in the program. The file will only be deleted if the user aborts order and there are
            // no other orders in the file.
            currentOrdersFile = new File(fileName);
        } else if (!checkFile.exists()) {
            throw new NoOrdersOnDateException("There are no orders for the specified date.");
        }

        currentOrdersFileName = fileName;
        loadOrdersFile();
    }

    // We will use this when displaying and/or orders, because we DO NOT want to create a new file if it doesn't exist
    public boolean checkFileExist(String fileName) {
        File checkFile = new File(fileName);
        return checkFile.exists();
    }

    private void createNewOrdersFile(String fileName) throws FlooringMasteryPersistenceException {
        File newFile = new File(fileName);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Error accessing orders file.");
        }
    }

    @Override
    public void deleteFileIfEmpty() throws IOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(currentOrdersFile)))) {
            scanner.nextLine();
        } catch (NoSuchElementException e) { // the created orders file is empty (user aborted order)
            currentOrdersFile.delete();
        }
    }

    public void loadfile(Date date) throws FlooringMasteryPersistenceException {
        File newFile = new File(".\\orders\\" + date);
        if (newFile.exists()) {
            loadOrdersFile();
        }
    }

    @Override
    public List<Order> getOrdersList() {
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
    public void addOrder(Order newOrder) throws FlooringMasteryPersistenceException {
        ordersList.add(newOrder);
        writeOrdersFile(ordersList);
    }

    @Override
    public Order getOrder(int orderNumber) throws FlooringMasteryPersistenceException {
        loadOrdersFile();
        return ordersList.get(orderNumber);
    }

    @Override
    public Order removeOrder(Order chosenOrder) {

        int orderNumber = chosenOrder.getOrderNumber();

        List<Order> orders = getOrdersList();
        Order removedOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);

        if (removedOrder != null) {
            orders.remove(removedOrder);
            //writeOrdersFile();
            return removedOrder;
        } else {
            return null;
        }

    }


    // Prantik
    @Override
    public void editOrder(int orderNumber, String newCustomerName, String newState,
                          String newProductType, BigDecimal newArea)
            throws UnsupportedOperationException {
//        Order orderToEdit = getOrder(orderNumber);
//        orderToEdit.setCustomerName(newCustomerName);
//        orderToEdit.setState(newState); // if we edit state we have to run the service layer's
//        orderToEdit.setProductType(newProductType);
//        orderToEdit.setArea(newArea);
//        writeOrdersFile();
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
        product.setLaborCostPerSquareFoot(new BigDecimal(productToken[2]));

        return product;
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

    //-----------------------------------
    // METHODS FOR READING/WRITING FILES
    //-----------------------------------
    private void writeOrdersFile(List<Order> orderList) throws FlooringMasteryPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(currentOrdersFileName));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Error. Could not write order data to file.");
        }

        String ordersFileHeader = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        out.println(ordersFileHeader);

        String orderAsText;
        for (Order order : orderList) {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }
        out.close();
    }

    private void loadOrdersFile() throws FlooringMasteryPersistenceException {
        // Clearing list, so it can be populated with current information as we changed Orders file or edited its contents.
        ordersList.clear();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(currentOrdersFileName)))) {
            // Getting rid of the Orders file header, so we do not add it to the ordersList
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Order currentOrder = unmarshallOrder(currentLine);
                ordersList.add(currentOrder);
            }
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load file into memory.");
        }
    }

    private void loadProductFile() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Error. Could not load product data from " + PRODUCT_FILE + " the " +
                    "file may have been moved or deleted.");
        }

        String currentLine;
        Product currentProduct;
        scanner.nextLine(); // this will take the header

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            productsList.add(currentProduct);
        }
        scanner.close();
    }

    private void loadStateFile() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(STATE_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Error. Could not load state data from " + STATE_FILE + " the file " +
                    "may have been moved or deleted.");
        }

        String currentLine;
        State currentState;
        scanner.nextLine(); // this will take the header

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            statesList.add(currentState);
        }
        scanner.close();
    }
}
