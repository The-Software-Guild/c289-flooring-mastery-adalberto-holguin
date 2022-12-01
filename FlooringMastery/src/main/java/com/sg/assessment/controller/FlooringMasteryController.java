package com.sg.assessment.controller;

import com.sg.assessment.dao.FlooringMasterDaoFileImpl;
import com.sg.assessment.dao.FlooringMasteryDao;
import com.sg.assessment.view.FlooringMasteryView;
import com.sg.assessment.view.UserIO;
import com.sg.assessment.view.UserIOConsoleImpl;

public class FlooringMasteryController {

    private FlooringMasteryView view;
    private FlooringMasteryDao dao;

    public FlooringMasteryController(FlooringMasteryDao dao, FlooringMasteryView view) {
        this.view = view;
        this.dao = dao;
    }
    private UserIO io = new UserIOConsoleImpl();

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        while (keepGoing) {
            io.print("<<Flooring Program>>");
            io.print("1. Display Orders");
            io.print("2. Add an Order");
            io.print("3. Edit an Order");
            io.print("4. Remove an Order");
            io.print("5. Export All Data");
            io.print("6. Quit");

            menuSelection = io.readInt("Please select from the above choices.", 1, 6);

            switch (menuSelection) {
                case 1:
                    displayOrder();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportData();
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default :
                    unknownCommand();

            }
        }
        exitMessage();
    }
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    private void displayOrder() {

    }
    private void addOrder() {

    }
    private void editOrder() {

    }
    private void removeOrder() {

    }
    private void exportData() {

    }
    private void unknownCommand() {
        view.displayUnknownCommandBanner();

    }
    private void exitMessage() {
        view.displayExitBanner();

    }
}


