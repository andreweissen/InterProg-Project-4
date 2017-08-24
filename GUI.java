/**
 * GUI.java - Constructs the main interface
 * Begun 08/06/17
 * @author Andrew Eissen
 */

//package stuDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public final class GUI extends JFrame {

    // Window-related variables
    private String title;
    private int width;
    private int height;

    // GUI fields
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JLabel idLabel, nameLabel, majorLabel, selectedLabel;
    private static JTextField idField, nameField, majorField;
    private static JComboBox<String> comboBox;
    private static JButton processButton;

    /*
     * Author's notes
     *
     * The author elected to set the HashMap's keys as strings, seeing as the author is not exactly
     * sure how student/employee ids are formed in terms of acceptable characters. In the event that
     * letters are occasionally used in EMPLIDS, it would be a bad idea for the program to reject
     * such input out of hand, so the author left the key as a string.
     *
     * Additionally, regarding the credits array, the author was unsure of how many options should
     * be listed in the dropdown. The rubric showed options of 3 & 6, but the author added a few
     * more options to show off the GPA calculation algorithm, the values of which may be confirmed
     * on the website gpacalculator.net/college-gpa-calculator which served as a useful tool to
     * check if the author's program displayed proper values.
     */

    // Important field definitions/declarations
    private static HashMap<String, Student> studentHashMap = new HashMap<>();
    private static String[] letterGrades = new String[] {"A", "B", "C", "D", "F"};
    private static Integer[] credits = new Integer[] {1, 2, 3, 4, 6};

    /**
     * Fully parameterized constructor
     * @param title     Window title
     * @param width     Window width
     * @param height    Window height
     */
    public GUI(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    /**
     * Default constructor
     */
    public GUI() {
        super("Project 4");
        this.setWindowTitle("Project 4");
        this.setWindowWidth(300);
        this.setWindowHeight(200);
    }

    /**
     * Setter for window title
     * @param title     Window title
     */
    private void setWindowTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for window width
     * @param width     Window width
     */
    private void setWindowWidth(int width) {
        this.width = width;
    }

    /**
     * Setter for window height
     * @param height    Window height
     */
    private void setWindowHeight(int height) {
        this.height = height;
    }

    /**
     * GUI constructor function
     */
    private void constructGUI() {
        //Assorted definitions
        mainPanel = new JPanel(new GridLayout(5, 5, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        majorField = new JTextField();
        comboBox = new JComboBox<>();
        processButton = new JButton("Process Request");

        idLabel = new JLabel("Id:");
        nameLabel = new JLabel("Name:");
        majorLabel = new JLabel("Major:");
        selectedLabel = new JLabel("Choose Selection:");

        // GUI construction
        mainPanel.add(idLabel);
        mainPanel.add(idField);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(majorLabel);
        mainPanel.add(majorField);
        mainPanel.add(selectedLabel);
        mainPanel.add(comboBox);
        mainPanel.add(processButton);

        // ComboBox options
        comboBox.addItem("Insert");
        comboBox.addItem("Delete");
        comboBox.addItem("Find");
        comboBox.addItem("Update");

        processButton.addMouseListener(new GUIMouseAdapter());

        // Assemble Frame
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method used when user selects <code>Insert</code> option
     */
    private void insertEntry() {
        String desiredId = idField.getText();
        String desiredName = nameField.getText();
        String desiredMajor = majorField.getText();

        if (studentHashMap.containsKey(desiredId)) {
            this.displayStatusPanel(
                "Error: A student with this information already exists.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            studentHashMap.put(desiredId, new Student(desiredName, desiredMajor));

            this.displayStatusPanel(
                "Success: " + desiredName + " has been added.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Method used when user selects <code>Delete</code> option
     */
    private void deleteEntry() {
        String desiredId = idField.getText();
        Student student = studentHashMap.get(desiredId);

        try {
            if (student == null) {
                throw new NoMatchFoundException();
            } else if (this.checkInput(student) == false) {
                throw new InputMismatchException();
            } else if (studentHashMap.containsKey(desiredId)) {
                studentHashMap.remove(desiredId);
                this.displayStatusPanel(
                    "Success: " + student.getName() + " removed.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                throw new NoMatchFoundException();
            }
        } catch(NoMatchFoundException ex) {
            this.displayStatusPanel(
                "Error: Entry not found.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        } catch(InputMismatchException e) {
            this.displayStatusPanel(
                "Error: Information does not match data on file.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Method used when user selects <code>Find</code> option
     */
    private void findEntry() {
        String desiredId = idField.getText();
        Student student = studentHashMap.get(desiredId);

        try {
            if (student == null) {
                throw new NoMatchFoundException();
            } else if (this.checkInput(student) == false) {
                throw new InputMismatchException();
            } else if (studentHashMap.containsKey(desiredId)) {
                this.displayStatusPanel(
                    "EMPLID: " + desiredId + "\n" + student.toString(),
                    "Find",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                throw new NoMatchFoundException();
            }
        } catch(NoMatchFoundException ex) {
            this.displayStatusPanel(
                "Error: Entry not found.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        } catch(InputMismatchException e) {
            this.displayStatusPanel(
                "Error: Information does not match data on file.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Method used when user selects <code>Update</code> option
     */
    private void updateEntry() {
        String desiredId = idField.getText();
        Student student = studentHashMap.get(desiredId);

        try {
            if (student == null) {
                throw new NoMatchFoundException();
            } else if (this.checkInput(student) == false) {
                throw new InputMismatchException();
            }
        } catch(NoMatchFoundException ex) {
            this.displayStatusPanel(
                "Error: Entry not found.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        } catch(InputMismatchException e) {
            this.displayStatusPanel(
                "Error: Information does not match data on file.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String grade = this.updateGrade(letterGrades);
        Integer credit = this.updateCredits(credits);

        if (grade == null || credit == null) {
            this.displayStatusPanel(
                "Success: Update cancelled.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            // Inspired by gpacalculator.net/college-gpa-calculator
            Integer numericGrade = Arrays.asList("F", "D", "C", "B", "A").indexOf(grade);

            student.courseCompleted(numericGrade, credit);

            this.displayStatusPanel(
                "Success: Grade has been updated.\nGrade: " + grade + "\nCredits: " + credit,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Compares user input with data on file
     * @param student       The student as selected by the <code>idField.getText()</code> input
     * @return boolean
     */
    private boolean checkInput(Student student) {
        return student.getName().equals(nameField.getText()) &&
                student.getMajor().equals(majorField.getText());
    }

    /**
     * Pseudoconstructor for <code>JOptionPane</code>s that display status of operation
     * @param message       The desired text to display in the panel body
     * @param title         The title of the window
     * @param status        The <code>int</code> indicating which image to display next to the text
     */
    private void displayStatusPanel(String message, String title, int status) {
        JOptionPane.showMessageDialog(mainPanel, message, title, status);
    }

    /**
     * Pseudoconstructor for <code>JOptionPane</code>s that provide user with operation options
     * @param message           The desired text to display in the panel body
     * @param possibleValues    The <code>Object</code> array containing possible choices
     * @return JOptionPane
     */
    private Object displayOptionsPanel(String message, Object[] possibleValues) {
        return JOptionPane.showInputDialog(
            mainPanel,                          // parentComponent
            message,                            // message
            "",                                 // title
            JOptionPane.QUESTION_MESSAGE,       // messageType
            null,                               // icon
            possibleValues,                     // selectionValues
            possibleValues[0]                   // initialSelectionValue
        );
    }

    /**
     * Handles the display of letter grade options
     * @param letterGrade               <code>String</code> array of letter grade values
     * @return (String) JOptionPane
     */
    private String updateGrade(String[] letterGrade) {
        return (String) this.displayOptionsPanel("Choose grade:", letterGrade);
    }

    /**
     * Handles the display of credit hour options
     * @param credits                   <code>Integer</code> array of credit hour values
     * @return (Integer) JOptionPane
     */
    private Integer updateCredits(Integer[] credits) {
        return (Integer) this.displayOptionsPanel("Choose credits:", credits);
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        GUI newGUI = new GUI("Project 4", 300, 200);
        newGUI.constructGUI();
    }

    /**
     * Class for handling button clicks, extends MouseAdapter
     * @see java.awt.event.MouseAdapter
     */
    class GUIMouseAdapter extends MouseAdapter {
        /**
         * Default constructor
         */
        public GUIMouseAdapter() {}

        /**
         * Handles mouse clicks of <code>Process</code> button
         * @param e         <code>MouseEvent</code>
         */
        @Override
        public void mousePressed(MouseEvent e) {
            Object selected = comboBox.getSelectedItem();
            JTextField[] textFieldArray = new JTextField[] {idField, nameField, majorField};

            // Checks that fields aren't blank prior to continuing
            for (JTextField field : textFieldArray) {
                if (field.getText().equals("")) {
                    displayStatusPanel(
                        "Error: Input may not be blank.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            if (selected.equals("Insert")) {
                insertEntry();
            } else if (selected.equals("Delete")) {
                deleteEntry();
            } else if (selected.equals("Find")) {
                findEntry();
            } else if (selected.equals("Update")) {
                updateEntry();
            }
        }
    }
}