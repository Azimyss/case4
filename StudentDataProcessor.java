import java.io.*;
import java.util.*;

public class StudentDataProcessor {
    private List<Student> students;
    private final String fileName;

    public StudentDataProcessor(String fileName) {
        this.fileName = fileName;
        this.students = new ArrayList<>();
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            readFromFile();
        } catch (IOException e) {
            System.out.println("Ошибка при инициализации файла: " + e.getMessage());
        }
    }

    public void readFromFile() throws IOException {
        students.clear();
        File file = new File(fileName);
        
        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        Student student = new Student(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            Double.parseDouble(parts[3].trim())
                        );
                        students.add(student);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка при чтении данных студента: " + line);
                    }
                }
            }
        }
    }

    public void saveToFile() throws IOException {
        Map<String, Student> uniqueStudents = new HashMap<>();
        
        // Читаем существующих студентов
        File file = new File(fileName);
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        try {
                            Student student = new Student(
                                parts[0].trim(),
                                parts[1].trim(),
                                Integer.parseInt(parts[2].trim()),
                                Double.parseDouble(parts[3].trim())
                            );
                            uniqueStudents.put(student.getId(), student);
                        } catch (NumberFormatException e) {
                            // Пропускаем некорректные строки
                        }
                    }
                }
            }
        }

        // Добавляем новых студентов
        for (Student student : students) {
            uniqueStudents.put(student.getId(), student);
        }

        // Создаем отсортированный список всех студентов
        List<Student> sortedStudents = new ArrayList<>(uniqueStudents.values());
        Collections.sort(sortedStudents);

        // Сохраняем всех студентов в файл в отсортированном порядке
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student student : sortedStudents) {
                writer.write(student.toCsvString());
                writer.newLine();
            }
        }
        
        // Обновляем список в памяти, сохраняя сортировку
        students.clear();
        students.addAll(sortedStudents);
    }

    public void addStudent(Student student) {
        boolean exists = students.stream()
                               .anyMatch(s -> s.getId().equals(student.getId()));
        
        if (!exists) {
            students.add(student);
            try {
                saveToFile();
                System.out.println("Студент успешно добавлен!");
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении данных: " + e.getMessage());
            }
        } else {
            System.out.println("Студент с ID " + student.getId() + " уже существует!");
        }
    }

    public void sortByName() {
        try {
            readFromFile();
            Collections.sort(students);
            saveToFile();
        } catch (IOException e) {
            System.out.println("Ошибка при сортировке: " + e.getMessage());
        }
    }

    public List<Student> getStudents() {
        try {
            readFromFile();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении данных: " + e.getMessage());
        }
        return new ArrayList<>(students);
    }

    public List<Student> filterByGpa(double minGpa) {
        return students.stream()
                      .filter(s -> s.getGpa() >= minGpa)
                      .toList();
    }

    public double calculateAverageGpa() {
        if (students.isEmpty()) return 0.0;
        return students.stream()
                      .mapToDouble(Student::getGpa)
                      .average()
                      .orElse(0.0);
    }
} 