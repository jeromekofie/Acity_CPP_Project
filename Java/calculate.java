import java.util.Scanner;

public class calculate {
    public static void add() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter num 1:");
        n1= input.nextInt ();
        
        sum =n1 + n2;

        System.out.println("Enter num 2:");
        n1= input.nextInt ();
        
        sum =n1 + n2

        
        System.out.print("Enter the second number: ");
        int num2 = scanner.nextInt();

        int result; 
        if (sign.equals("+")) {
            result = num1 + num2;
            System.out.println("Result: " + result);
        } else if (sign.equals("-")) {
            result = num1 - num2;
            System.out.println("Result: " + result);
        } else {
            System.out.println("Invalid operation. Please use + or -.");
        }

        scanner.close();
    }
}
