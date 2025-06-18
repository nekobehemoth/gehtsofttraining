package com.gehtsoft.training;


import java.io.IOException;
import java.util.EmptyStackException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    //Main menu Enum object, it used to call required logic.
    public enum Menu {

        //Encryption mode. Main method is CaesarCipher.crypt(CaesarCipher.Mode.ENCRYPT, inputString, shiftValue);
        ENCRYPT(1, "Caesar Cipher Encryption") {
            @Override
            void exec() {
                System.out.println(CaesarCipher.Mode.ENCRYPT.name + " mode: ");
                String inputString = null;
                try {
                    inputString = CustomUtils.enterNotEmptyStringOrPath("Enter text to encrypt or path to the file: ", scanner);
                    int shiftValue = CustomUtils.readInt("Enter shift value: ", scanner);
                    String result = CaesarCipher.crypt(CaesarCipher.Mode.ENCRYPT, inputString, shiftValue);
                    System.out.println("Result: " + result);
                } catch (IOException e) {
                    System.out.println("Issue during the file reading: " + e.getMessage());
                }
            }
        },

        //Decryption mode. Main method is CaesarCipher.crypt(CaesarCipher.Mode.DECRYPT, inputString, shiftValue);
        //Also this mode can be used without shiftValue as CaesarCipher.decryptWithoutShiftValue(inputString)
        DECRYPT(2, "Caesar Cipher Decryption") {
            @Override
            void exec() {
                System.out.println(CaesarCipher.Mode.DECRYPT.name + " mode: ");
                try {
                    String inputString = CustomUtils.enterNotEmptyStringOrPath("Enter text to decrypt or path to the file: ", scanner);
                    System.out.print("Enter shift value (or leave empty): ");
                    String shiftValue = scanner.nextLine().trim();

                    //If shift value is empty we will shift by -1 until the text will not make sense
                    //If result text make sense enter 'Y'

                    String result = shiftValue.isEmpty()
                            ? CaesarCipher.decryptWithoutShiftValue(inputString)
                            : CaesarCipher.crypt(CaesarCipher.Mode.DECRYPT, inputString, Integer.parseInt(shiftValue));

                    System.out.println("Result: " + result);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid shift value. Enter a valid integer.");
                } catch (IOException e) {
                    System.out.println("Issue during the file reading: " + e.getMessage());
                }
            }
        },

        //Evaluation mode. Enter basic math expression. The base +-()*/ operators an unary minus are supported
        EVALUATE(3, "Arithmetic Expression Evaluation") {
            @Override
            void exec() {
                System.out.print("Enter expression to evaluate: ");
                String expression = scanner.nextLine();
                try {
                    double result = ArithmeticExpressionEvaluator.evaluate(expression);
                    System.out.println("Result: " + result);
                } catch (EmptyStackException e) {
                    System.out.println("Wrong input, kindly enter only numbers and basic math operators like +-()*/");
                } catch (ArithmeticException e) {
                    System.out.println("Error in evaluation: " + e.getMessage());
                }
            }
        },

        //Exit from the application
        EXIT(4, "Exit") {
            @Override
            void exec() {
                System.out.println("Exiting...");
                System.exit(0);
            }
        };

        public final int id;
        public final String description;

        Menu(int id, String description) {
            this.id = id;
            this.description = description;
        }
        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        abstract void exec();

        public static Menu getEntryById(int id) {
            for (Menu entry : Menu.values()) {
                if (entry.id == id) {
                    return entry;
                }
            }
            throw new IllegalArgumentException("The wrong option has been selected, please select only a number from "
                    + values()[0].getId()
                    + " to " + values()[values().length - 1].getId());
        }

    }

    public static void main(String[] args) {

        do {
            printMenu();
            try {
                int choice = CustomUtils.readInt("Enter your choice: ", scanner);
                Menu entry = Menu.getEntryById(choice);
                //Execute selected menu entry
                entry.exec();
            } catch (InputMismatchException e) {
                System.out.println("Please enter only int value");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while(CustomUtils.theShowMustGoOn("Continue? (y/n): ",scanner));
    }


    private static void printMenu() {
        System.out.println();
        System.out.println("Welcome to Gehtsoft Technical Assessment");
        System.out.println("Please choose an option:");
        for (Menu entry  : Menu.values()) {
            System.out.println(entry.getId() + ". " + entry.getDescription());
        }
    }




}