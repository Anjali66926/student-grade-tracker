import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class StudentGradeTracker extends JFrame {
    // Using ArrayList to store and manage data 
    private ArrayList<Double> grades = new ArrayList<>();
    private JTextField gradeField;
    private JTextArea reportArea;

    public StudentGradeTracker() {
        // Setup the GUI Window 
        setTitle("CodeAlpha Student Grade Tracker");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Input Components
        add(new JLabel("Enter Student Grade:"));
        gradeField = new JTextField(10);
        add(gradeField);

        JButton addButton = new JButton("Add Grade");
        add(addButton);

        JButton reportButton = new JButton("Generate Report");
        add(reportButton);

        // Display Area for the Summary Report [cite: 26]
        reportArea = new JTextArea(15, 30);
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea));

        // Logic to Add Grades to ArrayList 
        addButton.addActionListener(e -> {
            try {
                double grade = Double.parseDouble(gradeField.getText());
                if (grade >= 0) {
                    grades.add(grade);
                    reportArea.append("Grade " + grade + " added.\n");
                    gradeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a non-negative grade.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric grade.");
            }
        });

        // Logic to Calculate and Display Summary [cite: 24, 26]
        reportButton.addActionListener(e -> {
            if (grades.isEmpty()) {
                reportArea.setText("No grades entered yet.");
                return;
            }

            double sum = 0;
            for (double g : grades) sum += g;

            // Calculate average, highest, and lowest scores 
            double average = sum / grades.size();
            double highest = Collections.max(grades);
            double lowest = Collections.min(grades);

            // Display a summary report of all students [cite: 26]
            reportArea.setText("--- Summary Report ---\n");
            reportArea.append("Total Students: " + grades.size() + "\n");
            reportArea.append("Average Score: " + String.format("%.2f", average) + "\n");
            reportArea.append("Highest Score: " + highest + "\n");
            reportArea.append("Lowest Score: " + lowest + "\n");
        });
    }

    public static void main(String[] args) {
        // Launch the GUI application 
        SwingUtilities.invokeLater(() -> {
            new StudentGradeTracker().setVisible(true);
        });
    }
}