<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< HEAD
    import java.util.Scanner;
=======
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
>>>>>>> 3d57f2c72dab70edc98dc19815d854f979039bd0
import javax.swing.JOptionPane;
=======
import java.util.Arrays;
>>>>>>> Stashed changes
=======
import java.util.Arrays;
>>>>>>> Stashed changes
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Test {

    // Main method
    public static void main(String[] args) {
        // Group 1: Get passwords and check access
        String group3 = getPasswordInput();
        String group5 = JOptionPane.showInputDialog("Enter the other password:");

        // Corrected password check with "PassWord"
        if (group3.equalsIgnoreCase("PassWord") || group5.equalsIgnoreCase("PassWord")) {
            JOptionPane.showMessageDialog(null, "Access granted! The door opens.");
        } else {
            JOptionPane.showMessageDialog(null, "Access denied. Incorrect password.");
            return; // Exit if incorrect password
        }

        // Group 2: Password verification and escape code generation
        group2();

        // Group 3: Word comparison logic
        checkGroup3Words(group3, group5);

        // Group 4: Memory test
        try {
            runMemoryTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Group 5: Final escape code guessing game
        group5EscapeCodeGuessing();

        JOptionPane.showMessageDialog(null, "You have unlocked the final escape code.");
    }

    // Group 1
    public static String getPasswordInput() {
        return JOptionPane.showInputDialog(null, "Please enter the name password:");
    }

    // Group 2
    public static void group2() {
        Scanner cin = new Scanner(System.in);
        Random random = new Random();

        String correctPassword = "PassWord"; // Changed here
        String lowerBoundWord = "password";

        int firstDigit = random.nextInt(51) * 2; // Random even number between 0 and 100
        int secondDigit = random.nextInt(51) * 2; // Random even number between 0 and 100
        String firstTwoDigits = String.valueOf(firstDigit) + String.valueOf(secondDigit);

        System.out.println("Enter the password:");
        String userInput = cin.nextLine();

        if (correctPassword.equalsIgnoreCase(userInput)) {
            System.out.println("You have made it you can proceed.");
            System.out.println("The lower bound word is '" + lowerBoundWord + "'.");
            System.out.println("The first two digits of the escape code are '" + firstTwoDigits + "'.");
        } else {
            System.out.println("Incorrect password. Try again!");
        }

        cin.close();
    }

    // Group 3
    public static void checkGroup3Words(String group2, String group5) {
        List<String> numberofwords = Arrays.asList("one", "ONE", "TWO", "two", "three", "THREE", "FOUR", "four", "five", "FIVE");

        if (!numberofwords.contains(group2) && !numberofwords.contains(group5)) {
            JOptionPane.showMessageDialog(null, "Both numbers are accepted");
        } else {
            JOptionPane.showMessageDialog(null, "At least one number is invalid");
        }

        if (group2.compareTo(group5) > 0) {
            System.out.println("High");
        } else {
            System.out.println("Low");
        }
    }

    // Group 4
    public static void runMemoryTest() throws InterruptedException {
        Scanner keyboard = new Scanner(System.in);
        Random randomNumbers = new Random();

        int number = randomNumbers.nextInt(10) + 1;
        System.out.println(number);

        Thread.sleep(2000);

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.print("Recall the number: ");
        int recall = keyboard.nextInt();

        if (recall == number) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect! The number was: " + number);
        }

        keyboard.close();
    }

    // Group 5
    public static void group5EscapeCodeGuessing() {
        Random random = new Random();
        int First_2_Digits = random.nextInt(90) + 10;
        int last_2_Digits = (random.nextInt(50) * 2);
        int finalCode = (First_2_Digits * 100) + last_2_Digits;

        JOptionPane.showMessageDialog(null, "Group 2 gives the first two digits: " + First_2_Digits);
        JOptionPane.showMessageDialog(null, "Group 4 confirms the number is even or odd.");

        int guess;
        do {
            String input = JOptionPane.showInputDialog("Enter your guess for the 4-digit escape code:");
            guess = Integer.parseInt(input);

            if (guess < finalCode) {
                JOptionPane.showMessageDialog(null, "Too low! Try again.");
            } else if (guess > finalCode) {
                JOptionPane.showMessageDialog(null, "Too high! Try again.");
            }
        } while (guess != finalCode);
    }
}
