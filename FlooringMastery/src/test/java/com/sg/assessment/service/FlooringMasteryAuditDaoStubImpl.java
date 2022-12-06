package com.sg.assessment.service;

import com.sg.assessment.dao.FlooringMasteryAuditDao;
import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;

public class FlooringMasteryAuditDaoStubImpl implements FlooringMasteryAuditDao {
    @Override
    public void writeAuditEntry(String message, String fileName) throws FlooringMasteryPersistenceException {

    }
}
