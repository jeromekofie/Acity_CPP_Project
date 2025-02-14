import java.util.Scanner;
import javax.swing.JOptionPane;

public class Test{

    

    public static void main(String[] args){

        
    }

    //Group 1


    //Group 2



    //Group 3



    //Group 4



    //Group 5


    import java.util.Random;
    import javax.swing.JOptionPane; // Import JOptionPane

public class Escape101 {
    public static void main(String[]args) {
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


    
}