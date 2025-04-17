public abstract class Employee {
    protected int id;
    protected String name;
    protected String email;
    protected String phone;
    
    public Employee(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    public abstract double calculateSalary();
    
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Email: %s, Phone: %s", 
                          id, name, email, phone);
    }
    
    public String toFileString() {
        return String.format("%d,%s,%s,%s", id, name, email, phone);
    }

    protected abstract void setVisible(boolean b);
}