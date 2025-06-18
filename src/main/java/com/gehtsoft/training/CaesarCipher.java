package com.gehtsoft.training;

import java.util.Scanner;

public class CaesarCipher {

    //Because the logic of encryption and decryption is similar added the enum to choose mode and re-use the code
    public static enum Mode {
        ENCRYPT(1, "Encryption"),
        DECRYPT(2, "Decryption");

        public final String name;
        public final int index;
        private Mode(int index, String name) {
            this.index = index;
            this.name = name;
        }
    }

    //Main logic of the class
    public static String crypt(Mode mode, String input, int shift) {
        switch (mode) {
            case ENCRYPT:
                shift *= 1;
                break;
            case DECRYPT:
                shift *= -1;
                break;
        }
        StringBuffer result = cryptString(input, shift);
        return result.toString();
    }


    //Because the decryption without the shift value a bit sophisticated created another method.
    //You will be promoted to decrypt current result until it will make sense
    public static String decryptWithoutShiftValue(String input) {
        Scanner scanner = new Scanner(System.in);
        String currentResult = input;
        int shift = -1;
        do {
            StringBuffer result = cryptString(currentResult, shift);
            currentResult = result.toString();
            System.out.println(result);
        } while (!CustomUtils.theShowMustGoOn("Does this sentence make sense? (y/n): ",scanner));

        return currentResult.toString();
    }

    //The base logic of encryption
    private static StringBuffer cryptString(String input, int shift) {
        StringBuffer result = new StringBuffer();
        for (char ch : input.toCharArray()) {
            if (Character.isLetter(ch)) {
                BaseInfo b = getBase(ch);
                int shiftedIndex = ((ch - b.base + shift) % b.alphabetSize + b.alphabetSize) % b.alphabetSize;
                char shifted = (char) (b.base + shiftedIndex);
                result.append(shifted);

            } else {
                result.append(ch);
            }
        }
        return result;
    }
    //Get the start point of the current character to handle wrap-around and language check
    private static BaseInfo getBase(char ch) {
        //Check if current character belongs to the English char set
        if (ch >= 0x0041 && ch <= 0x005A || ch >= 0x0061 && ch <= 0x007A) {
            char base = Character.isLowerCase(ch) ? (char) 0x0061 : (char) 0x0041;
            //In english alphabet 26 letters
            return new BaseInfo(base, 26);

        //Check if current character belongs to the Russian char set
        } else if (ch >= 0x0410 && ch <= 0x042F || ch >= 0x0430 && ch <= 0x044F) {
            char base = Character.isLowerCase(ch) ? (char) 0x0430 : (char) 0x0410;
            //In russian alphabet 26 letters
            return new BaseInfo(base, 33);
        }
        throw new IllegalArgumentException("Only English and Russian Languages are supported");
    }
}
