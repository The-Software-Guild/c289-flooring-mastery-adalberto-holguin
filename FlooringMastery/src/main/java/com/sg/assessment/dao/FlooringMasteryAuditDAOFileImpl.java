package com.sg.assessment.dao;

import com.sg.assessment.dao.exceptions.FlooringMasteryPersistenceException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class FlooringMasteryAuditDAOFileImpl implements FlooringMasteryAuditDAO {

    @Override
    public void writeAuditEntry(String message, String fileName) throws FlooringMasteryPersistenceException {
        File currentAuditFile = new File(fileName);

        // If this is the first order being added to a date, we need to create a new audit file for that date.
        if (!currentAuditFile.exists()) {
            createNewAuditFile(currentAuditFile);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(currentAuditFile, true))) {
            writer.println(message);
            writer.flush();
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Error, could not write to audit file. The file may have been moved " +
                    "or deleted.", e);
        }
    }

    private void createNewAuditFile(File newAuditFile) throws FlooringMasteryPersistenceException {
        try {
            newAuditFile.createNewFile();
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Error creating new audit file.");
        }
    }
}
