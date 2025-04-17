import java.util.Scanner;

public class CombinedProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSelect a program to run:");
            System.out.println("1. Test Average Calculator");
            System.out.println("2. Stock Commission Calculator");
            System.out.println("3. Sales Prediction");
            System.out.println("4. Pattern Printing");
            System.out.println("5. Exit"); 
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    test_average(scanner);
                    break;
                case 2:
                    stock_commission();
                    break;
                case 3:
                    sales_prediction();
                    break;
                case 4:
                    printPattern();
                    break;
                    
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    public static void test_average(Scanner scanner) {
        System.out.print("\nEnter first test score: ");
        double score1 = scanner.nextDouble();

        System.out.print("Enter second test score: ");
        double score2 = scanner.nextDouble();

        System.out.print("Enter third test score: ");
        double score3 = scanner.nextDouble();

        double average = (score1 + score2 + score3) / 3;

        System.out.println("\nTest Scores:");
        System.out.println("Score 1: " + score1);
        System.out.println("Score 2: " + score2);
        System.out.println("Score 3: " + score3);
        System.out.printf("Average Score: %.2f%n", average);
    }

    public static void stock_commission() {
        int shares_bought = 600;
        double price_per_share = 21.77;
        double commission_rate = 0.02;

        double stock_cost = shares_bought * price_per_share;
        double commission_amount = stock_cost * commission_rate;
        double total_amount = stock_cost + commission_amount;

        System.out.println("\nStock Commission Calculation:");
        System.out.printf("Amount paid for the stock: $%.2f%n", stock_cost);
        System.out.printf("Commission amount: $%.2f%n", commission_amount);
        System.out.printf("Total amount paid: $%.2f%n", total_amount);
    }

    public static void sales_prediction() {
        double total_sales = 4.6e6; // 4.6 million
        double east_coast_percentage = 0.62;
        double east_coast_sales = total_sales * east_coast_percentage;

        System.out.println("\nSales Prediction:");
        System.out.printf("The East Coast division is expected to generate $%.2f this year.%n", east_coast_sales);
    }

    public static void printPattern() {
        System.out.println("\nPattern Printing:");
        System.out.println("       *     ");
        System.out.println("      ***    ");
        System.out.println("     *****   ");
        System.out.println("    *******  ");
        System.out.println("   *********    ");
        System.out.println("      ***    ");
        System.out.println("      ***    ");
    }
}