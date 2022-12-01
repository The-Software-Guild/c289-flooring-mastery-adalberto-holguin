package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface FlooringMasteryService {

    private FlooringMasteryDao;

    @Autowired
    public FlooringMasteryService(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, State state);
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
}
