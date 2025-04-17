public class FullTimeEmployee extends Employee {
    private double salary;
    
    public FullTimeEmployee(int id, String name, String email, String phone, double salary) {
        super(id, name, email, phone);
        this.salary = salary;
    }
    
    @Override
    public double calculateSalary() {
        return salary;
    }
    
    public double getSalary() {
        return salary;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Type: Full-Time, Salary: $%.2f", salary);
    }
    
    @Override
    public String toFileString() {
        return super.toFileString() + String.format(",FullTime,%.2f,0,0", salary);
    }

    @Override
    protected void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}