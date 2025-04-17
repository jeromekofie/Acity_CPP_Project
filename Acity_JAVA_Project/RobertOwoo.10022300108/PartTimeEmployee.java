public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private double hoursWorked;
    
    public PartTimeEmployee(int id, String name, String email, String phone, 
                          double hourlyRate, double hoursWorked) {
        super(id, name, email, phone);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }
    
    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public double getHoursWorked() {
        return hoursWorked;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(
            ", Type: Part-Time, Hourly Rate: $%.2f, Hours Worked: %.2f, Salary: $%.2f", 
            hourlyRate, hoursWorked, calculateSalary());
    }
    
    @Override
    public String toFileString() {
        return super.toFileString() + String.format(",PartTime,0,%.2f,%.2f", hourlyRate, hoursWorked);
    }
}