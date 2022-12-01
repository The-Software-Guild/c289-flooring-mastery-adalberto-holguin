package com.sg.assessment.view;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements UserIO {


    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return inputReader.nextLine().trim();
    }

    @Override
    public String readStringNoEmpty(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        String returnString;
        do {
            System.out.println(prompt);
            if ((returnString = inputReader.nextLine()).equals("")) {
                System.out.println("No input received, please try again.");
            }
        } while (returnString.equals(""));
        return returnString.trim();
    }

    @Override
    public BigDecimal readBigdecimal(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return new BigDecimal(inputReader.nextLine());
    }

    @Override
    public int readInt(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return Integer.parseInt(inputReader.nextLine());
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        Scanner inputReader = new Scanner(System.in);
        int num;
        do {
            System.out.println(prompt);
            num = Integer.parseInt(inputReader.nextLine());
            if (num < min || num > max) {
                System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
            }
        } while (num < min || num > max);
        return num;
    }

    @Override
    public double readDouble(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return Double.parseDouble(inputReader.nextLine());
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        Scanner inputReader = new Scanner(System.in);
        double num;
        do {
            System.out.println(prompt);
            num = Double.parseDouble(inputReader.nextLine());
            if (num < min || num > max) {
                System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
            }
        } while (num < min || num > max);
        return num;
    }

    @Override
    public float readFloat(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return Float.parseFloat(inputReader.nextLine());
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        Scanner inputReader = new Scanner(System.in);
        float num;
        do {
            System.out.println(prompt);
            num = Float.parseFloat(inputReader.nextLine());
            if (num < min || num > max) {
                System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
            }
        } while (num < min || num > max);
        return num;
    }

    @Override
    public long readLong(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return Long.parseLong(inputReader.nextLine());
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        Scanner inputReader = new Scanner(System.in);
        long num;
        do {
            System.out.println(prompt);
            num = Long.parseLong(inputReader.nextLine());
            if (num < min || num > max) {
                System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
            }
        } while (num < min || num > max);
        return num;
    }
}
