import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable booksTable, membersTable, loansTable, finesTable;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Brown theme colors
    private final Color BROWN_BACKGROUND = new Color(222, 184, 135); // Burlywood
    private final Color DARK_BROWN = new Color(101, 67, 33);
    private final Color LIGHT_BROWN = new Color(210, 180, 140);
    private final Color BROWN_TAB = new Color(160, 82, 45); // Sienna
    
    public DashboardUI() {
        setTitle("Library System - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Apply brown theme to the main frame
        getContentPane().setBackground(BROWN_BACKGROUND);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BROWN_BACKGROUND);
        
        // Create control panel for logout and exit buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(BROWN_BACKGROUND);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add logout button
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            this.dispose();
            //Code to return to login screen would go here//
            new LoginUI().setVisible(true); 
        });
        
        // Add exit button
        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        
        controlPanel.add(logoutButton);
        controlPanel.add(exitButton);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BROWN_BACKGROUND);
        tabbedPane.setForeground(DARK_BROWN);
        
        // Style the tabbed pane
        UIManager.put("TabbedPane.selected", BROWN_TAB);
        UIManager.put("TabbedPane.borderHightlightColor", DARK_BROWN);
        UIManager.put("TabbedPane.contentAreaColor", LIGHT_BROWN);
        
        initBooksTab();
        initMembersTab();
        initLoansTab();
        initFinesTab();
        
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
        loadAllData();
    }

    // ... [All other methods remain exactly the same as in the original code] ...

    private void initBooksTab() {
        JPanel booksPanel = new JPanel(new BorderLayout());
        booksPanel.setBackground(BROWN_BACKGROUND);
        booksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Action buttons at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        
        JButton addBookButton = createStyledButton("Add New Book");
        JButton editBookButton = createStyledButton("Edit Book");
        JButton deleteBookButton = createStyledButton("Delete Book");
        JButton refreshButton = createStyledButton("Refresh");
        
        addBookButton.addActionListener(e -> showAddBookDialog());
        editBookButton.addActionListener(e -> editSelectedBook());
        deleteBookButton.addActionListener(e -> deleteSelectedBook());
        refreshButton.addActionListener(e -> loadBooksData());
        
        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(refreshButton);
        
        // Table
        booksTable = new JTable();
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(booksTable);
        
        // Search panel at the bottom (smaller)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBackground(BROWN_BACKGROUND);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JTextField searchField = new JTextField(15); // Smaller width
        searchField.setBackground(Color.WHITE);
        JButton searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> searchBooks(searchField.getText()));
        
        searchPanel.add(new JLabel("Search (ID/Title/ISBN):"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        booksPanel.add(buttonPanel, BorderLayout.NORTH);
        booksPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        booksPanel.add(searchPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Books", booksPanel);
    }
    
    private void initMembersTab() {
        JPanel membersPanel = new JPanel(new BorderLayout());
        membersPanel.setBackground(BROWN_BACKGROUND);
        membersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addMemberButton = createStyledButton("Add New Member");
        membersTable = new JTable();
        styleTable(membersTable);
        
        addMemberButton.addActionListener(e -> showAddMemberDialog());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        buttonPanel.add(addMemberButton);
        
        membersPanel.add(new JScrollPane(membersTable), BorderLayout.CENTER);
        membersPanel.add(buttonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Members", membersPanel);
    }
    
    private void initLoansTab() {
        JPanel loansPanel = new JPanel(new BorderLayout());
        loansPanel.setBackground(BROWN_BACKGROUND);
        loansPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton issueLoanButton = createStyledButton("Issue New Loan");
        JButton returnBookButton = createStyledButton("Return Book");
        loansTable = new JTable();
        styleTable(loansTable);
        
        issueLoanButton.addActionListener(e -> showIssueLoanDialog());
        returnBookButton.addActionListener(e -> returnSelectedBook());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        buttonPanel.add(issueLoanButton);
        buttonPanel.add(returnBookButton);
        
        loansPanel.add(new JScrollPane(loansTable), BorderLayout.CENTER);
        loansPanel.add(buttonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Loans", loansPanel);
    }
    
    private void initFinesTab() {
        JPanel finesPanel = new JPanel(new BorderLayout());
        finesPanel.setBackground(BROWN_BACKGROUND);
        finesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton calculateFinesButton = createStyledButton("Calculate Overdue Fines");
        JButton payFineButton = createStyledButton("Mark as Paid");
        finesTable = new JTable();
        styleTable(finesTable);
        
        calculateFinesButton.addActionListener(e -> calculateFines());
        payFineButton.addActionListener(e -> paySelectedFine());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        buttonPanel.add(calculateFinesButton);
        buttonPanel.add(payFineButton);
        
        finesPanel.add(new JScrollPane(finesTable), BorderLayout.CENTER);
        finesPanel.add(buttonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Fines", finesPanel);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(DARK_BROWN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void styleTable(JTable table) {
        table.setBackground(LIGHT_BROWN);
        table.setForeground(DARK_BROWN);
        table.setGridColor(DARK_BROWN);
        table.setSelectionBackground(DARK_BROWN);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setBackground(DARK_BROWN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
    }
    
    private void loadAllData() {
        loadBooksData();
        loadMembersData();
        loadLoansData();
        loadFinesData();
    }
    
    private void searchBooks(String query) {
        List<Book> books = Book.search(query);
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("ISBN");
        model.addColumn("Available");
        
        for (Book book : books) {
            model.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.isAvailable()
            });
        }
        
        booksTable.setModel(model);
        booksTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        booksTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    
    private void loadBooksData() {
        List<Book> books = Book.getAll();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("ISBN");
        model.addColumn("Available");
        
        for (Book book : books) {
            model.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.isAvailable()
            });
        }
        
        booksTable.setModel(model);
        booksTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        booksTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    
    private void editSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) booksTable.getValueAt(selectedRow, 0);
            String title = (String) booksTable.getValueAt(selectedRow, 1);
            String author = (String) booksTable.getValueAt(selectedRow, 2);
            String isbn = (String) booksTable.getValueAt(selectedRow, 3);
            boolean available = (boolean) booksTable.getValueAt(selectedRow, 4);
            
            JDialog dialog = new JDialog(this, "Edit Book", true);
            dialog.setSize(500, 350); // Wider dialog
            dialog.setLocationRelativeTo(this); // Center to parent
            JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(BROWN_BACKGROUND);
            dialog.add(contentPanel);
            
            JTextField titleField = new JTextField(title);
            JTextField authorField = new JTextField(author);
            JTextField isbnField = new JTextField(isbn);
            JCheckBox availableCheckbox = new JCheckBox("Available", available);
            
            contentPanel.add(createStyledLabel("Title:"));
            contentPanel.add(titleField);
            contentPanel.add(createStyledLabel("Author:"));
            contentPanel.add(authorField);
            contentPanel.add(createStyledLabel("ISBN:"));
            contentPanel.add(isbnField);
            contentPanel.add(createStyledLabel("Available:"));
            contentPanel.add(availableCheckbox);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(BROWN_BACKGROUND);
            
            JButton saveButton = createStyledButton("Save");
            saveButton.addActionListener(e -> {
                Book book = new Book();
                book.setId(id);
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setIsbn(isbnField.getText());
                book.setAvailable(availableCheckbox.isSelected());
                
                if (book.update()) {
                    JOptionPane.showMessageDialog(dialog, "Book updated successfully!");
                    loadBooksData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            JButton cancelButton = createStyledButton("Cancel");
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            contentPanel.add(new JLabel()); // Empty cell for layout
            contentPanel.add(buttonPanel);
            
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(DARK_BROWN);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }
    
    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) booksTable.getValueAt(selectedRow, 0);
            String title = (String) booksTable.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + title + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                Book book = new Book();
                book.setId(id);
                
                if (book.delete()) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                    loadBooksData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setSize(500, 350); // Wider dialog
        dialog.setLocationRelativeTo(this); // Center to parent
        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BROWN_BACKGROUND);
        dialog.add(contentPanel);
        
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JCheckBox availableCheckbox = new JCheckBox("Available", true);
        
        contentPanel.add(createStyledLabel("Title:"));
        contentPanel.add(titleField);
        contentPanel.add(createStyledLabel("Author:"));
        contentPanel.add(authorField);
        contentPanel.add(createStyledLabel("ISBN:"));
        contentPanel.add(isbnField);
        contentPanel.add(createStyledLabel("Available:"));
        contentPanel.add(availableCheckbox);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        
        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(e -> {
            Book book = new Book(
                titleField.getText(),
                authorField.getText(),
                isbnField.getText()
            );
            book.setAvailable(availableCheckbox.isSelected());
            
            if (book.save()) {
                JOptionPane.showMessageDialog(dialog, "Book added successfully!");
                loadBooksData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add book", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(new JLabel()); // Empty cell for layout
        contentPanel.add(buttonPanel);
        
        dialog.setVisible(true);
    }
    
    private void loadMembersData() {
        List<Member> members = Member.getAll();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Phone");
        model.addColumn("Has Unpaid Fines");
        
        for (Member member : members) {
            model.addRow(new Object[]{
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.hasUnpaidFines()
            });
        }
        
        membersTable.setModel(model);
        
        membersTable.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Boolean && (Boolean)value) {
                    c.setBackground(new Color(255, 200, 200)); // Light red
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });
    }
    
    private void showAddMemberDialog() {
        JDialog dialog = new JDialog(this, "Add New Member", true);
        dialog.setSize(500, 300); // Wider dialog
        dialog.setLocationRelativeTo(this); // Center to parent
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BROWN_BACKGROUND);
        dialog.add(contentPanel);
        
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        
        contentPanel.add(createStyledLabel("Name:"));
        contentPanel.add(nameField);
        contentPanel.add(createStyledLabel("Email:"));
        contentPanel.add(emailField);
        contentPanel.add(createStyledLabel("Phone:"));
        contentPanel.add(phoneField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        
        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(e -> {
            Member member = new Member(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText()
            );
            
            if (member.save()) {
                JOptionPane.showMessageDialog(dialog, "Member added successfully!");
                loadMembersData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(new JLabel()); // Empty cell for layout
        contentPanel.add(buttonPanel);
        
        dialog.setVisible(true);
    }
    
    private void loadLoansData() {
        List<Loan> loans = Loan.getActiveLoans();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Book ID");
        model.addColumn("Member ID");
        model.addColumn("Loan Date");
        model.addColumn("Due Date");
        
        for (Loan loan : loans) {
            model.addRow(new Object[]{
                loan.getId(),
                loan.getBookId(),
                loan.getMemberId(),
                loan.getLoanDate().format(dateFormatter),
                loan.getDueDate().format(dateFormatter)
            });
        }
        
        loansTable.setModel(model);
    }
    
    private void showIssueLoanDialog() {
        JDialog dialog = new JDialog(this, "Issue New Loan", true);
        dialog.setSize(500, 300); // Wider dialog
        dialog.setLocationRelativeTo(this); // Center to parent
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BROWN_BACKGROUND);
        dialog.add(contentPanel);
        
        List<Book> availableBooks = Book.getAvailableBooks();
        List<Member> members = Member.getAll();
        
        JComboBox<String> bookCombo = new JComboBox<>();
        JComboBox<String> memberCombo = new JComboBox<>();
        JTextField daysField = new JTextField("14"); // Default 14-day loan
        
        // Populate book combo with available books
        for (Book book : availableBooks) {
            bookCombo.addItem(book.getTitle() + " by " + book.getAuthor() + " (ID: " + book.getId() + ")");
        }
        
        // Populate member combo
        for (Member member : members) {
            memberCombo.addItem(member.getName() + " (ID: " + member.getId() + ")");
        }
        
        contentPanel.add(createStyledLabel("Book:"));
        contentPanel.add(bookCombo);
        contentPanel.add(createStyledLabel("Member:"));
        contentPanel.add(memberCombo);
        contentPanel.add(createStyledLabel("Loan Duration (days):"));
        contentPanel.add(daysField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BROWN_BACKGROUND);
        
        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(e -> {
            try {
                if (availableBooks.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "No available books to loan", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int selectedBookIndex = bookCombo.getSelectedIndex();
                Book selectedBook = availableBooks.get(selectedBookIndex);
                
                int selectedMemberIndex = memberCombo.getSelectedIndex();
                Member selectedMember = members.get(selectedMemberIndex);
                
                // Check for unpaid fines
                if (selectedMember.hasUnpaidFines()) {
                    int override = JOptionPane.showConfirmDialog(dialog,
                        "Member has unpaid fines. Override restriction?",
                        "Fine Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                        
                    if (override != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                
                int days = Integer.parseInt(daysField.getText());
                LocalDate loanDate = LocalDate.now();
                LocalDate dueDate = loanDate.plusDays(days);
                
                Loan loan = new Loan(selectedBook.getId(), selectedMember.getId(), loanDate, dueDate);
                
                if (loan.save()) {
                    selectedBook.setAvailable(false);
                    selectedBook.updateAvailability();
                    JOptionPane.showMessageDialog(dialog, "Loan issued successfully!");
                    loadLoansData();
                    loadBooksData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to issue loan", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number of days", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(new JLabel()); // Empty cell for layout
        contentPanel.add(buttonPanel);
        
        dialog.setVisible(true);
    }
    
    private void returnSelectedBook() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int loanId = (int) loansTable.getValueAt(selectedRow, 0);
                int bookId = (int) loansTable.getValueAt(selectedRow, 1);
                
                // Get the due date from the table
                String dueDateStr = (String) loansTable.getValueAt(selectedRow, 4);
                LocalDate dueDate = LocalDate.parse(dueDateStr, dateFormatter);
                
                Loan loan = new Loan();
                loan.setId(loanId);
                
                if (loan.returnBook()) {
                    Book book = new Book();
                    book.setId(bookId);
                    book.setAvailable(true);
                    book.updateAvailability();
                    
                    // Calculate fine if overdue
                    LocalDate returnDate = LocalDate.now();
                    double fineAmount = FineCalculator.calculateFine(dueDate, returnDate);
                    
                    if (fineAmount > 0) {
                        Fine fine = new Fine(loanId, fineAmount, returnDate);
                        fine.save();
                    }
                    
                    JOptionPane.showMessageDialog(this, "Book returned successfully!");
                    loadLoansData();
                    loadBooksData();
                    loadFinesData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to return book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a loan to return", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadFinesData() {
        List<Fine> fines = Fine.getUnpaidFines();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Double.class : super.getColumnClass(column);
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Loan ID");
        model.addColumn("Amount");
        model.addColumn("Issued Date");
        
        for (Fine fine : fines) {
            model.addRow(new Object[]{
                fine.getId(),
                fine.getLoanId(),
                String.format("$%.2f", fine.getAmount()), // Format as currency
                fine.getIssuedDate().format(dateFormatter)
            });
        }
        
        finesTable.setModel(model);
        
        // Set custom renderer for the amount column to right-align currency
        finesTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel)c).setHorizontalAlignment(JLabel.RIGHT);
                return c;
            }
        });
    }
    
    private void calculateFines() {
        List<Loan> activeLoans = Loan.getActiveLoans();
        LocalDate today = LocalDate.now();
        int finesCreated = 0;
        
        for (Loan loan : activeLoans) {
            if (today.isAfter(loan.getDueDate())) {
                double fineAmount = FineCalculator.calculateProjectedFine(loan.getDueDate());
                if (fineAmount > 0) {
                    Fine fine = new Fine(loan.getId(), fineAmount, today);
                    if (fine.save()) {
                        finesCreated++;
                    }
                }
            }
        }
        
        JOptionPane.showMessageDialog(this, "Created " + finesCreated + " new fines for overdue loans");
        loadFinesData();
    }
    
    private void paySelectedFine() {
        int selectedRow = finesTable.getSelectedRow();
        if (selectedRow >= 0) {
            int fineId = (int) finesTable.getValueAt(selectedRow, 0);
            
            Fine fine = new Fine();
            fine.setId(fineId);
            
            if (fine.markAsPaid()) {
                JOptionPane.showMessageDialog(this, "Fine marked as paid!");
                loadFinesData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to mark fine as paid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a fine to mark as paid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                setText((Boolean)value ? "Available" : "Unavailable");
                setBackground((Boolean)value ? new Color(34, 139, 34) : new Color(220, 20, 60)); // Forest Green and Crimson Red
                setForeground(Color.WHITE);
            }
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;
        private Boolean currentValue;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentValue = (Boolean)value;
            button.setText(currentValue ? "Available" : "Unavailable");
            button.setBackground(currentValue ? new Color(34, 139, 34) : new Color(220, 20, 60));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int bookId = (int) booksTable.getValueAt(booksTable.getEditingRow(), 0);
                Book book = new Book();
                book.setId(bookId);
                book.setAvailable(!currentValue);
                
                if (book.updateAvailability()) {
                    return !currentValue;
                }
            }
            return currentValue;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}