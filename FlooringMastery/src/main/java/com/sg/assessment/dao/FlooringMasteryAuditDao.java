package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;

public interface FlooringMasteryAuditDao {

    void writeAuditEntry(String message, String fileName) throws FlooringMasteryPersistenceException;
}
