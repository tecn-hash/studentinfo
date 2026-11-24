import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StudentInfoApp extends JFrame {

    // 1. Functional UI Components
    private final JTextField nameField;
    private final JTextField admissionField;
    private final JComboBox<String> courseComboBox;
    private final JRadioButton maleRadio;
    private final JRadioButton femaleRadio;
    private final JRadioButton otherRadio;
    private final ButtonGroup genderGroup;
    private final JButton registerButton;
    private final JButton clearButton;
    private final JButton exitButton;
    private final JTextArea feedbackArea;

    public StudentInfoApp() {
        super("Event-Driven Student Information Interface");

        // Intercept window close so WindowEvent handles exit confirmation
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(620, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Initialize components
        nameField = new JTextField(20);
        admissionField = new JTextField(20);

        String[] courses = {
            "Bachelor of Science in Software Engineering",
            "Bachelor of Science in Computer Science",
            "Bachelor of Science in Information Technology",
            "Diploma in Computer Studies"
        };
        courseComboBox = new JComboBox<>(courses);

        maleRadio = new JRadioButton("Male", true);
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");

        feedbackArea = new JTextArea(10, 45);
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBackground(Color.WHITE);

        // Build panels and wire events
        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildFeedbackPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        attachEventListeners();
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Admission Number:"));
        formPanel.add(admissionField);

        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseComboBox);

        formPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        formPanel.add(genderPanel);

        return formPanel;
    }

    private JPanel buildFeedbackPanel() {
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Registration Feedback & Event Log"));
        feedbackPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        return feedbackPanel;
    }

    private JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }

    private void attachEventListeners() {
        // 1. ActionEvent for Register, Clear, and Exit buttons
        ActionListener buttonActionHandler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == registerButton) {
                    handleRegister();
                } else if (source == clearButton) {
                    handleClear();
                } else if (source == exitButton) {
                    confirmAndExit();
                }
            }
        };
        registerButton.addActionListener(buttonActionHandler);
        clearButton.addActionListener(buttonActionHandler);
        exitButton.addActionListener(buttonActionHandler);

        // 2. ItemEvent for course selection changes
        courseComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    feedbackArea.append("[ItemEvent] Course selected: " + e.getItem() + "\n");
                }
            }
        });

        // 3. KeyEvent for text-field input interaction (admission number + Enter to submit)
        admissionField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Allow letters, digits, hyphens, slashes, and control keys (like Backspace)
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && c != '-' && c != '/' && !Character.isISOControl(c)) {
                    e.consume();
                    feedbackArea.append("[KeyEvent] Blocked invalid character '" + c + "' in Admission Number.\n");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    feedbackArea.append("[KeyEvent] Enter pressed in Admission Number field.\n");
                    handleRegister();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Optional hook for post-key release handling
            }
        });

        // 4. WindowEvent for exit confirmation when closing the JFrame
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                feedbackArea.append("[WindowEvent] Application window opened.\n");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // Cleanup after dispose if needed
            }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
        });

        // 5. MouseEvent for mouse-based interaction on the feedback area
        feedbackArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                feedbackArea.append("[MouseEvent] Clicked feedback area at (" + e.getX() + ", " + e.getY() + ").\n");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                feedbackArea.setBackground(new Color(240, 248, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                feedbackArea.setBackground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }
        });
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String admission = admissionField.getText().trim();
        String course = (String) courseComboBox.getSelectedItem();
        String gender = getSelectedGender();

        if (name.isEmpty() || admission.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both Student Name and Admission Number.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            feedbackArea.append("[ActionEvent] Registration failed: missing required fields.\n");
            return;
        }

        String record = String.format(
            "[REGISTERED] Name: %s | Adm No: %s | Course: %s | Gender: %s%n",
            name, admission, course, gender
        );
        feedbackArea.append(record);
        JOptionPane.showMessageDialog(
            this,
            "Student registered successfully!",
            "Registration Confirmed",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void handleClear() {
        nameField.setText("");
        admissionField.setText("");
        courseComboBox.setSelectedIndex(0);
        maleRadio.setSelected(true);
        nameField.requestFocusInWindow();
        feedbackArea.append("[ActionEvent] Form fields cleared.\n");
    }

    private String getSelectedGender() {
        if (femaleRadio.isSelected()) {
            return "Female";
        } else if (otherRadio.isSelected()) {
            return "Other";
        }
        return "Male";
    }

    private void confirmAndExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        } else {
            feedbackArea.append("[Exit Cancelled] User chose to remain in the application.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentInfoApp().setVisible(true);
            }
        });
    }
}