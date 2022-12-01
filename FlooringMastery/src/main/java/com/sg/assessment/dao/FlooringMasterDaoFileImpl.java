package com.sg.assessment.dao;

<<<<<<< HEAD
import com.sg.assessment.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

=======
import org.springframework.stereotype.Component;

@Component
>>>>>>> main
public class FlooringMasterDaoFileImpl implements FlooringMasteryDao {

    private final String CURRENT_FILE;
    public static final String DELIMITER = ",";
    private  Map<Integer, Order> orders = new HashMap<>();

    public FlooringMasterDaoFileImpl() {
        CURRENT_FILE = "default.txt";
    }

    public FlooringMasterDaoFileImpl(String currentTextFile) {
        CURRENT_FILE = currentTextFile;
    }

    //factory method
    public FlooringMasterDaoFileImpl basedOnDate(LocalDate date) {
        //if file already exists, don't create new file
        //if file does not exist, create a new order file under Orders folder
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddYYY");
        String fileName = "Order_" + date.format(formatter) + ".txt";

        //create a file with fileName

        //instantiate a dao with that new file
        FlooringMasterDaoFileImpl dao = new FlooringMasterDaoFileImpl();
        return dao;
    }

    //check by date?
    @Override
    public List<Order> getAllOrders() throws UnsupportedOperationException {
        loadFile();
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order addOrder(LocalDate date, String customerName, String state, String productType, BigDecimal area) throws UnsupportedOperationException {
        loadFile();
        //set a date
        Order newOrder = new Order(customerName, state, productType, area);
        return newOrder;
    }


    @Override
    public Order getOrder(int orderNumber) throws UnsupportedOperationException {
        loadFile();
        return orders.get(orderNumber);
    }

    @Override
    public Order removeOrder(int orderNumber) throws UnsupportedOperationException {
        loadFile();
        Order removedOrder = orders.remove(orderNumber);
        writeFile();
        return removedOrder;
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
        writeFile();
    }
//
//    private File getFile(LocalDate date) {
//        return new File("");
//    }
//
//    private File createFile(LocalDate date) {
//        return new File("");
//    }

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

    private void loadFile() throws UnsupportedOperationException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(CURRENT_FILE)));
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not load file into memory.");
        }

        String currentLine;
        Order currentOrder;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.put(currentOrder.getOrderNumber(), currentOrder);
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

    private void writeFile() throws UnsupportedOperationException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(CURRENT_FILE));
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }

        String orderAsText;
        List<Order> orderList = this.getAllOrders();
        for(Order order: orderList) {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }

        out.close();
    }


}
