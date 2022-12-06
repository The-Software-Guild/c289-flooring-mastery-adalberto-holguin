package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import com.sg.assessment.dto.Order;
import com.sg.assessment.dto.Product;
import com.sg.assessment.dto.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryDaoTest {

    // The methods addOrder and removeOrder are tested directly here. All other methods of the DAO are tested indirectly when
    // testing those two methods, except for setCurrentFile, deleteFile, and writeToExport.

    FlooringMasteryDao dao;
    private List<State> stateList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    public FlooringMasteryDaoTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = appContext.getBean("flooringMasteryDao", FlooringMasteryDao.class);
    }

    @BeforeEach
    void setUp() {
        File testFile = new File("src/test/testorders.txt");
        dao = new FlooringMasteryDaoFileImpl(testFile);

        stateList.add(new State("TX", "Texas", new BigDecimal("4.45")));
        stateList.add(new State("WA", "Washington", new BigDecimal("9.25")));
        stateList.add(new State("KY", "Kentucky", new BigDecimal("6.00")));
        stateList.add(new State("CA", "California", new BigDecimal("25.00")));

        productList.add(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        productList.add(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        productList.add(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        productList.add(new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));
    }

    @Test
    void testAddOrder() throws FlooringMasteryPersistenceException {

        Order order = new Order();

        order.setOrderNumber(1);
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
        for (State state : stateList) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                order.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                break;
            }
        }
        BigDecimal materialsPlusLabor = order.getMaterialCost().add(order.getLaborCost());
        order.setTotal(materialsPlusLabor.add(order.getTax()));

        // To test that adding orders to file works, (make sure testorders file is empty first) we will try adding an order to
        // it as well as to the expectedList, then we will load the orders file and return its contents, which should be equal
        // to the expectedList which contains the order added to the file.
        List<Order> expectedList = new ArrayList<>();
        expectedList.add(order);
        dao.addOrder(order);
        dao.loadOrdersFile();
        assertEquals(expectedList, dao.getOrdersList(), "Returned list does not match expected list.");
    }

    @Test
    void testRemoveOrder() throws FlooringMasteryPersistenceException {

        Order order = new Order();
        order.setOrderNumber(3);
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
        for (State state : stateList) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                order.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                break;
            }
        }
        BigDecimal materialsPlusLabor = order.getMaterialCost().add(order.getLaborCost());
        order.setTotal(materialsPlusLabor.add(order.getTax()));

        Order order2 = new Order();
        order2.setOrderNumber(4);
        order2.setCustomerName("Adalberto");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25.00"));
        order2.setProductType("Wood");
        order2.setArea(new BigDecimal("100"));
        order2.setCostPerSquareFoot(new BigDecimal("5.15"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order2.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));
        order2.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_EVEN));

        // Getting State from statesList that matches state abbreviation in the Order, so we can calculate tax.
        for (State state : stateList) {
            if (state.getStateAbbreviation().equals(order.getState())) {
                BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
                BigDecimal costPlusLabor = order.getMaterialCost().add(order.getLaborCost());
                order2.setTax(costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN));
                break;
            }
        }
        materialsPlusLabor = order2.getMaterialCost().add(order.getLaborCost());
        order2.setTotal(materialsPlusLabor.add(order.getTax()));

        // Adding two orders to the testorders file, and adding the second one to expectedList. To test that removeOrder works,
        // we will try removing the first order from the file, then loading the file again, which should now only contain the
        // order that is in expectedList if it removed the first order from the file correctly.
        dao.addOrder(order);
        dao.addOrder(order2);
        List<Order> expectedList = new ArrayList<>();
        expectedList.add(order2);

        dao.removeOrder(order);
        dao.loadOrdersFile();
        assertEquals(expectedList, dao.getOrdersList(), "Returned list does not match expected list.");
    }
}
