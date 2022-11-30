package com.sg.assessment.service;

import com.sg.assessment.model.State;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FlooringMasterServiceImpl implements FlooringMasteryService {

    // This is a test
    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot) {
        return area.multiply(laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, State state) {
        BigDecimal stateTax = state.getTaxRate().divide(new BigDecimal("100"), 15, RoundingMode.HALF_EVEN);
        BigDecimal costPlusLabor = materialCost.add(laborCost);
        return costPlusLabor.multiply(stateTax).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        BigDecimal materialsPlusLabor = materialCost.add(laborCost);
        return materialsPlusLabor.add(tax);
    }
}
