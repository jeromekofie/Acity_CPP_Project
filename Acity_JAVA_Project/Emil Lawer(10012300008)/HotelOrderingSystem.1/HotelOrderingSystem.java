import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.sql.Date;

public class HotelOrderingSystem extends JFrame {
    private JTabbedPane tabbedPane;
    
    // Hotel components
    private JTable hotelTable;
    private DefaultTableModel hotelTableModel;
    private JButton btnRefreshHotels, btnAddHotel, btnEditHotel, btnDeleteHotel;
    
    // Menu components
    private JComboBox<Integer> hotelComboBox;
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private JButton btnRefreshMenu, btnAddMenuItem, btnEditMenuItem, btnDeleteMenuItem;
    
    // Order components
    private JComboBox<Integer> orderHotelComboBox;
    private JTextField txtCustomerName;
    private JTable orderItemsTable;
    private DefaultTableModel orderItemsModel;
    private JButton btnAddToOrder, btnRemoveFromOrder, btnPlaceOrder;
    private JLabel lblOrderTotal;
    
    // Reservation components
    private JComboBox<Integer> reservationHotelComboBox;
    private JTextField txtReservationName;
    private JComboBox<String> roomTypeComboBox;
    private JButton btnCheckAvailability, btnMakeReservation;
    private JSpinner checkInSpinner, checkOutSpinner;
    private JLabel lblAvailabilityResult;
    
    // DAOs
    private HotelDAO hotelDAO;
    private MenuItemDAO menuItemDAO;
    private OrderDAO orderDAO;
    private ReservationDAO reservationDAO;
    
    public HotelOrderingSystem() {
        super("Hotel Menu Ordering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        hotelDAO = new HotelDAO();
        menuItemDAO = new MenuItemDAO();
        orderDAO = new OrderDAO();
        reservationDAO = new ReservationDAO();
        
        initComponents();
        loadHotels();
        loadHotelComboBoxes();
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        // Hotel Management Tab
        tabbedPane.addTab("Hotels", createHotelPanel());
        
        // Menu Management Tab
        tabbedPane.addTab("Menu", createMenuPanel());
        
        // Order Tab
        tabbedPane.addTab("Orders", createOrderPanel());
        
        // Reservation Tab
        tabbedPane.addTab("Reservations", createReservationPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createHotelPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table
        hotelTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Address", "Phone", "Rating"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        hotelTable = new JTable(hotelTableModel);
        JScrollPane scrollPane = new JScrollPane(hotelTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnRefreshHotels = new JButton("Refresh");
        btnRefreshHotels.addActionListener(e -> loadHotels());
        
        btnAddHotel = new JButton("Add Hotel");
        btnAddHotel.addActionListener(e -> showAddHotelDialog());
        
        btnEditHotel = new JButton("Edit Hotel");
        btnEditHotel.addActionListener(e -> showEditHotelDialog());
        
        btnDeleteHotel = new JButton("Delete Hotel");
        btnDeleteHotel.addActionListener(e -> deleteHotel());
        
        buttonPanel.add(btnRefreshHotels);
        buttonPanel.add(btnAddHotel);
        buttonPanel.add(btnEditHotel);
        buttonPanel.add(btnDeleteHotel);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Hotel selection
        JPanel hotelSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hotelSelectPanel.add(new JLabel("Select Hotel:"));
        hotelComboBox = new JComboBox<>();
        hotelComboBox.addActionListener(e -> loadMenuForSelectedHotel());
        hotelSelectPanel.add(hotelComboBox);
        
        panel.add(hotelSelectPanel, BorderLayout.NORTH);
        
        // Table
        menuTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price", "Category"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuTableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnRefreshMenu = new JButton("Refresh");
        btnRefreshMenu.addActionListener(e -> loadMenuForSelectedHotel());
        
        btnAddMenuItem = new JButton("Add Item");
        btnAddMenuItem.addActionListener(e -> showAddMenuItemDialog());
        
        btnEditMenuItem = new JButton("Edit Item");
        btnEditMenuItem.addActionListener(e -> showEditMenuItemDialog());
        
        btnDeleteMenuItem = new JButton("Delete Item");
        btnDeleteMenuItem.addActionListener(e -> deleteMenuItem());
        
        buttonPanel.add(btnRefreshMenu);
        buttonPanel.add(btnAddMenuItem);
        buttonPanel.add(btnEditMenuItem);
        buttonPanel.add(btnDeleteMenuItem);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Hotel selection and customer name
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.add(new JLabel("Select Hotel:"));
        orderHotelComboBox = new JComboBox<>();
        orderHotelComboBox.addActionListener(e -> loadMenuForOrder());
        topPanel.add(orderHotelComboBox);
        
        topPanel.add(new JLabel("Customer Name:"));
        txtCustomerName = new JTextField();
        topPanel.add(txtCustomerName);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Menu items and order items
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Menu items table
        menuTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Qty"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        menuTable = new JTable(menuTableModel);
        JScrollPane menuScrollPane = new JScrollPane(menuTable);
        centerPanel.add(menuScrollPane);
        
        // Order items table
        orderItemsModel = new DefaultTableModel(new Object[]{"Item", "Qty", "Price", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(orderItemsModel);
        JScrollPane orderScrollPane = new JScrollPane(orderItemsTable);
        centerPanel.add(orderScrollPane);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Buttons and total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAddToOrder = new JButton("Add to Order");
        btnAddToOrder.addActionListener(e -> addItemsToOrder());
        
        btnRemoveFromOrder = new JButton("Remove from Order");
        btnRemoveFromOrder.addActionListener(e -> removeFromOrder());
        
        btnPlaceOrder = new JButton("Place Order");
        btnPlaceOrder.addActionListener(e -> placeOrder());
        
        buttonPanel.add(btnAddToOrder);
        buttonPanel.add(btnRemoveFromOrder);
        buttonPanel.add(btnPlaceOrder);
        
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Total label
        lblOrderTotal = new JLabel("Total: $0.00", JLabel.RIGHT);
        bottomPanel.add(lblOrderTotal, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Hotel selection
        JPanel hotelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hotelPanel.add(new JLabel("Select Hotel:"));
        reservationHotelComboBox = new JComboBox<>();
        hotelPanel.add(reservationHotelComboBox);
        
        panel.add(hotelPanel, BorderLayout.NORTH);
        
        // Reservation details
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        detailsPanel.add(new JLabel("Customer Name:"));
        txtReservationName = new JTextField();
        detailsPanel.add(txtReservationName);
        
        detailsPanel.add(new JLabel("Room Type:"));
        roomTypeComboBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        detailsPanel.add(roomTypeComboBox);
        
        detailsPanel.add(new JLabel("Check-in Date:"));
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);
        detailsPanel.add(checkInSpinner);
        
        detailsPanel.add(new JLabel("Check-out Date:"));
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);
        detailsPanel.add(checkOutSpinner);
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        // Buttons and result
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnCheckAvailability = new JButton("Check Availability");
        btnCheckAvailability.addActionListener(e -> checkAvailability());
        
        btnMakeReservation = new JButton("Make Reservation");
        btnMakeReservation.addActionListener(e -> makeReservation());
        
        buttonPanel.add(btnCheckAvailability);
        buttonPanel.add(btnMakeReservation);
        
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        lblAvailabilityResult = new JLabel(" ", JLabel.CENTER);
        lblAvailabilityResult.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(lblAvailabilityResult, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Data loading methods
    private void loadHotels() {
        try {
            List<Hotel> hotels = hotelDAO.getAllHotels();
            hotelTableModel.setRowCount(0);
            
            for (Hotel hotel : hotels) {
                hotelTableModel.addRow(new Object[]{
                    hotel.getId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getPhone(),
                    hotel.getRating()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading hotels: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadHotelComboBoxes() {
        try {
            List<Hotel> hotels = hotelDAO.getAllHotels();
            hotelComboBox.removeAllItems();
            orderHotelComboBox.removeAllItems();
            reservationHotelComboBox.removeAllItems();
            
            for (Hotel hotel : hotels) {
                hotelComboBox.addItem(hotel.getId());
                orderHotelComboBox.addItem(hotel.getId());
                reservationHotelComboBox.addItem(hotel.getId());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading hotels: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMenuForSelectedHotel() {
        if (hotelComboBox.getSelectedItem() == null) return;
        
        int hotelId = (Integer) hotelComboBox.getSelectedItem();
        loadMenuItems(hotelId);
    }
    
    private void loadMenuForOrder() {
        if (orderHotelComboBox.getSelectedItem() == null) return;
        
        int hotelId = (Integer) orderHotelComboBox.getSelectedItem();
        loadMenuItems(hotelId);
    }
    
    private void loadMenuItems(int hotelId) {
        try {
            List<MenuItem> menuItems = menuItemDAO.getMenuByHotel(hotelId);
            menuTableModel.setRowCount(0);
            
            for (MenuItem item : menuItems) {
                menuTableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getCategory()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading menu: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Hotel management methods
    private void showAddHotelDialog() {
        JTextField txtName = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtRating = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Address:"));
        panel.add(txtAddress);
        panel.add(new JLabel("Phone:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Rating (0-5):"));
        panel.add(txtRating);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Hotel", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Hotel hotel = new Hotel(
                    0,
                    txtName.getText(),
                    txtAddress.getText(),
                    txtPhone.getText(),
                    Double.parseDouble(txtRating.getText())
                );
                
                if (hotelDAO.addHotel(hotel)) {
                    loadHotels();
                    loadHotelComboBoxes();
                    JOptionPane.showMessageDialog(this, "Hotel added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add hotel", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid rating", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding hotel: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showEditHotelDialog() {
        int selectedRow = hotelTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hotel to edit", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int hotelId = (Integer) hotelTableModel.getValueAt(selectedRow, 0);
        
        try {
            Hotel hotel = hotelDAO.getHotelById(hotelId);
            if (hotel == null) {
                JOptionPane.showMessageDialog(this, "Hotel not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JTextField txtName = new JTextField(hotel.getName());
            JTextField txtAddress = new JTextField(hotel.getAddress());
            JTextField txtPhone = new JTextField(hotel.getPhone());
            JTextField txtRating = new JTextField(String.valueOf(hotel.getRating()));
            
            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Name:"));
            panel.add(txtName);
            panel.add(new JLabel("Address:"));
            panel.add(txtAddress);
            panel.add(new JLabel("Phone:"));
            panel.add(txtPhone);
            panel.add(new JLabel("Rating (0-5):"));
            panel.add(txtRating);
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Hotel", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                hotel.setName(txtName.getText());
                hotel.setAddress(txtAddress.getText());
                hotel.setPhone(txtPhone.getText());
                hotel.setRating(Double.parseDouble(txtRating.getText()));
                
                if (hotelDAO.updateHotel(hotel)) {
                    loadHotels();
                    JOptionPane.showMessageDialog(this, "Hotel updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update hotel", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid rating", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error editing hotel: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteHotel() {
        int selectedRow = hotelTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hotel to delete", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int hotelId = (Integer) hotelTableModel.getValueAt(selectedRow, 0);
        String hotelName = (String) hotelTableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete hotel: " + hotelName + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (hotelDAO.deleteHotel(hotelId)) {
                    loadHotels();
                    loadHotelComboBoxes();
                    JOptionPane.showMessageDialog(this, "Hotel deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete hotel", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting hotel: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Menu item management methods
    private void showAddMenuItemDialog() {
        if (hotelComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel first", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int hotelId = (Integer) hotelComboBox.getSelectedItem();
        
        JTextField txtName = new JTextField();
        JTextField txtDescription = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtCategory = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Description:"));
        panel.add(txtDescription);
        panel.add(new JLabel("Price:"));
        panel.add(txtPrice);
        panel.add(new JLabel("Category:"));
        panel.add(txtCategory);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Menu Item", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                MenuItem item = new MenuItem(
                    0,
                    hotelId,
                    txtName.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    txtCategory.getText()
                );
                
                if (menuItemDAO.addMenuItem(item)) {
                    loadMenuForSelectedHotel();
                    JOptionPane.showMessageDialog(this, "Menu item added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add menu item", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding menu item: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showEditMenuItemDialog() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to edit", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int itemId = (Integer) menuTableModel.getValueAt(selectedRow, 0);
        
        try {
            MenuItem item = menuItemDAO.getMenuItemById(itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Menu item not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JTextField txtName = new JTextField(item.getName());
            JTextField txtDescription = new JTextField(item.getDescription());
            JTextField txtPrice = new JTextField(String.valueOf(item.getPrice()));
            JTextField txtCategory = new JTextField(item.getCategory());
            
            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Name:"));
            panel.add(txtName);
            panel.add(new JLabel("Description:"));
            panel.add(txtDescription);
            panel.add(new JLabel("Price:"));
            panel.add(txtPrice);
            panel.add(new JLabel("Category:"));
            panel.add(txtCategory);
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Menu Item", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                item.setName(txtName.getText());
                item.setDescription(txtDescription.getText());
                item.setPrice(Double.parseDouble(txtPrice.getText()));
                item.setCategory(txtCategory.getText());
                
                if (menuItemDAO.updateMenuItem(item)) {
                    loadMenuForSelectedHotel();
                    JOptionPane.showMessageDialog(this, "Menu item updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update menu item", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error editing menu item: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to delete", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int itemId = (Integer) menuTableModel.getValueAt(selectedRow, 0);
        String itemName = (String) menuTableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete menu item: " + itemName + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (menuItemDAO.deleteMenuItem(itemId)) {
                    loadMenuForSelectedHotel();
                    JOptionPane.showMessageDialog(this, "Menu item deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete menu item", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting menu item: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Order management methods
    private void addItemsToOrder() {
        List<OrderItem> itemsToAdd = new ArrayList<>();
        
        for (int i = 0; i < menuTableModel.getRowCount(); i++) {
            try {
                int qty = Integer.parseInt(menuTableModel.getValueAt(i, 3).toString());
                if (qty > 0) {
                    int itemId = (Integer) menuTableModel.getValueAt(i, 0);
                    String name = (String) menuTableModel.getValueAt(i, 1);
                    double price = (Double) menuTableModel.getValueAt(i, 2);
                    
                    itemsToAdd.add(new OrderItem(itemId, name, qty, price));
                }
            } catch (NumberFormatException e) {
                // Ignore rows with invalid quantity
            }
        }
        
        if (itemsToAdd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantities for items to add", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add to order table
        for (OrderItem item : itemsToAdd) {
            orderItemsModel.addRow(new Object[]{
                item.getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal()
            });
        }
        
        // Clear quantities in menu table
        for (int i = 0; i < menuTableModel.getRowCount(); i++) {
            menuTableModel.setValueAt("0", i, 3);
        }
        
        updateOrderTotal();
    }
    
    private void removeFromOrder() {
        int selectedRow = orderItemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        orderItemsModel.removeRow(selectedRow);
        updateOrderTotal();
    }
    
    private void updateOrderTotal() {
        double total = 0;
        for (int i = 0; i < orderItemsModel.getRowCount(); i++) {
            total += (Double) orderItemsModel.getValueAt(i, 3);
        }
        
        DecimalFormat df = new DecimalFormat("$0.00");
        lblOrderTotal.setText("Total: " + df.format(total));
    }
    
    private void placeOrder() {
        if (orderHotelComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String customerName = txtCustomerName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (orderItemsModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add items to the order", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int hotelId = (Integer) orderHotelComboBox.getSelectedItem();
        
        // Collect order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < orderItemsModel.getRowCount(); i++) {
            String name = (String) orderItemsModel.getValueAt(i, 0);
            int qty = (Integer) orderItemsModel.getValueAt(i, 1);
            double price = (Double) orderItemsModel.getValueAt(i, 2);
            
            // We need to get the item ID from the menu table
            int itemId = -1;
            for (int j = 0; j < menuTableModel.getRowCount(); j++) {
                if (name.equals(menuTableModel.getValueAt(j, 1))) {
                    itemId = (Integer) menuTableModel.getValueAt(j, 0);
                    break;
                }
            }
            
            if (itemId != -1) {
                orderItems.add(new OrderItem(itemId, name, qty, price));
            }
        }
        
        try {
            int orderId = orderDAO.createOrder(hotelId, customerName, orderItems);
            
            // Generate bill
            String bill = generateBill(hotelId, customerName, orderId, orderItems);
            
            // Save bill to file
            String fileName = "order_" + orderId + "_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".txt";
            
            java.nio.file.Files.write(
                java.nio.file.Paths.get(fileName),
                bill.getBytes()
            );
            
            // Clear order
            orderItemsModel.setRowCount(0);
            txtCustomerName.setText("");
            lblOrderTotal.setText("Total: $0.00");
            
            // Show success message with bill
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\nOrder ID: " + orderId + 
                "\nBill saved to: " + fileName + "\n\n" + bill, 
                "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error placing order: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateBill(int hotelId, String customerName, int orderId, List<OrderItem> items) throws SQLException {
        Hotel hotel = hotelDAO.getHotelById(hotelId);
        if (hotel == null) {
            throw new SQLException("Hotel not found");
        }
        
        StringBuilder bill = new StringBuilder();
        DecimalFormat df = new DecimalFormat("$0.00");
        
        bill.append("================================\n");
        bill.append("          ORDER BILL\n");
        bill.append("================================\n");
        bill.append("Hotel: ").append(hotel.getName()).append("\n");
        bill.append("Address: ").append(hotel.getAddress()).append("\n");
        bill.append("Phone: ").append(hotel.getPhone()).append("\n");
        bill.append("--------------------------------\n");
        bill.append("Order ID: ").append(orderId).append("\n");
        bill.append("Customer: ").append(customerName).append("\n");
        bill.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())).append("\n");
        bill.append("--------------------------------\n");
        bill.append("ITEMS ORDERED:\n\n");
        
        double subtotal = 0;
        for (OrderItem item : items) {
            bill.append(String.format("%-30s %3d x %6s = %7s\n", 
                item.getName(), 
                item.getQuantity(), 
                df.format(item.getPrice()), 
                df.format(item.getSubtotal())));
            subtotal += item.getSubtotal();
        }
        
        double tax = subtotal * 0.1; // 10% tax
        double total = subtotal + tax;
        
        bill.append("\n");
        bill.append("--------------------------------\n");
        bill.append(String.format("%-30s %12s\n", "Subtotal:", df.format(subtotal)));
        bill.append(String.format("%-30s %12s\n", "Tax (10%):", df.format(tax)));
        bill.append(String.format("%-30s %12s\n", "TOTAL:", df.format(total)));
        bill.append("================================\n");
        bill.append("Thank you for your order!\n");
        bill.append("================================\n");
        
        return bill.toString();
    }
    
    // Reservation management methods
    private void checkAvailability() {
        if (reservationHotelComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String customerName = txtReservationName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        java.util.Date checkInUtilDate = (java.util.Date) checkInSpinner.getValue();
        java.util.Date checkOutUtilDate = (java.util.Date) checkOutSpinner.getValue();
        java.sql.Date checkInDate = new java.sql.Date(checkInUtilDate.getTime());
       java.sql.Date checkOutDate = new java.sql.Date(checkOutUtilDate.getTime());
        
        if (checkOutDate.before(checkInDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // In a real system, we would check actual availability
        // For this example, we'll just assume rooms are available
        lblAvailabilityResult.setText("Rooms available for selected dates!");
        lblAvailabilityResult.setForeground(Color.GREEN);
    }
    
    private void makeReservation() {
        if (reservationHotelComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a hotel", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String customerName = txtReservationName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        java.util.Date checkInUtilDate = (java.util.Date) checkInSpinner.getValue();
        java.util.Date checkOutUtilDate = (java.util.Date) checkOutSpinner.getValue();
        java.sql.Date checkInDate = new java.sql.Date(checkInUtilDate.getTime());
        Date checkOutDate = new Date(checkOutUtilDate.getTime());
        
        if (checkOutDate.before(checkInDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        int hotelId = (Integer) reservationHotelComboBox.getSelectedItem();
        
        try {
            Reservation reservation = new Reservation(
                0,
                hotelId,
                customerName,
                checkInDate,
                checkOutDate,
                roomType,
                "Confirmed"
            );
            
            int reservationId = reservationDAO.createReservation(reservation);
            
            // Generate confirmation
            String confirmation = generateReservationConfirmation(reservationId, reservation);
            
            // Save confirmation to file
            String fileName = "reservation_" + reservationId + "_" + 
                new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".txt";
            
            java.nio.file.Files.write(
                java.nio.file.Paths.get(fileName),
                confirmation.getBytes()
            );
            
            // Clear form
            txtReservationName.setText("");
            checkInSpinner.setValue(new java.util.Date());
            checkOutSpinner.setValue(new java.util.Date());
            roomTypeComboBox.setSelectedIndex(0);
            lblAvailabilityResult.setText(" ");
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Reservation created successfully!\nReservation ID: " + reservationId + 
                "\nConfirmation saved to: " + fileName + "\n\n" + confirmation, 
                "Reservation Confirmation", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating reservation: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateReservationConfirmation(int reservationId, Reservation reservation) throws SQLException {
        Hotel hotel = hotelDAO.getHotelById(reservation.getHotelId());
        if (hotel == null) {
            throw new SQLException("Hotel not found");
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        StringBuilder confirmation = new StringBuilder();
        
        confirmation.append("================================\n");
        confirmation.append("     RESERVATION CONFIRMATION\n");
        confirmation.append("================================\n");
        confirmation.append("Hotel: ").append(hotel.getName()).append("\n");
        confirmation.append("Address: ").append(hotel.getAddress()).append("\n");
        confirmation.append("Phone: ").append(hotel.getPhone()).append("\n");
        confirmation.append("--------------------------------\n");
        confirmation.append("Reservation ID: ").append(reservationId).append("\n");
        confirmation.append("Customer: ").append(reservation.getCustomerName()).append("\n");
        confirmation.append("Room Type: ").append(reservation.getRoomType()).append("\n");
        confirmation.append("Check-in: ").append(dateFormat.format(reservation.getCheckInDate())).append("\n");
        confirmation.append("Check-out: ").append(dateFormat.format(reservation.getCheckOutDate())).append("\n");
        confirmation.append("Status: ").append(reservation.getStatus()).append("\n");
        confirmation.append("--------------------------------\n");
        confirmation.append("Thank you for your reservation!\n");
        confirmation.append("================================\n");
        
        return confirmation.toString();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel if system L&F not available
            }
            
            HotelOrderingSystem system = new HotelOrderingSystem();
            system.setVisible(true);
        });
    }
}