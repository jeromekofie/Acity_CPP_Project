import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JTextArea inputArea, outputArea;
    private JButton encryptBtn, decryptBtn, saveBtn, loadBtn, dbSaveBtn, dbLoadBtn, themeToggleBtn;
    private EncryptionLogic logic = new EncryptionLogic();
    private FileHandler fileHandler = new FileHandler();
    private MessageDAO messageDAO;
    private boolean darkMode = false;

    // Added: Store text and button panels
    private JPanel textPanel, buttonPanel;

    public MainFrame() {
        setTitle("Message Encryptor");
        setSize(840, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(244, 166, 176)); // Background pink
        setLayout(new BorderLayout(10, 10));

        try {
            messageDAO = new MessageDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }

        // ===== Text Area Panel =====
        textPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Stored as field
        textPanel.setBackground(new Color(244, 166, 176));
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputArea = new JTextArea(4, 10);
        outputArea = new JTextArea(4, 10);
        
        Color sharedTextColor = new Color(255, 220, 230);
        inputArea.setBackground(sharedTextColor);
        outputArea.setBackground(sharedTextColor);

        inputArea.setBorder(BorderFactory.createTitledBorder("Input"));
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));

        textPanel.add(new JScrollPane(inputArea));
        textPanel.add(new JScrollPane(outputArea));
        add(textPanel, BorderLayout.CENTER);

        // ===== Button Panel =====
        buttonPanel = new JPanel(new GridLayout(2, 3, 15, 10)); // Stored as field
        buttonPanel.setBackground(new Color(244, 166, 176));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        encryptBtn = createStyledButton("Encrypt");
        decryptBtn = createStyledButton("Decrypt");
        saveBtn = createStyledButton("Save File");
        loadBtn = createStyledButton("Load File");
        dbSaveBtn = createStyledButton("Save to DB");
        dbLoadBtn = createStyledButton("Load from DB");

        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(dbSaveBtn);
        buttonPanel.add(dbLoadBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Action Listeners =====
        encryptBtn.addActionListener(e -> outputArea.setText(logic.encrypt(inputArea.getText())));
        decryptBtn.addActionListener(e -> outputArea.setText(logic.decrypt(inputArea.getText())));

        saveBtn.addActionListener(e -> {
            try {
                fileHandler.writeToFile("encrypted.txt", outputArea.getText());
                JOptionPane.showMessageDialog(this, "Saved to file.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving.");
            }
        });

        loadBtn.addActionListener(e -> {
            try {
                inputArea.setText(fileHandler.readFromFile("encrypted.txt"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading.");
            }
        });

        dbSaveBtn.addActionListener(e -> {
            try {
                String title = JOptionPane.showInputDialog("Enter title:");
                messageDAO.saveMessage(title, outputArea.getText());
                JOptionPane.showMessageDialog(this, "Saved to DB.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB Save failed.");
            }
        });

        dbLoadBtn.addActionListener(e -> {
            try {
                String title = JOptionPane.showInputDialog("Enter title:");
                String msg = messageDAO.getMessageByTitle(title);
                inputArea.setText(msg);
                JOptionPane.showMessageDialog(this, "Loaded from DB.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB Load failed.");
            }
        });

        // ===== Dark Mode Toggle Button =====
        themeToggleBtn = new JButton("Switch to Dark Mode");
        themeToggleBtn.setBackground(new Color(255, 204, 229));
        themeToggleBtn.setForeground(Color.WHITE);
        themeToggleBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        themeToggleBtn.setPreferredSize(new Dimension(150, 40));
        themeToggleBtn.addActionListener(e -> toggleTheme());

        JPanel togglePanel = new JPanel();
        togglePanel.setOpaque(false);
        togglePanel.add(themeToggleBtn);
        add(togglePanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 228, 236));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(getText(), getWidth() / 2 - getFontMetrics(getFont()).stringWidth(getText()) / 2,
                        getHeight() / 2 + getFontMetrics(getFont()).getAscent() / 2 - 2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(250, 218, 221));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(84, 28));
        return button;
    }

    private void toggleTheme() {
    darkMode = !darkMode;
    if (darkMode) {
        Color darkGray = Color.DARK_GRAY;
        Color lightGrayTextArea = new Color(200, 200, 200); // Lighter gray

        getContentPane().setBackground(darkGray);
        textPanel.setBackground(darkGray);
        buttonPanel.setBackground(darkGray);

        inputArea.setBackground(lightGrayTextArea);
        outputArea.setBackground(lightGrayTextArea);
        inputArea.setForeground(Color.BLACK);
        outputArea.setForeground(Color.BLACK);

        themeToggleBtn.setText("Switch to Light Mode");
    } else {
            getContentPane().setBackground(new Color(244, 166, 176));
            textPanel.setBackground(new Color(244, 166, 176));
            buttonPanel.setBackground(new Color(244, 166, 176));

            inputArea.setBackground(new Color(255, 220, 230));
            outputArea.setBackground(new Color(255, 220, 230));
            inputArea.setForeground(Color.BLACK);
            outputArea.setForeground(Color.BLACK);

            themeToggleBtn.setText("Switch to Dark Mode");
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
