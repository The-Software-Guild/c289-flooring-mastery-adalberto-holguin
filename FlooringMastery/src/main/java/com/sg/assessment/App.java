package com.sg.assessment;

import com.sg.assessment.controller.FlooringMasteryController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) throws InterruptedException {

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.assessment");
        appContext.refresh();

        FlooringMasteryController controller = appContext.getBean("flooringMasteryController", FlooringMasteryController.class);
        controller.run();
    }
}
