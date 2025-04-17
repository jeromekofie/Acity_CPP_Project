import java.util.Scanner;
class FactorialCalculator {
    // Recursive method 
    public static long factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number;
        
        // Input validation
        while (true) {
            System.out.print("Enter a non-negative integer: ");
            number = scanner.nextInt();
            if (number >= 0) {
                break;
            }
            System.out.println("Invalid input. Please enter a non-negative integer.");
        }
        
        // Compute 
        long result = factorial(number);
        System.out.println(number + "! = " + result);
        scanner.close();
    }
}