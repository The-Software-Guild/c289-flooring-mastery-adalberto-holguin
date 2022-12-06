package com.sg.assessment.service;

import com.sg.assessment.dto.Order;
import com.sg.assessment.service.exceptions.InvalidDateException;
import com.sg.assessment.service.exceptions.InvalidStateException;
import com.sg.assessment.service.exceptions.NoSuchOrderException;
import com.sg.assessment.service.exceptions.NoSuchProductException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlooringMasteryServiceImplTest {

    // The methods calculatePrices, generateOrderNumber, validateDate, validateState, validateProduct, and retrieveOrder are
    // tested directly here. All other methods of the service layer are tested indirectly when testing those methods, except
    // for setOrdersFile, deleteEmptyFile, and exportData. AuditDao functionality is not tested.

    FlooringMasteryService service;

    public FlooringMasteryServiceImplTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = appContext.getBean("flooringMasteryService", FlooringMasteryService.class);
    }

    @Test
    public void testCalculatePrices() {
        // To test the calculatePrices method, we will compare an order for which material cost, labor cost, tax, and total was
        // calculated by hand with another one whose fields will be calculated by the method.

        Order orderCalculatedByHand = new Order();
        orderCalculatedByHand.setOrderNumber(1);
        orderCalculatedByHand.setCustomerName("Sam");
        orderCalculatedByHand.setState("TX");
        orderCalculatedByHand.setTaxRate(new BigDecimal("4.45"));
        orderCalculatedByHand.setProductType("Carpet");
        orderCalculatedByHand.setArea(new BigDecimal("2000"));
        orderCalculatedByHand.setCostPerSquareFoot(new BigDecimal("2.25"));
        orderCalculatedByHand.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        orderCalculatedByHand.setMaterialCost(new BigDecimal("4500.00"));
        orderCalculatedByHand.setLaborCost(new BigDecimal("4200.00"));
        orderCalculatedByHand.setTax(new BigDecimal("387.15"));
        orderCalculatedByHand.setTotal(new BigDecimal("9087.15"));

        Order orderCalculatedByMethod = new Order();
        orderCalculatedByMethod.setOrderNumber(1);
        orderCalculatedByMethod.setCustomerName("Sam");
        orderCalculatedByMethod.setState("TX");
        orderCalculatedByMethod.setTaxRate(new BigDecimal("4.45"));
        orderCalculatedByMethod.setProductType("Carpet");
        orderCalculatedByMethod.setArea(new BigDecimal("2000"));
        orderCalculatedByMethod.setCostPerSquareFoot(new BigDecimal("2.25"));
        orderCalculatedByMethod.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        assertEquals(orderCalculatedByHand, service.calculatePrices(orderCalculatedByMethod), "Returned order does not match " +
                "expected order.");
    }

    @Test
    void testValidateDate() throws InvalidDateException {
        // Should throw InvalidDateException if we pass a date in the past (from the current date) or pass if it is in the future.
        String pastDateString = "04/12/1999";
        LocalDate pastDate = LocalDate.parse(pastDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        assertThrows(InvalidDateException.class, () -> service.validateDate(pastDate), "Date in the past does not throw " +
                "expected exception.");

        String futureDateString = "04/12/2050";
        LocalDate futureDate = LocalDate.parse(futureDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        service.validateDate(futureDate); // This should not throw an exception
    }

    @Test
    void testGenerateOrderNumber() {
        // In the StubDAO we are using an orderList with two orders; one with order number 12, the other one with order number
        // 34, so we are expecting the number of a new order to be 35 (one more than the maximum order number in the list).
        assertEquals(35, service.generateOrderNumber(), "Returned order number does not match expected order number.");
    }

    @Test
    void testValidateState() throws InvalidStateException {
        // State List populated in StubDAO.
        assertThrows(InvalidStateException.class, () -> service.validateState("FL"), "Passing in a state" +
                " abbreviation that does not belong to any state in the states list does not throw the expected exception.");

        // This should not throw an exception, as we are passing a state abbreviation that belongs to a state in the list.
        service.validateState("CA");
    }

    @Test
    void testValidateProduct() throws NoSuchProductException {
        // Product List populated in StubDAO.
        assertThrows(NoSuchProductException.class, () -> service.validateProduct("Mattress"), "Passing in a" +
                " product type that is not in the products list does not throw the expected exception.");

        // This should not throw an exception, as we are passing a product type that is in the list.
        service.validateProduct("Wood");
    }

    @Test
    void testRetrieveOrder() throws NoSuchOrderException {
        // Order List populated in Stub DAO.
        assertThrows(NoSuchOrderException.class, () -> service.retrieveOrder(3), "Passing in an order" +
                "number that does not belong to any order on the list does not throw the expected exception.");

        // Order List contains an Order just like this, so when we pass in number 12, the returned order should match this.
        Order expectedOrder = new Order(12, null, null, null, null, null,
                null, null, null, null, null, null);
        assertEquals(expectedOrder, service.retrieveOrder(12));
    }
}
