package com.koreait.hanGyeDolpa;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:2025/hangyedolpa_db";
        String user = "root";
        String password = "1234";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("데이터베이스에 성공적으로 연결되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
