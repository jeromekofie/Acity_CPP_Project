/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ericamoakoagyare
 */
public class EncryptionLogic {
    private static final int SHIFT = 3;

    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) result.append((char)(ch + SHIFT));
        return result.toString();
    }

    public String decrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) result.append((char)(ch - SHIFT));
        return result.toString();
    }
    
}
