import java.util.Scanner;  
import java.util.Random;   

public class ass3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); 
        Random random = new Random();          
        // Ask for the Wizard
        System.out.print("Welcome, Algy the Wise! How old are you? ");
        int age = input.nextInt();  
        System.out.println("Algy, you are " + age + " years old.");
        double magicPoints = (age * 10) + 50;
        System.out.println("Your total magic points are: " + magicPoints); //magic points

        
        int intMagicPoints = (int) magicPoints; 
        System.out.println("Converted magic points (integer): " + intMagicPoints);

        
        if (age > 100) {
            System.out.println("Oh no! Your age is over 100, causing a magic overflow!");
        }
        int overflowTest = (int) ((age + 200) * 10 + 50); // Simulate overflow
        System.out.println("With overflow, your magic points would be: " + overflowTest);

        final double MAGIC_PI = 3.14159; // Named constant
        int radius = 10;
        double shieldCircumference = 2 * MAGIC_PI * radius;
        System.out.println("The circumference of your magical shield is: " + shieldCircumference);

        // Step 6: Adding Magical Boosts
        magicPoints += 20;   // Add boost
        magicPoints *= 2;    // Multiply total
        System.out.println("Your boosted magical power is: " + magicPoints);

        // Step 7: Formatting the Magic
        System.out.printf("Formatted magic points: %.2f%n", magicPoints); // Two decimal places

        // Step 8: Secret Spells with Strings
        System.out.print("What is your favorite spell, Algy? ");
        input.nextLine(); // Clear the input buffer
        String spell = input.nextLine(); // Get spell as a string
        System.out.println("Your spell in uppercase: " + spell.toUpperCase());
        System.out.println("The length of your spell is: " + spell.length() + " characters.");

        


public class ass3 {
    
}
