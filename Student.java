/**
 * Student.java - Student object class
 * Begun 08/06/17
 * @author Andrew Eissen
 */

//package stuDatabase;

final class Student {

    // Declarations
    private String name, major;
    private double totalPoints;
    private int creditHours;

    /**
     * Fully parameterized constructor
     * @param name          Student's name
     * @param major         Student's major
     */
    public Student(String name, String major) {
        this.setName(name);
        this.setMajor(major);
        this.totalPoints = 0.0;
        this.creditHours = 0;
    }

    /**
     * Setter for String <code>name</code>
     * @param name          Student's name
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for String <code>major</code>
     * @param major         Student's major
     */
    private void setMajor(String major) {
        this.major = major;
    }

    /**
     * Getter for String <code>name</code>
     * @return name         Returns <code>this.name</code>
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for String <code>major</code>
     * @return major        Returns <code>this.major</code>
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * Required function for setting of calculation fields for use by <code>calculateGPA()</code>
     * @param courseGrade       Student's grade in the course
     * @param credits           Number of credit hours
     */
    public void courseCompleted(double courseGrade, int credits) {
        this.totalPoints += (courseGrade * credits);
        this.creditHours += credits;
    }

    /**
     * Main calculation method
     * @return double           Returns either a 4.0 or a more precise double value for GPA
     */
    private double calculateGPA() {
        if (this.creditHours == 0) {
            return 4.0;
        } else {
            return Math.round((this.totalPoints / this.creditHours) * 100.0) / 100.0;
        }
    }

    /**
     * Overridden <code>toString()</code> method
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + this.getName() + "\nMajor: " + this.getMajor() + "\nGPA: "
            + this.calculateGPA();
    }
}