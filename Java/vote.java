import javax.swing.JOptionPane;

public class vote {
    public static void main(String[] args) {

        System.out.println("Menu");
        System.out.println("1.Console");
        System.out.println("2.GUI");


     
        int age;      
        age=Integer.parseInt( JOptionPane.showInputDialog("Enter your age:"));
     

        if(age>=18){
            JOptionPane.showMessageDialog(null,"Elligible to vote");
        }
        else{
            JOptionPane.showMessageDialog(null,"Not Elligible to vote");

        }
      
    }
}