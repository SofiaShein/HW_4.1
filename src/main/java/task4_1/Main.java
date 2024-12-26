package task4_1;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in); Connection connection = ConnectionFactory.getConnection()) {
            DatabaseManager dbManager = new DatabaseManager(connection);

            dbManager.initDB();

            while (true) {
                System.out.println("Enter command: ");
                System.out.print("1 - add client, 2 - add product, 3 - add order, 4 - view clients, 5 - view products, 6 - view orders: ");
                String command = sc.nextLine();

                switch (command) {
                    case "1":
                        System.out.print("Enter client name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter client last name: ");
                        String lastName = sc.nextLine();
                        dbManager.addClient(name, lastName);
                        break;
                    case "2":
                        System.out.print("Enter product name: ");
                        String productName = sc.nextLine();
                        System.out.print("Enter product category: ");
                        String productCategory = sc.nextLine();
                        dbManager.addProduct(productName, productCategory);
                        break;
                    case "3":
                        System.out.print("Enter client ID: ");
                        int clientId = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter product ID: ");
                        int productId = Integer.parseInt(sc.nextLine());
                        dbManager.addOrder(clientId, productId);
                        break;
                    case "4":
                        dbManager.viewTable("Clients");
                        break;
                    case "5":
                        dbManager.viewTable("Products");
                        break;
                    case "6":
                        dbManager.viewTable("Orders");
                        break;
                    default:
                        System.out.println("Invalid command");
                        return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
