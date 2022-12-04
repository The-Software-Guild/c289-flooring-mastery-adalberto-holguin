package com.sg.assessment.view;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public String readString(String prompt, int max) {
        boolean valid = false;
        String result = "";
        do {
            result = readString(prompt);
            if (result.length() <= max) {
                valid = true;
            } else {
                System.out.printf("The entry must be %s letters or less. /n", max);
            }
        } while (!valid);
        return result;
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
    public String readNames(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        String returnString;
        do {
            System.out.println(prompt);
            if ((returnString = inputReader.nextLine().trim()).equals("")) {
                System.out.println("No input received, please try again.");
            } else {
                for (int i = 0; i < returnString.length(); i++) {
                    if (!Character.isLetterOrDigit(returnString.charAt(i)) && returnString.charAt(i) != ' ' && returnString.charAt(i) != '.') {
                        System.out.println("Customer name cannot contain special characters.");
                        returnString = ""; // so do-while keeps looping
                        break;
                    }
                }
            }
        } while (returnString.equals(""));
        return returnString;
    }

    @Override
    public String readNamesAllowEmpty(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        String returnString;
        while (true) {
            boolean nameIsOk = true;
            System.out.println(prompt);
            if ((returnString = inputReader.nextLine().trim()).length() > 0) {

                for (int i = 0; i < returnString.length(); i++) {
                    if (!Character.isLetterOrDigit(returnString.charAt(i)) && returnString.charAt(i) != ' ' && returnString.charAt(i) != '.') {
                        System.out.println("Customer name cannot contain special characters.");
                        nameIsOk = false;
                        break;
                    }
                }
                // If looped through all chars in the name and found no illegal characters
                if (nameIsOk) {
                    return returnString;
                }
            } else {
                // If user left input empty
                return returnString;
            }
        }
    }

    @Override
    public String formatCurrency(BigDecimal amount) {
        return null;
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
        while (true) {
            try {
                do {
                    System.out.println(prompt);
                    num = Integer.parseInt(inputReader.nextLine());
                    if ((num < min || num > max) && min == max) {
                        System.out.println("Invalid input, the only available option is " + min + ".");
                    } else if (num < min || num > max) {
                        System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
                    }
                } while (num < min || num > max);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error. Please input only whole numbers for your selection.");
            }
        }
        return num;
    }

    @Override
    public int readIntAllowEmpty(String prompt, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int num = 0;
            try {
                String input = scanner.nextLine();
                if (!input.trim().equals("")) {
                    if (Integer.parseInt(input) >= min && Integer.parseInt(input) <= max) {
                        return num;
                    } else {
                        System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
                    }
                } else {
                    return num; // If user left input empty (will return -1)
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input, please only enter whole numbers.");
            }
        }
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

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        Scanner inputReader = new Scanner(System.in);
        System.out.println(prompt);
        return new BigDecimal(inputReader.nextLine()).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        Scanner inputReader = new Scanner(System.in);
        BigDecimal num;

        while (true) {
            try {
                System.out.println(prompt);
                num = new BigDecimal(inputReader.nextLine()).setScale(2, RoundingMode.HALF_EVEN);
                if (num.compareTo(min) == -1 || num.compareTo(max) == 1) {
                    System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
                } else {
                    return num;
                }
            } catch (NumberFormatException e) {
                // If the user inputs something other than numbers
                System.out.println("Invalid numeric input, please try again.");
            }
        }
    }

    @Override
    public BigDecimal readBigDecimalAllowEmpty(String prompt, BigDecimal min, BigDecimal max) {
        Scanner inputReader = new Scanner(System.in);
        while (true) {
            BigDecimal num = null;
            try {
                System.out.println(prompt);
                String numberString = inputReader.nextLine();
                if (numberString.trim().equals("")) {
                    return num; // If user left input empty (will return null)
                } else {
                    num = new BigDecimal(numberString).setScale(2, RoundingMode.HALF_EVEN);
                    if (num.compareTo(min) == -1 || num.compareTo(max) == 1) {
                        System.out.println("Invalid input, enter a number between " + min + " and " + max + ".");
                    } else {
                        return num;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input, please only enter whole numbers.");
            }
        }
    }
}
