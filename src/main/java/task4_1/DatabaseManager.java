package task4_1;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(Connection connection) {
        this.connection = connection;
    }

    public void initDB() throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            stmt.execute("CREATE TABLE IF NOT EXISTS Products (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) NOT NULL, category VARCHAR(20))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Clients (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) NOT NULL, last_name VARCHAR(20) NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS Orders (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, client_id INT NOT NULL, date DATETIME NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS OrderItems (order_id INT NOT NULL REFERENCES Orders(id), product_id INT NOT NULL REFERENCES Products(id))");
        } finally {
            stmt.close();
        }
    }

    public void addClient(String name, String lastName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Clients (name, last_name) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.executeUpdate();
        }
    }

    public void addProduct(String productName, String productCategory) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Products (name, category) VALUES (?, ?)")) {
            ps.setString(1, productName);
            ps.setString(2, productCategory);
            ps.executeUpdate();
        }
    }

    public void addOrder(int clientId, int productId) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders (client_id, date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clientId);
            ps.setString(2, dtf.format(now));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    try (PreparedStatement ps2 = connection.prepareStatement("INSERT INTO OrderItems (order_id, product_id) VALUES (?, ?)")) {
                        ps2.setInt(1, orderId);
                        ps2.setInt(2, productId);
                        ps2.executeUpdate();
                    }
                }
            }
        }
    }


    public void viewTable(String tableName) throws SQLException {
        String query = "";

        if (tableName.equals("Orders")) {
            query = "SELECT o.id AS order_id, c.id AS client_id, c.name AS client_name, p.id AS product_id, p.name AS product_name, o.date AS order_date " +
                    "FROM Orders o " +
                    "JOIN Clients c ON o.client_id = c.id " +
                    "JOIN OrderItems oi ON o.id = oi.order_id " +
                    "JOIN Products p ON oi.product_id = p.id";
        } else {
            query = "SELECT * FROM " + tableName;
        }

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                if (columnName.equals("order_date")) {
                    System.out.print("Order Date\t\t");
                } else {
                    System.out.print(columnName + "\t\t");
                }
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t\t");
                }
                System.out.println();
            }
        }
    }


}
