import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    private static final String FILE_NAME = "students.csv";
    private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    private static final StudentDataProcessor processor = new StudentDataProcessor(FILE_NAME);

    public static void main(String[] args) {
        while (true) {
            try {
                showMenu();
                int choice = getIntInput("Выберите опцию: ");
                scanner.nextLine(); // очистка буфера

                switch (choice) {
                    case 1:
                        addNewStudent();
                        break;
                    case 2:
                        displayAllStudents();
                        break;
                    case 3:
                        sortAndDisplayStudents();
                        break;
                    case 4:
                        filterAndDisplayByGpa();
                        break;
                    case 5:
                        showAverageGpa();
                        break;
                    case 6:
                        saveAndExit();
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
                scanner.nextLine(); // очистка буфера после ошибки
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nСтуденческая информационная система");
        System.out.println("1. Добавить нового студента");
        System.out.println("2. Показать всех студентов");
        System.out.println("3. Сортировать по имени");
        System.out.println("4. Фильтровать по среднему баллу");
        System.out.println("5. Показать средний балл всех студентов");
        System.out.println("6. Сохранить и выйти");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Пожалуйста, введите корректное число.");
                scanner.nextLine();
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("Пожалуйста, введите корректное число (используйте точку для дробной части).");
                scanner.nextLine();
            }
        }
    }

    private static void addNewStudent() {
        try {
            System.out.print("Введите ID студента: ");
            String id = scanner.nextLine().trim();
            
            System.out.print("Введите имя студента: ");
            String name = scanner.nextLine().trim();
            
            int age = getIntInput("Введите возраст студента: ");
            scanner.nextLine();
            
            double gpa = getDoubleInput("Введите средний балл студента (используйте точку, например 4.5): ");
            scanner.nextLine();

            processor.addStudent(new Student(id, name, age, gpa));
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении студента: " + e.getMessage());
        }
    }

    private static void displayAllStudents() throws IOException {
        List<Student> students = processor.getStudents();
        if (students.isEmpty()) {
            System.out.println("Нет данных о студентах.");
            return;
        }
        System.out.println("\nСписок всех студентов:");
        students.forEach(System.out::println);
    }

    private static void sortAndDisplayStudents() throws IOException {
        List<Student> students = processor.getStudents();
        if (students.isEmpty()) {
            System.out.println("Нет данных о студентах.");
            return;
        }
        
        processor.sortByName();
        students = processor.getStudents();
        System.out.println("\nСтуденты, отсортированные по имени:");
        students.forEach(System.out::println);
    }

    private static void filterAndDisplayByGpa() throws IOException {
        List<Student> students = processor.getStudents();
        if (students.isEmpty()) {
            System.out.println("Нет данных о студентах.");
            return;
        }
        
        double minGpa = getDoubleInput("Введите минимальный средний балл для фильтрации: ");
        scanner.nextLine();
        
        List<Student> filteredStudents = processor.filterByGpa(minGpa);
        if (filteredStudents.isEmpty()) {
            System.out.println("Нет студентов с баллом выше " + minGpa);
            return;
        }
        
        System.out.println("\nСтуденты с средним баллом >= " + minGpa + ":");
        filteredStudents.forEach(System.out::println);
    }

    private static void showAverageGpa() throws IOException {
        List<Student> students = processor.getStudents();
        if (students.isEmpty()) {
            System.out.println("Нет данных о студентах.");
            return;
        }
        
        double averageGpa = processor.calculateAverageGpa();
        System.out.printf("\nСредний балл всех студентов: %.2f%n", averageGpa);
    }

    private static void saveAndExit() throws IOException {
        processor.saveToFile();
        System.out.println("Программа завершена.");
    }
} 
