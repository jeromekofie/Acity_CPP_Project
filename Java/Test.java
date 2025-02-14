import java.util.Scanner;
import javax.swing.JOptionPane;

public class Test{

    

    public static void main(String[] args){

        
    }

    //Group 1

public class the_quiz {
    public static String combinePasswords(String group3, String group5) {
        return group3 + group5;
    }

    public static String getPasswordInput() {
        return JOptionPane.showInputDialog(null,"Please enter the name password:");
    }

    public static void checkPassword(String inputPassword, String fullPassword) {
        if (inputPassword.equals(fullPassword)) {

            JOptionPane.showMessageDialog(null, "Access granted! The door opens.");
        } else {

            JOptionPane.showMessageDialog(null, "Access denied. Incorrect password.");
        }
    }
}


    //Group 2



    //Group 3



    //Group 4



    //Group 5




    //Group Challeng 1,2,3,4,5


    
}