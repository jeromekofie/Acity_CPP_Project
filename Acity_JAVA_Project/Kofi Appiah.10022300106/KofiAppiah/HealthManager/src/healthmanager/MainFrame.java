/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthmanager;

/**
 *
 * @author User
 */
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {
    private static DefaultTableModel tableModel;

    public MainFrame() {
        setTitle("Health Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"ID", "Name", "Age", "Weight", "Height", "BMI", "Blood Type", "Allergies"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        RecordFormPanel formPanel = new RecordFormPanel(table, tableModel);
        add(formPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        reloadTable(tableModel);
        setVisible(true);
    }

    public static void reloadTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM health_records");
            while (rs.next()) {
                Object[] row = new Object[8];
                for (int i = 0; i < 8; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            conn.close();
        } catch (SQLException ex) {
        }
    }
}
