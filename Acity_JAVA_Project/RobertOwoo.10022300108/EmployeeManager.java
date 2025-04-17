import java.sql.*;
import java.util.*;
import java.io.*;

public class EmployeeManager {
    private List<Employee> employees;
    private int nextId;
    private static final String FILE_NAME = "employees.txt";
    
    public EmployeeManager() {
        employees = new ArrayList<>();
        nextId = 1;
        loadEmployees();
        syncWithDatabase();
    }
    
    private void loadEmployees() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Employee emp = parseEmployee(line);
                if (emp != null) {
                    employees.add(emp);
                    if (emp.getId() >= nextId) {
                        nextId = emp.getId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, that's okay
        }
    }
    
    private Employee parseEmployee(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) return null;
        
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            String phone = parts[3].trim();
            String type = parts[4].trim();
            
            if (type.equals("FullTime")) {
                double salary = Double.parseDouble(parts[5].trim());
                return new FullTimeEmployee(id, name, email, phone, salary);
            } else if (type.equals("PartTime")) {
                double hourlyRate = Double.parseDouble(parts[5].trim());
                double hoursWorked = Double.parseDouble(parts[6].trim());
                return new PartTimeEmployee(id, name, email, phone, hourlyRate, hoursWorked);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void saveEmployees() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Employee emp : employees) {
                writer.println(emp.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void syncWithDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("DELETE FROM employees"); // Clear existing
            
            for (Employee emp : employees) {
                if (emp instanceof FullTimeEmployee) {
                    FullTimeEmployee ft = (FullTimeEmployee) emp;
                    try (PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                        pstmt.setInt(1, ft.getId());
                        pstmt.setString(2, ft.getName());
                        pstmt.setString(3, ft.getEmail());
                        pstmt.setString(4, ft.getPhone());
                        pstmt.setString(5, "FullTime");
                        pstmt.setDouble(6, ft.getSalary());
                        pstmt.setDouble(7, 0);
                        pstmt.setDouble(8, 0);
                        pstmt.executeUpdate();
                    }
                } else if (emp instanceof PartTimeEmployee) {
                    PartTimeEmployee pt = (PartTimeEmployee) emp;
                    try (PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                        pstmt.setInt(1, pt.getId());
                        pstmt.setString(2, pt.getName());
                        pstmt.setString(3, pt.getEmail());
                        pstmt.setString(4, pt.getPhone());
                        pstmt.setString(5, "PartTime");
                        pstmt.setDouble(6, 0);
                        pstmt.setDouble(7, pt.getHourlyRate());
                        pstmt.setDouble(8, pt.getHoursWorked());
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addEmployee(Employee emp) {
        emp = new EmployeeWithId(emp, nextId++);
        employees.add(emp);
        saveEmployees();
        syncWithDatabase();
    }
    
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Employee getEmployee(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }
    
    public void updateEmployee(int id, Employee updatedEmp) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                employees.set(i, new EmployeeWithId(updatedEmp, id));
                saveEmployees();
                syncWithDatabase();
                return;
            }
        }
    }
    
    public void deleteEmployee(int id) {
        employees.removeIf(emp -> emp.getId() == id);
        saveEmployees();
        syncWithDatabase();
    }
    
    private class EmployeeWithId extends Employee {
        private Employee original;
        private int id;
        
        public EmployeeWithId(Employee original, int id) {
            super(id, original.getName(), original.getEmail(), original.getPhone());
            this.original = original;
            this.id = id;
        }
        
        @Override
        public double calculateSalary() {
            return original.calculateSalary();
        }
        
        @Override
        public String toString() {
            return original.toString().replaceFirst("ID: \\d+", "ID: " + id);
        }
        
        @Override
        public String toFileString() {
            return original.toFileString().replaceFirst("^\\d+", String.valueOf(id));
        }
    }
}