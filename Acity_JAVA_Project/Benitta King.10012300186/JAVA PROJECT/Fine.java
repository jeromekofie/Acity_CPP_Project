import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Fine {
    private int id;
    private int loanId;
    private double amount;
    private LocalDate issuedDate;
    private boolean paid;
    
    public Fine() {}
    
    public Fine(int loanId, double amount, LocalDate issuedDate) {
        this.loanId = loanId;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.paid = false;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    
    // Database operations
    public boolean save() {
        String sql = "INSERT INTO fines (loan_id, amount, issued_date, paid) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, loanId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, Date.valueOf(issuedDate));
            stmt.setBoolean(4, paid);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean markAsPaid() {
        String sql = "UPDATE fines SET paid = TRUE WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Fine> getUnpaidFines() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE paid = FALSE";
        
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Fine fine = new Fine();
                fine.setId(rs.getInt("id"));
                fine.setLoanId(rs.getInt("loan_id"));
                fine.setAmount(rs.getDouble("amount"));
                fine.setIssuedDate(rs.getDate("issued_date").toLocalDate());
                fine.setPaid(rs.getBoolean("paid"));
                fines.add(fine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fines;
    }
}