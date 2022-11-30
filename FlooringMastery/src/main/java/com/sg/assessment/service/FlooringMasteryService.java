package com.sg.assessment.service;

import com.sg.assessment.model.State;

import java.math.BigDecimal;

public interface FlooringMasteryService {

    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, State state);
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
}
