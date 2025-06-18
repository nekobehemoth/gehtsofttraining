package com.gehtsoft.training;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CustomUtils {

    //Check if we continue executing current menu or exit
    public static boolean theShowMustGoOn(String inputMessage, Scanner scanner) {
        while (true) {
            System.out.print(inputMessage);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("д")) return true;
            if (input.equals("n") || input.equals("н")) return false;
            System.out.println("Please, enter only 'y', 'n', 'д' or 'н'");
        }
    }

    //The logic which parse String or file
    public static String enterNotEmptyStringOrPath(String inputMessage, Scanner scanner) throws IOException {
        String inputString;
        do {
            System.out.print(inputMessage);
            inputString = scanner.nextLine().trim();
            if (CustomUtils.isExistingPath(inputString)) {
                inputString = CustomUtils.readFile(inputString);
            }
            if (inputString.isEmpty()) {
                System.out.println("Entered string can't be empty, please try again.");
            }
        } while (inputString.isEmpty());
        return inputString;
    }


    //Safe int scanner wrapper. It will ask enter integer until you do it
    public static int readInt(String inputMessage, Scanner scanner) {
        while (true){
            System.out.print(inputMessage);
            String inputInt = scanner.nextLine().trim();
            try {
                return Integer.parseInt(inputInt);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    //Read input and checks if it is path to existing file.
    //If file exists returns its content. If it binary file it will fail farther on flow and will return following error:
    //"Only English and Russian Languages are supported"
    public static String readFile(String pathString) throws IOException {
        Path path = Paths.get(pathString).toAbsolutePath();

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IOException("File is not found: " + path.toString());
        }

        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }


    //Check if path exists. Please note it use the current dir as base folder,
    //So if you enter existing file without path like "test.txt" and it present in current folder it will use it as input.
    //Or you can enter absolute path like D:\test.txt for Windows or /mnt/test/path/test.txt for Mac/Linux to fetch the file.
    public static boolean isExistingPath(String input) {
        input = input.replace("/", File.separator).replace("\\", File.separator);
        Path path = Paths.get(input).toAbsolutePath();
        return input.contains(File.separator) || Files.exists(path);

    }

}
