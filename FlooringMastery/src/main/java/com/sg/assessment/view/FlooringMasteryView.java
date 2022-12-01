package com.sg.assessment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlooringMasteryView {

    private UserIO io;

    @Autowired
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
}
