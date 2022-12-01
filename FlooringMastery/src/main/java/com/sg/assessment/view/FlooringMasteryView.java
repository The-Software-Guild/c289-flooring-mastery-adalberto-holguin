package com.sg.assessment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sg.assessment.model.Order;

import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class FlooringMasteryView {

    private UserIO io;

    @Autowired
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }


    public void printMenu(){

        io.print("* * * * * * * * * * * * * * * * * ");
        io.print("* * * * * * * * * * * * * * * * * *");
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print(" 2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("Export All Data");
        io.print("Quit");

    }

    public  void displayUnknownCommandBanner(){
        io.print("Entered invalid command");
    }

    public void displayExitBanner(){
        io.print("Goodbye!!");
    }
    public Order displayOrderDate(){
        Order order=new Order();
        String ordernumber=io.readString("Enter the order date");
        return order;
    }



    ///add order
    public Order addOrder(){


        Order order=new Order();
        String date=io.readString("Enter the order date");
        String name= io.readString("Enter customer name");
        String state=io.readString("Enter name of the state");
        String productType=io.readString("Enter product type");
        BigDecimal area=io.readBigdecimal("Enter area");

        order.setCustomerName(name);
        order.setState(state);
        order.setProductType(productType);
        order.setArea(area);

        return order;
    }

    //editorder //removeorder
    public String getdate(){
        String date=io.readString("Enter the order date");
        return date;

    }
    public int getordernumber(){
        int orderNumber=io.readInt("Enter the order number");
        return orderNumber;

    }




}
