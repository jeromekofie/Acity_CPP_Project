// Source code is decompiled from a .class file using FernFlower decompiler.
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GPA {
   private JFrame frame;
   private JTextField nameField;
   private JTextField IDField;
   private JTextField courseField;
   private JTextField gradesField;
   private JTextField creditHoursField;
   private JTable table;
   private DefaultTableModel tableModel;
   private File file = new File("gpa_records.txt");

   public GPA() {
      try {
         if (!this.file.exists()) {
            this.file.createNewFile();
         }
      } catch (IOException var11) {
         JOptionPane.showMessageDialog((Component)null, "Error creating file.");
      }

      this.frame = new JFrame("GPA Calculator");
      this.frame.setSize(700, 500);
      this.frame.setLocationRelativeTo((Component)null);
      this.frame.setDefaultCloseOperation(3);
      JPanel var1 = new JPanel(new BorderLayout(10, 10));
      var1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      JPanel var2 = new JPanel(new GridLayout(5, 2, 10, 10));
      var2.setBorder(BorderFactory.createTitledBorder("Student Details"));
      var2.add(new JLabel("Name"));
      this.nameField = new JTextField();
      var2.add(this.nameField);
      var2.add(new JLabel("ID"));
      this.IDField = new JTextField();
      var2.add(this.IDField);
      var2.add(new JLabel("Course"));
      this.courseField = new JTextField();
      var2.add(this.courseField);
      var2.add(new JLabel("Grades"));
      this.gradesField = new JTextField();
      var2.add(this.gradesField);
      var2.add(new JLabel("Credit Hours"));
      this.creditHoursField = new JTextField();
      var2.add(this.creditHoursField);
      JPanel var3 = new JPanel(new GridLayout(1, 4, 10, 10));
      var3.setBorder(BorderFactory.createTitledBorder("Actions"));
      JButton var4 = new JButton("Add");
      JButton var5 = new JButton("Calculate GPA");
      JButton var6 = new JButton("Load Data");
      JButton var7 = new JButton("Clear");
      var3.add(var4);
      var3.add(var5);
      var3.add(var6);
      var3.add(var7);
      this.tableModel = new DefaultTableModel(new String[]{"Name", "ID", "Course", "Grades", "Credit Hours", "GPA"}, 0);
      this.table = new JTable(this.tableModel);
      JScrollPane var8 = new JScrollPane(this.table);
      JPanel var9 = new JPanel(new BorderLayout());
      var9.setBorder(BorderFactory.createTitledBorder("Student Records"));
      var9.add(var8, "Center");
      JPanel var10 = new JPanel(new BorderLayout(10, 10));
      var10.add(var2, "Center");
      var10.add(var3, "South");
      var1.add(var10, "North");
      var1.add(var9, "Center");
      this.frame.add(var1);
      this.frame.setVisible(true);
      var4.addActionListener((var1x) -> {
         this.addGPA();
      });
      var5.addActionListener((var1x) -> {
         this.calculateGPA();
      });
      var6.addActionListener((var1x) -> {
         this.loadData();
      });
      var7.addActionListener((var1x) -> {
         this.clearFields();
      });
   }

   private void addGPA() {
      try {
         BufferedWriter var1 = new BufferedWriter(new FileWriter(this.file, true));

         try {
            String var2 = this.nameField.getText();
            String var3 = this.IDField.getText();
            String var4 = this.courseField.getText();
            double var5 = Double.parseDouble(this.gradesField.getText());
            int var7 = Integer.parseInt(this.creditHoursField.getText());
            double var8 = var5 * (double)var7 / (double)var7;
            var1.write(var2 + "," + var3 + "," + var4 + "," + var5 + "," + var7 + "," + var8);
            var1.newLine();
            JOptionPane.showMessageDialog(this.frame, "Details Added");
            this.clearFields();
         } catch (Throwable var11) {
            try {
               var1.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         var1.close();
      } catch (NumberFormatException | IOException var12) {
         JOptionPane.showMessageDialog(this.frame, "Error adding details. Please check your input.");
      }

   }

   private void calculateGPA() {
      try {
         int var1 = 0;
         double var2 = 0.0;

         for(int var4 = 0; var4 < this.tableModel.getRowCount(); ++var4) {
            double var5 = Double.parseDouble(this.tableModel.getValueAt(var4, 3).toString());
            int var7 = Integer.parseInt(this.tableModel.getValueAt(var4, 4).toString());
            var2 += var5 * (double)var7;
            var1 += var7;
         }

         double var9 = var2 / (double)var1;
         JFrame var10000 = this.frame;
         Object[] var10002 = new Object[]{var9};
         JOptionPane.showMessageDialog(var10000, "Overall GPA: " + String.format("%.2f", var10002));
      } catch (Exception var8) {
         JOptionPane.showMessageDialog(this.frame, "Error calculating GPA.");
      }

   }

   private void loadData() {
      try {
         BufferedReader var1 = new BufferedReader(new FileReader(this.file));

         try {
            this.tableModel.setRowCount(0);

            String var2;
            while((var2 = var1.readLine()) != null) {
               String[] var3 = var2.split(",");
               if (var3.length == 6) {
                  this.tableModel.addRow(var3);
               }
            }
         } catch (Throwable var5) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         var1.close();
      } catch (IOException var6) {
         JOptionPane.showMessageDialog(this.frame, "Error loading data.");
      }

   }

   private void clearFields() {
      this.nameField.setText("");
      this.IDField.setText("");
      this.courseField.setText("");
      this.gradesField.setText("");
      this.creditHoursField.setText("");
   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(GPA::new);
   }
}
