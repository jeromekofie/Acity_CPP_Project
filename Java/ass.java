import java.util.Scanner;  
import java.util.Random;   

public class as {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); // Create Scanner object for input
        Random random = new Random();          // Create Random object for random numbers

        // Step 1: Ask for the Wizard's Age
        System.out.print("Welcome, Algy the Wise! How old are you? ");
        int age = input.nextInt();  // Get the wizard's age
        System.out.println("Ah, so you are " + age + " years old.");

        // Step 2: Calculating Magic Points
        double magicPoints = (age * 10) + 50;
        System.out.println("Your total magic points are: " + magicPoints);

        // Step 3: Convert Magic Power to Integer
        int intMagicPoints = (int) magicPoints; // Type casting
        System.out.println("Converted magic points (integer): " + intMagicPoints);

        // Step 4: Check for Magic Overflow
        if (age > 100) {
            System.out.println("Oh no! Your age is over 100, causing a magic overflow!");
        }
        int overflowTest = (int) ((age + 200) * 10 + 50); // Simulate overflow
        System.out.println("With overflow, your magic points would be: " + overflowTest);

        // Step 5: The Magical Pi Shield
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

        // Step 9: More Powerful Spells with Math
        double superchargedPower = Math.pow(magicPoints, 3); // Raise to 3rd power
        System.out.println("Your supercharged magical power is: " + superchargedPower);

        // Step 10: Random Monster Encounter
        int monsterStrength = random.nextInt(100); // Random number between 0-99
        System.out.println("A wild monster appears! Its strength is: " + monsterStrength);

        // Bonus: Ensure magic points are within safe range
        if (magicPoints < 100 || magicPoints > 500) {
            System.out.println("WARNING: Your magic points are outside the safe range (100-500)!");
        }

        // Closing message
        System.out.println("Thank you for helping Algy in his magical adventure. Goodbye!");
    }
}


public class ass3 {
    
}
