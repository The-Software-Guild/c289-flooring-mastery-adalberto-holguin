package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryDaoFileImpl;
import com.sg.assessment.dao.FlooringMasteryAuditDao;
import com.sg.assessment.dao.FlooringMasteryAuditDaoFileImpl;
import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlooringMasteryServiceImplTest {

    FlooringMasteryService service;

    public FlooringMasteryServiceImplTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = appContext.getBean("flooringMasteryService", FlooringMasteryService.class);
    }

    Order order = new Order();
    @BeforeEach
    void setup(){

        order.setOrderNumber(1);
        order.setCustomerName("Sam");
        order.setState("Texas");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("2000"));
        order.setCostPerSquareFoot(new BigDecimal("2.25"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));
        BigDecimal stateTax = order.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
        BigDecimal costPlusLabor = materialCost.add(laborCost);
        BigDecimal tax = costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal materialsPlusLabor = materialCost.add(laborCost);
        BigDecimal total = materialsPlusLabor.add(tax);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

    }
    @Test
    public void testCalculatePrices(){
        System.out.println(order.toString());
        //assertEquals(order,service.calculatePrices(order));
    }
}
