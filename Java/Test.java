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

    //Group Challeng 1,2,3,4,5


    
}
