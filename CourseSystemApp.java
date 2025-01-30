import java.util.*;

// Class to represent a Course
class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private String schedule;
    private int enrolledStudents;

    // Constructor to initialize course details
    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolledStudents = 0;
    }

    // Getters for course details
    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getAvailableSlots() {
        return capacity - enrolledStudents;
    }

    public boolean isFull() {
        return enrolledStudents >= capacity;
    }

    // Enroll a student in the course
    public boolean enrollStudent() {
        if (!isFull()) {
            enrolledStudents++;
            return true;
        }
        return false;
    }

    // Remove a student from the course
    public boolean dropStudent() {
        if (enrolledStudents > 0) {
            enrolledStudents--;
            return true;
        }
        return false;
    }
}

// Class to represent a Student
class Student {
    private String studentId;
    private String name;
    private List<Course> registeredCourses;

    // Constructor to initialize student details
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    // Getters for student details
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    // Register for a course
    public boolean registerCourse(Course course) {
        if (course.enrollStudent()) {
            registeredCourses.add(course);
            return true;
        }
        return false;
    }

    // Drop a course
    public boolean dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            course.dropStudent();
            registeredCourses.remove(course);
            return true;
        }
        return false;
    }
}

// Class to manage the Course Management System
class CourseManagementSystem {
    private List<Course> courses;
    private List<Student> students;
    private Scanner scanner;

    // Constructor to initialize the system
    public CourseManagementSystem() {
        this.courses = new ArrayList<>();
        this.students = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Add a course to the system
    public void addCourse(Course course) {
        courses.add(course);
    }

    // Add a student to the system
    public void addStudent(Student student) {
        students.add(student);
    }

    // Display available courses with details
    public void displayCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course course : courses) {
            System.out.println("Course Code: " + course.getCourseCode());
            System.out.println("Title: " + course.getTitle());
            System.out.println("Description: " + course.getDescription());
            System.out.println("Schedule: " + course.getSchedule());
            System.out.println("Available Slots: " + course.getAvailableSlots() + "\n");
        }
    }

    // Register a student for a course
    public void registerStudentForCourse() {
        System.out.print("\nEnter Student ID: ");
        String studentId = scanner.nextLine();
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        displayCourses();
        System.out.print("Enter Course Code to Register: ");
        String courseCode = scanner.nextLine();
        Course course = findCourseByCode(courseCode);

        if (course == null) {
            System.out.println("Course not found.");
        } else if (student.registerCourse(course)) {
            System.out.println("Successfully registered for the course: " + course.getTitle());
        } else {
            System.out.println("Failed to register. The course might be full.");
        }
    }

    // Remove a student from a course
    public void removeStudentFromCourse() {
        System.out.print("\nEnter Student ID: ");
        String studentId = scanner.nextLine();
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("\nRegistered Courses:");
        List<Course> registeredCourses = student.getRegisteredCourses();
        if (registeredCourses.isEmpty()) {
            System.out.println("No registered courses to remove.");
            return;
        }

        for (int i = 0; i < registeredCourses.size(); i++) {
            Course course = registeredCourses.get(i);
            System.out.println((i + 1) + ". " + course.getTitle() + " (" + course.getCourseCode() + ")");
        }

        System.out.print("Enter the number of the course to drop: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (choice < 1 || choice > registeredCourses.size()) {
            System.out.println("Invalid choice.");
        } else {
            Course course = registeredCourses.get(choice - 1);
            if (student.dropCourse(course)) {
                System.out.println("Successfully dropped the course: " + course.getTitle());
            } else {
                System.out.println("Failed to drop the course.");
            }
        }
    }

    // Find a course by its code
    private Course findCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }

    // Find a student by their ID
    private Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return student;
            }
        }
        return null;
    }
}

// Main class
public class CourseSystemApp {
    public static void main(String[] args) {
        CourseManagementSystem system = new CourseManagementSystem();

        // Add some sample courses
        system.addCourse(new Course("CS101", "Intro to Computer Science", "Learn the basics of CS.", 30, "MWF 9:00-10:00"));
        system.addCourse(new Course("MATH101", "Calculus I", "Differential and integral calculus.", 25, "TTh 11:00-12:30"));
        system.addCourse(new Course("PHY101", "Physics I", "Introduction to mechanics.", 20, "MWF 10:00-11:00"));

        // Add some sample students
        system.addStudent(new Student("S001", "Alice"));
        system.addStudent(new Student("S002", "Bob"));

        // Menu loop
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nCourse Management System");
            System.out.println("1. Display Courses");
            System.out.println("2. Register for a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    system.displayCourses();
                    break;
                case 2:
                    system.registerStudentForCourse();
                    break;
                case 3:
                    system.removeStudentFromCourse();
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}
