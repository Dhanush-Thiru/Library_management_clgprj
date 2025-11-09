import java.sql.*;
import java.util.Scanner;

public class LibraryManager {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_mysql_password";

    private final Scanner scanner;

    public LibraryManager() {
        this.scanner = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please ensure the connector JAR is in your classpath.");
            System.exit(1);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public void addBook() {
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Book ISBN (Unique): ");
        String isbn = scanner.nextLine();

        String sql = "INSERT INTO books (title, author, isbn, is_issued) VALUES (?, ?, ?, 0)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n[SUCCESS] Book added successfully: " + title);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
             System.err.println("\n[ERROR] Failed to add book. An item with ISBN '" + isbn + "' already exists.");
        } catch (SQLException e) {
            System.err.println("\n[ERROR] Database error during addBook: " + e.getMessage());
        }
    }

    public void issueBook() {
        System.out.print("Enter ISBN of the book to issue: ");
        String isbn = scanner.nextLine();

        String checkSql = "SELECT title FROM books WHERE isbn = ? AND is_issued = 0";
        String updateSql = "UPDATE books SET is_issued = 1 WHERE isbn = ? AND is_issued = 0";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            checkStmt.setString(1, isbn);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("\n[INFO] Book not found with ISBN " + isbn + " or it is already issued.");
                return;
            }

            updateStmt.setString(1, isbn);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                String title = rs.getString("title");
                System.out.println("\n[SUCCESS] Book issued successfully: " + title);
            } else {
                System.out.println("\n[ERROR] Failed to issue book. Check ISBN or status.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR] Database error during issueBook: " + e.getMessage());
        }
    }

    public void returnBook() {
        System.out.print("Enter ISBN of the book to return: ");
        String isbn = scanner.nextLine();

        String sql = "UPDATE books SET is_issued = 0 WHERE isbn = ? AND is_issued = 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\n[SUCCESS] Book with ISBN " + isbn + " returned successfully.");
            } else {
                System.out.println("\n[INFO] Book not found with ISBN " + isbn + " or it was not marked as issued.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR] Database error during returnBook: " + e.getMessage());
        }
    }

    public void viewAllBooks() {
        String sql = "SELECT id, title, author, isbn, is_issued FROM books ORDER BY title";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- LIBRARY CATALOG ---");
            if (!rs.isBeforeFirst()) {
                System.out.println("The library catalog is empty.");
                return;
            }

            System.out.printf("%-5s | %-30s | %-20s | %-15s | %-10s\n",
                    "ID", "TITLE", "AUTHOR", "ISBN", "STATUS");
            System.out.println("----------------------------------------------------------------------------------");

            while (rs.next()) {
                String status = rs.getBoolean("is_issued") ? "ISSUED" : "AVAILABLE";
                System.out.printf("%-5d | %-30s | %-20s | %-15s | %-10s\n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        status);
            }
            System.out.println("----------------------------------------------------------------------------------\n");

        } catch (SQLException e) {
            System.err.println("\n[ERROR] Database error during viewAllBooks: " + e.getMessage());
        }
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. View All Books");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }

            try {
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        issueBook();
                        break;
                    case 3:
                        returnBook();
                        break;
                    case 4:
                        viewAllBooks();
                        break;
                    case 5:
                        System.out.println("Exiting Library System. Goodbye!");
                        break;
                    default:
                        if (choice != 0) {
                            System.out.println("Invalid option. Please try again.");
                        }
                        break;
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }

        } while (choice != 5);
        scanner.close();
    }

    public static void main(String[] args) {
        new LibraryManager().run();
    }
}
