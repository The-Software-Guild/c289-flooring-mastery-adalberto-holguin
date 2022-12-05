package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryDaoTest {

    FlooringMasteryDao testDao;
    private List<State> stateList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    @Test
    void loadStatesAndProductsLists() {
    }

    public FlooringMasteryDaoTest() {

    }

    @BeforeEach
    void setUp() throws Exception {
        File testFile = new File("src/test/testorders.txt");
        testDao = new FlooringMasterDaoFileImpl(testFile);

        stateList = testDao.getStatesList();
        stateList.add(new State("TX", "Texas", new BigDecimal("4.45")));
        stateList.add(new State("WA", "Washington", new BigDecimal("9.25")));
        stateList.add(new State("KY", "Kentucky", new BigDecimal("6.00")));
        stateList.add(new State("CA", "California", new BigDecimal("25.00")));

        productList = testDao.getProductsList();
        productList.add(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        productList.add(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        productList.add(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        productList.add(new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));


    }

    @Test
    void addOrder() throws FlooringMasteryPersistenceException {

        Order order = new Order();

        order.setOrderNumber(12052022);
        order.setCustomerName("Patrick");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));
        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));


        // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
        BigDecimal tax = new BigDecimal("0");
        for (State state : testDao.getStatesList()) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                order.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                break;
            }
        }
        BigDecimal materialsPlusLabor = order.getMaterialCost().add(order.getLaborCost());
        order.setTotal(materialsPlusLabor.add(order.getTax()));

        assertEquals(0, testDao.getOrdersList().size(), "Returned list does not match expected size.");
        testDao.addOrder(order);
        assertEquals(1, testDao.getOrdersList().size(), "Returned list does not match expected size.");

//        assertEquals(order.getOrderNumber(), retrieveOrder.getOrderNumber(), "Checking Order Number.");
//        assertEquals(order.getCustomerName(), retrieveOrder.getCustomerName(), "Checking Customer Name.");
//        assertEquals(order.getState(), retrieveOrder.getState(), "Checking State Name.");
//        assertEquals(order.getTaxRate(), retrieveOrder.getTaxRate(), "Confirming Check Rate.");
//        assertEquals(order.getProductType(), retrieveOrder.getProductType(), "Checking Product Type.");
//        assertEquals(order.getArea(), retrieveOrder.getArea(), "Confirming Area.");
//        assertEquals(order.getCostPerSquareFoot(), retrieveOrder.getCostPerSquareFoot(), "Checking Cost Per Square Foot.");
//        assertEquals(order.getLaborCostPerSquareFoot(), retrieveOrder.getLaborCostPerSquareFoot(), "Checking Labor Cost Per Square Foot.");
//        assertEquals(order.getMaterialCost(), retrieveOrder.getMaterialCost(), "Checking Material Cost.");
//        assertEquals(order.getLaborCost(), retrieveOrder.getLaborCost(), "Checking Labor Cost.");
//        assertEquals(order.getTax(), retrieveOrder.getTax(), "Checking Tax.");
//        assertEquals(order.getTotal(), retrieveOrder.getTotal(), "Checking Total.");


    }

    @Test
    void editOrder() {

        Order order = new Order();

        order.setOrderNumber(12052022);
        order.setCustomerName("Patrick");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));
        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));

        // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
        BigDecimal tax = new BigDecimal("0");
        for (State state : testDao.getStatesList()) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                order.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                break;
            }
        }
//                testDao.addOrder(order);

                Order editedOrder = order;
                editedOrder.setCustomerName("Cool Dudes & Co.");

                //editedOrder = testDao.editOrder(editedOrder);

                List<Order> orders = testDao.getOrdersList();
                int orderNumber = editedOrder.getOrderNumber();

                Order chosenOrder = orders.stream()
                        .filter(o -> o.getOrderNumber() == orderNumber)
                        .findFirst().orElse(null);

                assertEquals(editedOrder, chosenOrder);

            }

            @Test
            void removeOrder () throws FlooringMasteryPersistenceException {

                Order order = new Order();

                order.setOrderNumber(12052022);
                order.setCustomerName("Patrick");
                order.setState("CA");
                order.setTaxRate(new BigDecimal("25.00"));
                order.setProductType("Wood");
                order.setArea(new BigDecimal("100"));
                order.setCostPerSquareFoot(new BigDecimal("5.15"));
                order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
                order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));
                order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));

                // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
                BigDecimal tax = new BigDecimal("0");
                for (State state : testDao.getStatesList()) {
                    if (state.getStateAbbreviation().equals(order.getState())) {
                        BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                        BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                        order.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                        break;
                    }
                }

                //Adding both to Dao

                //testDao.addOrder();

                //Order removeOrder = testDao.removeOrder();

                List<Order> initialOrders = testDao.getOrdersList();


                List<Order> fromDao = testDao.getOrdersList();

                assertEquals(1, testDao.getOrdersList().size(), "Returned list does not match expected size.");
                testDao.removeOrder(order);
                assertEquals(0, testDao.getOrdersList().size(), "Returned list does not match expected size.");

            }
        }
