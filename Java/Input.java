import java.util.Scanner;

public class Input{

    public static void main(String[] args){

        Scanner cin = new Scanner(System.in);

        int age;

        System.out.print("Enter Age: ");

        age = cin.nextInt();

        System.out.println("You are " + age + " years old");

        cin.close();

    }

}67 