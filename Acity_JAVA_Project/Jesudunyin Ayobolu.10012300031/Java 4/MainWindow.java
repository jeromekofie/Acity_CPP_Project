import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color TRANSLUCENT_WHITE = new Color(220, 220, 255);
    private static final Color BLUE_BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color SELECTED_TAB_COLOR = new Color(100, 150, 200);

    public MainWindow() {
        setTitle("Employee Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Customize tab appearance
        tabbedPane.setBackground(BLUE_BUTTON_COLOR);
        tabbedPane.setForeground(Color.WHITE);
        
        // Create custom tab components for better styling
        JPanel empManagementTab = createTabPanel("Employee Management");
        JPanel payrollTab = createTabPanel("Payroll Generation");
        
        JPanel empManagementPanel = new JPanel(new BorderLayout());
        empManagementPanel.setOpaque(false);
        empManagementPanel.add(new EmployeeManagementPanel(), BorderLayout.CENTER);
        
        JPanel payrollPanel = new JPanel(new BorderLayout());
        payrollPanel.setOpaque(false);
        payrollPanel.add(new PayrollPanel(), BorderLayout.CENTER);
        
        // Add tabs with custom components
        tabbedPane.addTab(null, empManagementPanel);
        tabbedPane.setTabComponentAt(0, empManagementTab);
        
        tabbedPane.addTab(null, payrollPanel);
        tabbedPane.setTabComponentAt(1, payrollTab);

        // Add tab selection listener
        tabbedPane.addChangeListener(e -> {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component tab = tabbedPane.getTabComponentAt(i);
                if (tab instanceof JPanel) {
                    ((JPanel) tab).setBackground(
                        tabbedPane.getSelectedIndex() == i ? SELECTED_TAB_COLOR : BLUE_BUTTON_COLOR
                    );
                }
            }
        });

        JPanel tabWrapper = new JPanel(new BorderLayout());
        tabWrapper.setOpaque(false);
        tabWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel tabContent = new JPanel(new BorderLayout());
        tabContent.setBackground(TRANSLUCENT_WHITE);
        tabContent.add(tabbedPane, BorderLayout.CENTER);
        
        tabWrapper.add(tabContent, BorderLayout.CENTER);
        mainPanel.add(tabWrapper, BorderLayout.CENTER);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Arial", Font.BOLD, 12));
        fileMenu.setForeground(TEXT_COLOR);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(new Font("Arial", Font.PLAIN, 12));
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createTabPanel(String title) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        JLabel tabLabel = new JLabel(title);
        tabLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tabLabel.setForeground(Color.WHITE);
        tabLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        tabPanel.add(tabLabel, BorderLayout.CENTER);
        tabPanel.setBackground(BLUE_BUTTON_COLOR);
        
        // Add mouse listener for hover effects
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int index = tabbedPane.indexOfTabComponent(tabPanel);
                if (index != -1 && index != tabbedPane.getSelectedIndex()) {
                    tabPanel.setBackground(SELECTED_TAB_COLOR);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                int index = tabbedPane.indexOfTabComponent(tabPanel);
                if (index != -1 && index != tabbedPane.getSelectedIndex()) {
                    tabPanel.setBackground(BLUE_BUTTON_COLOR);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tabbedPane.indexOfTabComponent(tabPanel);
                if (index != -1) {
                    tabbedPane.setSelectedIndex(index);
                }
            }
        };
        
        tabLabel.addMouseListener(mouseAdapter);
        tabPanel.addMouseListener(mouseAdapter);
        
        return tabPanel;
    }
}