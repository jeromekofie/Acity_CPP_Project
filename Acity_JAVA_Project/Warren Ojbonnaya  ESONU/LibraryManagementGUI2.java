import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.List; // Import List
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
// Assuming Book, Member, StudentMember, TeacherMember, Library, DatabaseConnection
// are in the same package or correctly imported if in different packages.

public class LibraryManagementGUI2 {
    // Use the actual Library class that interacts with DatabaseConnection
    private Library library = new Library(); // This now refers to your external Library class
    private JTable table;
    private DefaultTableModel tableModel;
    private Image backgroundImage;

    // --- Custom Viewport Class ---
    class ImageViewport extends JViewport {
        private Image img;

        public ImageViewport(Image img) {
            this.img = img;
            setOpaque(false); // Make viewport transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (img != null) {
                // Draw the image, scaling it to fill the viewport
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
            super.paintComponent(g);
        }
    }

    // --- Custom Transparent Cell Renderer ---
     class TransparentCellRenderer extends DefaultTableCellRenderer {
        private Color backgroundColor = new Color(255, 255, 255, 180); // Semi-transparent white

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
                c.setBackground(backgroundColor);
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                 c.setBackground(backgroundColor);
                c.setForeground(table.getForeground());
            }
            c.setForeground(Color.BLACK);

            return c;
        }
    }

    // --- Main Method - Application Entry Point ---
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        SwingUtilities.invokeLater(() -> new LibraryManagementGUI2().createAndShowGUI());
    }

    // --- GUI Creation Method ---
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Warren's Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 550);

        // --- Load Background Image ---
        // !!! IMPORTANT: Update this path if necessary !!!
        String bgImagePath = "C:\\Users\\abiko\\OneDrive\\Desktop\\Warren/R.jpg"; //
        File bgImageFile = new File(bgImagePath); //
        if (bgImageFile.exists()) { //
            backgroundImage = new ImageIcon(bgImagePath).getImage(); //
        } else {
            System.err.println("Warning: Background image not found at " + bgImagePath); //
            backgroundImage = null; //
        }

        // --- Look and Feel (Optional) ---
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set Look and Feel: " + e); //
        }

        // --- Window Icon ---
        // !!! IMPORTANT: Update this path if necessary !!!
        String iconPath = "C:\\Users\\abiko\\OneDrive\\Desktop\\Warren/library.png"; //
        File iconFile = new File(iconPath); //
        if (iconFile.exists()) { //
            frame.setIconImage(new ImageIcon(iconPath).getImage()); //
        } else {
            System.err.println("Warning: Window icon not found at " + iconPath); //
        }

        // --- Global Font Settings (Optional) ---
        UIManager.put("Label.font", new Font("Times New Roman", Font.PLAIN, 14)); //
        UIManager.put("Button.font", new Font("Times New Roman", Font.PLAIN, 14)); //
        UIManager.put("Table.font", new Font("Times New Roman", Font.PLAIN, 14)); //
        UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 14)); //

        // --- Main Panel ---
        JPanel panel = new JPanel(new BorderLayout(10, 10)); //
        panel.setBackground(Color.BLACK); //

        // --- Title Panel ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); //
        titlePanel.setBackground(Color.BLACK); //
        JLabel titleLabel = new JLabel("Warren's Library"); //
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 28)); //
        titleLabel.setForeground(Color.WHITE); //
        // Add icon to title label (ensure path is correct)
        // !!! IMPORTANT: Update this path if necessary !!!
        String labelIconPath = "C:\\Users\\abiko\\OneDrive\\Desktop\\Warren/library.png"; //
        File labelIconFile = new File(labelIconPath); //
        if (labelIconFile.exists()) { //
            titleLabel.setIcon(new ImageIcon(labelIconPath)); //
        } else {
            System.err.println("Warning: Title icon not found at " + labelIconPath); //
        }
        titleLabel.setHorizontalTextPosition(SwingConstants.RIGHT); //
        titleLabel.setIconTextGap(15); //
        titlePanel.add(titleLabel); //
        panel.add(titlePanel, BorderLayout.NORTH); //

        // --- Table Setup ---
        String[] columnNames = { "Title", "Name", "Author", "Type", "Status", "ID" }; //
        tableModel = new DefaultTableModel(columnNames, 0) { //
            @Override
            public boolean isCellEditable(int row, int column) { //
                return false; // Cells are not editable
            }
        };
        table = new JTable(tableModel); //
        table.setOpaque(false); // Table itself is transparent
        table.setForeground(Color.BLACK); // Text color
        table.setGridColor(Color.GRAY); //
        table.setFillsViewportHeight(true); //
        table.setSelectionBackground(Color.LIGHT_GRAY); //
        table.setSelectionForeground(Color.BLACK); //
        table.setRowHeight(25); //

        // Apply the transparent cell renderer to all columns
        table.setDefaultRenderer(Object.class, new TransparentCellRenderer()); //

        // Style the table header
         TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer(); //
        if (headerRenderer instanceof JLabel) { //
            ((JLabel) headerRenderer).setOpaque(true); //
             ((JLabel) headerRenderer).setHorizontalAlignment(SwingConstants.CENTER); //
            table.getTableHeader().setBackground(new Color(50, 50, 50, 200)); //
            table.getTableHeader().setForeground(Color.WHITE); //
             table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14)); //
        } else {
            table.getTableHeader().setBackground(Color.DARK_GRAY); //
            table.getTableHeader().setForeground(Color.WHITE); //
        }


        // --- Scroll Pane Setup ---
        JScrollPane scrollPane = new JScrollPane(); //
        // Set custom viewport if background image exists
        if (backgroundImage != null) { //
            scrollPane.setViewport(new ImageViewport(backgroundImage)); //
        } else {
             scrollPane.getViewport().setOpaque(true); //
             scrollPane.getViewport().setBackground(Color.WHITE); // Fallback
        }
        scrollPane.getViewport().setView(table); // Tell viewport to show the table
        scrollPane.setOpaque(false); // ScrollPane container is transparent
        panel.add(scrollPane, BorderLayout.CENTER); //

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10)); //
        buttonPanel.setBackground(Color.BLACK); //

        // --- Create Buttons ---
        // !!! IMPORTANT: Update this base path and icon names if necessary !!!
        String basePath = "C:\\Users\\abiko\\OneDrive\\Desktop\\Warren/"; //

        JButton addBookButton = createButton("Add Book", basePath + "book.png"); //
        addBookButton.addActionListener(e -> addBook()); //
        buttonPanel.add(addBookButton); //

        JButton addMemberButton = createButton("Add Member", basePath + "group.png"); //
        addMemberButton.addActionListener(e -> addMember()); //
        buttonPanel.add(addMemberButton); //

        JButton displayBooksButton = createButton("Display Books", basePath + "books.png"); //
        displayBooksButton.addActionListener(e -> displayBooksInTable()); //
        buttonPanel.add(displayBooksButton); //

        JButton displayMembersButton = createButton("Display Members", basePath + "staff.png"); //
        displayMembersButton.addActionListener(e -> displayMembersInTable()); //
        buttonPanel.add(displayMembersButton); //

        JButton borrowBookButton = createButton("Borrow Book", basePath + "borrow.png"); //
        borrowBookButton.addActionListener(e -> borrowBook()); //
        buttonPanel.add(borrowBookButton); //

        JButton returnBookButton = createButton("Return Book", basePath + "back.png"); //
        returnBookButton.addActionListener(e -> returnBook()); //
        buttonPanel.add(returnBookButton); //

        JButton deleteBookButton = createButton("Delete Book", basePath + "bin.png"); //
        deleteBookButton.addActionListener(e -> deleteBook()); //
        buttonPanel.add(deleteBookButton); //

        // **** Changed back to Delete Row button ****
        JButton deleteRowButton = createButton("Delete Row", basePath + "cross.png"); // Changed text and icon path (assuming cross.png exists)
        deleteRowButton.setToolTipText("Removes selected row from view only, does not delete from database."); //
        deleteRowButton.addActionListener(e -> deleteSelectedRowFromView()); // Changed action listener
        buttonPanel.add(deleteRowButton); //
        // **** End of change ****

        JButton exitButton = createButton("Exit", basePath + "logout.png"); //
        exitButton.addActionListener(e -> System.exit(0)); //
        buttonPanel.add(exitButton); //

        panel.add(buttonPanel, BorderLayout.SOUTH); //

        // --- Finalize Frame ---
        frame.add(panel); //
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true); //

        // Initial data load into the table
        refreshTableData(); //
    }

    // --- Helper Method for Button Creation ---
    private JButton createButton(String text, String iconPath) { //
        JButton button = new JButton(text); //
        button.setFont(new Font("Times New Roman", Font.BOLD, 14)); //
        button.setBackground(Color.DARK_GRAY); //
        button.setForeground(Color.WHITE); //
        button.setFocusPainted(false); //

        File iconFile = new File(iconPath); //
        if (iconFile.exists()) { //
            button.setIcon(new ImageIcon(iconFile.getAbsolutePath())); //
        } else {
            System.err.println("Warning: Button icon not found at: " + iconPath); //
        }
        button.setVerticalTextPosition(SwingConstants.BOTTOM); //
        button.setHorizontalTextPosition(SwingConstants.CENTER); //
        button.setIconTextGap(5); //
        return button; //
    }

    // --- Combined Data Refresh Method ---
    // Clears the table and reloads all books and members from the database
    private void refreshTableData() { //
        tableModel.setRowCount(0); // Clear table
        displayBooksInTableInternal(); // Load books from DB
        displayMembersInTableInternal(); // Load members from DB
    }

    // --- Action Methods ---

    private void addBook() { //
        String title = JOptionPane.showInputDialog(null, "Enter Book Title:", "Add Book", JOptionPane.PLAIN_MESSAGE); //
        if (title == null || title.trim().isEmpty()) return; //
        String author = JOptionPane.showInputDialog(null, "Enter Author:", "Add Book", JOptionPane.PLAIN_MESSAGE); //
        if (author == null || author.trim().isEmpty()) return; //
        String idStr = JOptionPane.showInputDialog(null, "Enter Book ID:", "Add Book", JOptionPane.PLAIN_MESSAGE); //
        if (idStr == null) return; //

        try {
            int id = Integer.parseInt(idStr.trim()); //
            library.addBook(title.trim(), author.trim(), id); // Calls external Library method
            refreshTableData(); // Refresh table view
        } catch (NumberFormatException ex) { //
            JOptionPane.showMessageDialog(null, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE); //
        }
    }

    private void addMember() { //
        String name = JOptionPane.showInputDialog(null, "Enter Member Name:", "Add Member", JOptionPane.PLAIN_MESSAGE); //
        if (name == null || name.trim().isEmpty()) return; //
        String idStr = JOptionPane.showInputDialog(null, "Enter Member ID:", "Add Member", JOptionPane.PLAIN_MESSAGE); //
        if (idStr == null) return; //

        String[] options = { "Student", "Teacher" }; //
        int typeChoice = JOptionPane.showOptionDialog(null, "Select Member Type:", "Add Member", //
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]); //
        if (typeChoice == JOptionPane.CLOSED_OPTION) return; //

        try {
            int id = Integer.parseInt(idStr.trim()); //
            Member member; // Uses external Member class
            if (typeChoice == 0) { // Student
                member = new StudentMember(name.trim(), id); // Uses external StudentMember class
            } else { // Teacher
                member = new TeacherMember(name.trim(), id); // Uses external TeacherMember class
            }
            library.addMember(member); // Calls external Library method
            refreshTableData(); // Refresh table view
        } catch (NumberFormatException ex) { //
            JOptionPane.showMessageDialog(null, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE); //
        }
    }

    // Displays only books by clearing table and reloading books
    private void displayBooksInTable() { //
        tableModel.setRowCount(0); //
        displayBooksInTableInternal(); //
    }

    // Internal helper to add book rows from the database
    private void displayBooksInTableInternal() { //
        List<Book> books = library.getBooks(); // Uses external Library & Book classes
        for (Book book : books) { //
            tableModel.addRow(new Object[] { //
                    book.getTitle(), //
                    "", //
                    book.getAuthor(), //
                    "Book", //
                    book.getAvailability() ? "Available" : "Borrowed", //
                    book.getId() //
            });
        }
    }

    // Displays only members by clearing table and reloading members
    private void displayMembersInTable() { //
        tableModel.setRowCount(0); //
        displayMembersInTableInternal(); //
    }

    // Internal helper to add member rows from the database
    private void displayMembersInTableInternal() { //
        List<Member> members = library.getMembers(); // Uses external Library & Member classes
        for (Member member : members) { //
            String type = "Member"; //
            if (member instanceof StudentMember) type = "Student"; // Uses external StudentMember
            else if (member instanceof TeacherMember) type = "Teacher"; // Uses external TeacherMember

            tableModel.addRow(new Object[] { //
                    "", //
                    member.name, // Direct access - consider getter if changing Member class
                    "", //
                    type, //
                    "", //
                    member.getId() //
            });
        }
    }

     private void borrowBook() { //
        String bookIdStr = JOptionPane.showInputDialog(null, "Enter Book ID to Borrow:", "Borrow Book", JOptionPane.PLAIN_MESSAGE); //
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) return; //
        String memberIdStr = JOptionPane.showInputDialog(null, "Enter Your Member ID:", "Borrow Book", JOptionPane.PLAIN_MESSAGE); //
        if (memberIdStr == null || memberIdStr.trim().isEmpty()) return; //

        try {
            int bookId = Integer.parseInt(bookIdStr.trim()); //
            int memberId = Integer.parseInt(memberIdStr.trim()); //
            library.borrowBook(bookId, memberId); // Calls external Library method
            refreshTableData(); //
        } catch (NumberFormatException ex) { //
            JOptionPane.showMessageDialog(null, "Invalid ID. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE); //
        }
    }

    private void returnBook() { //
        String bookIdStr = JOptionPane.showInputDialog(null, "Enter Book ID to Return:", "Return Book", JOptionPane.PLAIN_MESSAGE); //
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) return; //
        String memberIdStr = JOptionPane.showInputDialog(null, "Enter Your Member ID:", "Return Book", JOptionPane.PLAIN_MESSAGE); //
         if (memberIdStr == null || memberIdStr.trim().isEmpty()) return; //

        try {
            int bookId = Integer.parseInt(bookIdStr.trim()); //
            int memberId = Integer.parseInt(memberIdStr.trim()); //
            library.returnBook(bookId, memberId); // Calls external Library method
            refreshTableData(); //
        } catch (NumberFormatException ex) { //
            JOptionPane.showMessageDialog(null, "Invalid ID. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE); //
        }
    }

    private void deleteBook() { //
        String bookIdStr = JOptionPane.showInputDialog(null, "Enter Book ID to Delete:", "Delete Book", JOptionPane.WARNING_MESSAGE); //
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) return; //

        try {
            int bookId = Integer.parseInt(bookIdStr.trim()); //
            int confirmation = JOptionPane.showConfirmDialog(null, //
                    "Are you sure you want to delete book with ID " + bookId + "?\nThis action permanently removes it from the database.", //
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); //

            if (confirmation == JOptionPane.YES_OPTION) { //
                library.deleteBook(bookId); // Calls external Library method
                refreshTableData(); //
            }
        } catch (NumberFormatException ex) { //
            JOptionPane.showMessageDialog(null, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE); //
        }
    }

    // **** Added back the method to delete the selected row from the view ****
    private void deleteSelectedRowFromView() { //
        int selectedRow = table.getSelectedRow(); //
        if (selectedRow != -1) { // Check if a row is actually selected
            // Optional confirmation:
            int confirmation = JOptionPane.showConfirmDialog(null, //
                    "Remove selected row from the table view?\n(This does NOT delete from the database)", //
                    "Confirm Row Removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //
            if (confirmation == JOptionPane.YES_OPTION) { //
                tableModel.removeRow(selectedRow); // Removes row only from the table model (view)
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row in the table first.", "No Row Selected", //
                    JOptionPane.WARNING_MESSAGE); //
        }
    }
    // **** End of added back method ****


} // End of LibraryManagementGUI2 class