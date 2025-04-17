
import java.sql.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ericamoakoagyare
 */
public class MessageDAO {
    private Connection conn;

    public MessageDAO() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/encryption_db", "root", "");
    }

    public void saveMessage(String title, String encryptedText) throws SQLException {
        String sql = "INSERT INTO messages (title, encrypted_text) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setString(2, encryptedText);
        stmt.executeUpdate();
    }

    public String getMessageByTitle(String title) throws SQLException {
        String sql = "SELECT encrypted_text FROM messages WHERE title = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getString("encrypted_text");
        return null;
    }
    
}
