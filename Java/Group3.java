import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.List;
public class Groupc{
    public static void main(String[] args) {
        String halfpassw ="agsja";
        String group2="";
        String group5="";

        List<String> numberofwords = Arrays.asList("one","ONE","TWO","two","three","THREE","FOUR","four","five","FIVE");
     
        if (!numberofwords.contains(group2)){ 

        
       JOptionPane.showMessageDialog(null,"Number is accepted");

        }

        else if (!numberofwords.contains(group5)){
            JOptionPane.showMessageDialog(null,"Number is accepted");


        }

        else
        {
            JOptionPane.showMessageDialog(null,"Number is invalid"); 

        }

        if(group2.compareTo(group5) > 0){
            System.out.println("High");

        }
        else 
        {
            System.out.println("Low");

              
        }
            

        if(group5.compareTo(group2) < 0){
            System.out.println("Low");

        }
        else 
        {
            System.out.println("High");

        }


            

        


        

    


}