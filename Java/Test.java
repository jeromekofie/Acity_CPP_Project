import java.util.Scanner;
import java.util.Random;
import javax.swing.JOptionPane;

public class Test{

    //Main method 
    public static void main(String[] args){

        
    }

    //Group 1
    
    //Group 2
    
    //Group 3

    //Group 4
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


    //Group 5
    public class Escape101 {
            String otherPassword = JOptionPane.showInputDialog("Enter the other password:");
            
            Random random = new Random();
            int First_2_Digits = random.nextInt(90) + 10; // Random number between 10 and 99
            int last_2_Digits = (random.nextInt(50) * 2); // Ensures even number
            int finalCode = (First_2_Digits * 100) + last_2_Digits;
            
            JOptionPane.showMessageDialog(null, "Group 2 gives the first two digits: " + First_2_Digits);
            JOptionPane.showMessageDialog(null, "Group 4 confirms the number is even or odd.");
            
            int guess;
            do {
                String input = JOptionPane.showInputDialog("Enter your guess for the 4-digit escape code:");
                guess = Integer.parseInt(input); // Convert input to integer
                
                if (guess < finalCode) {
                    JOptionPane.showMessageDialog(null, "Too low! Try again.");
                } else if (guess > finalCode) {
                    JOptionPane.showMessageDialog(null, "Too high! Try again.");
                }
            } while (guess != finalCode);
            
            JOptionPane.showMessageDialog(null, "You have unlocked the final escape code.");
        }
    }

    //Group Challeng 1,2,3,4,5


    

