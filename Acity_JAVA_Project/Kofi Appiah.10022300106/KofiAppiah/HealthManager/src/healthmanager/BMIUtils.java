/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthmanager;

/**
 *
 * @author User
 */
public class BMIUtils {
    public static double calculateBMI(double weight, double height) {
        return Math.round((weight / (height * height)) * 100.0) / 100.0;
    }
}