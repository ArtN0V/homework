package homework.second;

import homework.second.model.User;
import homework.second.service.UserService;
import homework.second.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        UserService service = new UserService();
        Scanner sc = new Scanner(System.in);

        logger.info("User-service started.");
        boolean running = true;

        while (running) {
            System.out.println("\n--- User Service ---");
            System.out.println("1) Create user");
            System.out.println("2) List users");
            System.out.println("3) Get user by id");
            System.out.println("4) Update user");
            System.out.println("5) Delete user");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Age (number or empty): ");
                        String ageStr = sc.nextLine();
                        Integer age = ageStr.isBlank() ? null : Integer.parseInt(ageStr);
                        User created = service.createUser(name, email, age);
                        System.out.println("Created: id=" + created.getId());
                    }
                    case "2" -> {
                        List<User> users = service.listUsers();
                        System.out.println("Users:");
                        for (User u : users) {
                            System.out.printf("%d | %s | %s | %s | created=%s%n",
                                    u.getId(), u.getName(), u.getEmail(),
                                    u.getAge() == null ? "-" : u.getAge().toString(),
                                    u.getCreatedAt());
                        }
                    }
                    case "3" -> {
                        System.out.print("Id: ");
                        Long id = Long.parseLong(sc.nextLine());
                        User u = service.getUser(id);
                        if (u == null) System.out.println("User not found");
                        else System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
                    }
                    case "4" -> {
                        System.out.print("Id: ");
                        Long id = Long.parseLong(sc.nextLine());
                        System.out.print("New name (leave empty to keep): ");
                        String name = sc.nextLine();
                        System.out.print("New email (leave empty to keep): ");
                        String email = sc.nextLine();
                        System.out.print("New age (leave empty to keep): ");
                        String ageStr2 = sc.nextLine();
                        Integer age = ageStr2.isBlank() ? null : Integer.parseInt(ageStr2);
                        User updated = service.updateUser(id,
                                name.isBlank() ? null : name,
                                email.isBlank() ? null : email,
                                age);
                        if (updated == null) System.out.println("User not found");
                        else System.out.println("Updated.");
                    }
                    case "5" -> {
                        System.out.print("Id: ");
                        Long id = Long.parseLong(sc.nextLine());
                        boolean deleted = service.deleteUser(id);
                        System.out.println(deleted ? "Deleted." : "Not found.");
                    }
                    case "0" -> {
                        running = false;
                    }
                    default -> System.out.println("Unknown command");
                }
            } catch (Exception e) {
                logger.error("Ошибка выполнения операции: ", e);
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }

        sc.close();
        HibernateUtil.shutdown();
        logger.info("User-service stopped.");
    }
}
