import java.util.Locale;

public class Student implements Comparable<Student> {
    private String id;
    private String name;
    private int age;
    private double gpa;

    public Student(String id, String name, int age, double gpa) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    @Override
    public String toString() {
        return String.format(Locale.US, "ID: %s, Name: %s, Age: %d, GPA: %.2f", id, name, age, gpa);
    }

    @Override
    public int compareTo(Student other) {
        // Сравниваем имена без учета регистра для корректной сортировки
        return this.name.toLowerCase().compareTo(other.name.toLowerCase());
    }

    public String toCsvString() {
        return String.format(Locale.US, "%s,%s,%d,%.2f", id, name, age, gpa);
    }
} 