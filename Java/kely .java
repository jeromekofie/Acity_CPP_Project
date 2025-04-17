import java.util.Scanner;
public class Vote{
    public static void main(String[] args) {
        System.outprintln
}




Scanner input= new Scanner (system.in);
choice =input.nextInt();
if (choice==1){
    System.out.print("how old are you;");
    int age = input.nextint();
    if (age >=18){
        System.out.println("Go vote");
    }
    else if (age<=17 && age >=0){
        System.out.println("come back whe you're 18");
    }
    else {
        System.out.println("positive numbers only");
}
if (choice==2){
    age=Integer.parseInt( JOptionPane.showInputDialog("how old are you"));
    
    System.out.print("how old are you;");
    int age = input.nextint();
    if (age >=18){
        System.out.println("Go vote");
    }
    else if (age<=17 && age >=0){
        System.out.println("come back whe you're 18");
    }
    else {
        System.out.println("positive numbers only");
}