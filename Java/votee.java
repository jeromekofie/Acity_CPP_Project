import java.util.Scanner;
import javax.swing.JOptionPane;

public class votee {
    public static void main(String[] args) {
 
            System.out.println("Where do you want your output?");
            System.out.println("1.Console");
            System.out.println("2.GUI");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice;

            Scanner input = new Scanner(System.in);

            choice = input.nextInt();

            if(choice ==1){
                System.out.println("How old are you?: ");

                int age = input.nextInt();
                if(age>=18){
                    System.out.println("Go vote");
                }
                else if(age<=17 && age>=0){
                    System.out.println("Come back when 18");
                }
                else{
                    System.out.println("Positive numbers only");

                }
            
            }
            else if (choice==2){
                int age;

                age=Integer.parseInt( JOptionPane.showInputDialog("Enter your age:"));
                switch(age){
                    case 200:
                    case 18:
                }

                
                if(age>=18){
                    JOptionPane.showMessageDialog(null,"Elligible to vote");
                }
                else{
                    JOptionPane.showMessageDialog(null,"Not Elligible to vote");
        
                }
            }  
      
    }
}