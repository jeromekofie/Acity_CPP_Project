import java.text.SimpleDateFormat;
import java.util.Date;

public class PaySlipGenerator {
    public static void generatePaySlip(Employee emp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        String currentMonth = dateFormat.format(new Date());
        
        String payslip = String.format(
            "=== PAY SLIP FOR %s ===\n" +
            "Employee ID: %d\n" +
            "Name: %s\n" +
            "Email: %s\n" +
            "Phone: %s\n" +
            "Pay Period: %s\n" +
            "Gross Pay: $%.2f\n" +
            "Deductions: $%.2f\n" +
            "Net Pay: $%.2f\n" +
            "=========================",
            currentMonth,
            emp.getId(),
            emp.getName(),
            emp.getEmail(),
            emp.getPhone(),
            currentMonth,
            emp.calculateSalary(),
            calculateDeductions(emp),
            emp.calculateSalary() - calculateDeductions(emp)
        );
        
        // Save to file
        String fileName = String.format("payslip_%s_%d.txt", 
                                      emp.getName().replace(" ", "_"), 
                                      emp.getId());
        try (java.io.PrintWriter out = new java.io.PrintWriter(fileName)) {
            out.println(payslip);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static double calculateDeductions(Employee emp) {
        return emp.calculateSalary() * 0.2; // 20% tax
    }
}