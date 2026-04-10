package com.library.util;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private static final Object LOCK = new Object();

    private DatabaseConnection() {
        // Private constructor để ngăn tạo instance từ bên ngoài
        System.out.println("DatabaseConnection Singleton được khởi tạo!");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public void connect() {
        System.out.println("Kết nối đến Database thành công (Singleton Pattern)");
    }
}