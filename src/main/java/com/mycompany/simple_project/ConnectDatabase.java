package com.mycompany.simple_project;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.dbTable;

public class ConnectDatabase {

    private final String hostname = "localhost:3306";
    private final String dbName = "quanlyrapphim";
    private final String username = "root";
    private final String password = "123456";

    private final String url = "jdbc:mysql://" + hostname + "/" + dbName;

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public <T> List<T> select(String tenBang, String key, String value, Class<T> modelClass) throws SQLException {
        List<T> resultList = new ArrayList<>();
        String query = "SELECT * FROM " + tenBang;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(buildQuery(query, key, value))) {

            if (key != null && value != null) {
                pstmt.setString(1, value);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    T obj = modelClass.getDeclaredConstructor().newInstance();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        setProperty(obj, columnName, columnValue);
                    }
                    resultList.add(obj);
                }
            }
        } catch (SQLException | ReflectiveOperationException e) {
            throw new SQLException("Error executing query: " + e.getMessage(), e);
        }
        return resultList;
    }

    private String buildQuery(String baseQuery, String key, String value) {
        if (key != null && value != null) {
            return baseQuery + " WHERE " + key + " = ?";
        }
        return baseQuery;
    }

    private <T> void setProperty(T obj, String columnName, Object value)
            throws ReflectiveOperationException {
        Field field = obj.getClass().getDeclaredField(convertColumnToField(columnName));
        field.setAccessible(true);

        Class<?> fieldType = field.getType();

        if (value instanceof java.sql.Date && fieldType.equals(java.time.LocalDate.class)) {
            field.set(obj, ((java.sql.Date) value).toLocalDate());
        } else if (value instanceof java.sql.Timestamp && fieldType.equals(java.time.LocalDateTime.class)) {
            field.set(obj, ((java.sql.Timestamp) value).toLocalDateTime());
        } else if (value instanceof java.sql.Time && fieldType.equals(java.time.LocalTime.class)) {
            field.set(obj, ((java.sql.Time) value).toLocalTime());
        } else {
            field.set(obj, value);
        }
    }

    private String convertColumnToField(String columnName) {
        // Chuyển đổi tên cột SQL sang tên field Java (ví dụ: user_id -> userId)
        return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
    }

    public <T> boolean insert(String tenBang, T obj) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connect();

            // 1. Lấy tất cả các field @dbTable
            List<Field> allFields = getAnnotatedFields(obj.getClass());

            // 2. Tách ra insertFields (không autoIncrement) và idFields (autoIncrement)
            List<Field> insertFields = allFields.stream()
                    .filter(f -> !f.getAnnotation(dbTable.class).autoIncrement())
                    .collect(Collectors.toList());
            List<Field> idFields = allFields.stream()
                    .filter(f -> f.getAnnotation(dbTable.class).autoIncrement())
                    .collect(Collectors.toList());

            // 3. Build câu SQL
            String columns = insertFields.stream()
                    .map(f -> f.getAnnotation(dbTable.class).columnName())
                    .collect(Collectors.joining(", "));
            String placeholders = insertFields.stream()
                    .map(f -> "?")
                    .collect(Collectors.joining(", "));
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                    tenBang, columns, placeholders);

            // 4. Tạo PreparedStatement với RETURN_GENERATED_KEYS
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 5. Đổ giá trị vào câu lệnh
            for (int i = 0; i < insertFields.size(); i++) {
                Field f = insertFields.get(i);
                f.setAccessible(true);
                Object val = f.get(obj);
                pstmt.setObject(i + 1, val);
            }

            // 6. Thực thi INSERT
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            }

            // 7. Lấy generated keys và gán lại vào field ID (nếu có)
            if (!idFields.isEmpty()) {
                try (ResultSet genKeys = pstmt.getGeneratedKeys()) {
                    if (genKeys.next()) {
                        Object genObj = genKeys.getObject(1);
                        Field idField = idFields.get(0);
                        idField.setAccessible(true);

                        // Convert Number => đúng kiểu primitive/boxed của field
                        if (genObj instanceof Number) {
                            Number num = (Number) genObj;
                            Class<?> ft = idField.getType();
                            if (ft == int.class || ft == Integer.class) {
                                idField.set(obj, num.intValue());
                            } else if (ft == long.class || ft == Long.class) {
                                idField.set(obj, num.longValue());
                            } else {
                                // nếu muốn hỗ trợ BigInteger/BigDecimal
                                idField.set(obj, genObj);
                            }
                        } else {
                            // fallback cho các kiểu khác
                            idField.set(obj, genObj);
                        }
                    }
                }
            }
            return true;

        } catch (IllegalAccessException e) {
            throw new SQLException("Lỗi truy cập trường: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private <T> List<Field> getAnnotatedFields(Class<T> clazz) {
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(dbTable.class)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    public <T> boolean update(String tenBang, T obj) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connect();

            List<Field> fields = getAnnotatedFields(obj.getClass());
            Field idField = fields.stream()
                    .filter(f -> f.getAnnotation(dbTable.class).autoIncrement())
                    .findFirst()
                    .orElseThrow(() -> new SQLException("Không tìm thấy field autoIncrement"));

            // Danh sách field để cập nhật (loại trừ khóa chính)
            List<Field> updateFields = fields.stream()
                    .filter(f -> !f.getAnnotation(dbTable.class).autoIncrement())
                    .collect(Collectors.toList());

            // Tạo câu SQL update
            String setClause = updateFields.stream()
                    .map(f -> f.getAnnotation(dbTable.class).columnName() + " = ?")
                    .collect(Collectors.joining(", "));
            String sql = String.format("UPDATE %s SET %s WHERE %s = ?",
                    tenBang,
                    setClause,
                    idField.getAnnotation(dbTable.class).columnName());

            pstmt = conn.prepareStatement(sql);

            // Gán giá trị cho các tham số
            int index = 1;
            for (Field f : updateFields) {
                f.setAccessible(true);
                pstmt.setObject(index++, f.get(obj));
            }

            idField.setAccessible(true);
            pstmt.setObject(index, idField.get(obj)); // WHERE id = ?

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (IllegalAccessException e) {
            throw new SQLException("Lỗi truy cập field: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public <T> boolean delete(String tenBang, T obj) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connect();

            List<Field> fields = getAnnotatedFields(obj.getClass());
            Field idField = fields.stream()
                    .filter(f -> f.getAnnotation(dbTable.class).autoIncrement())
                    .findFirst()
                    .orElseThrow(() -> new SQLException("Không tìm thấy field autoIncrement"));

            String sql = String.format("DELETE FROM %s WHERE %s = ?",
                    tenBang,
                    idField.getAnnotation(dbTable.class).columnName());

            pstmt = conn.prepareStatement(sql);

            idField.setAccessible(true);
            pstmt.setObject(1, idField.get(obj));

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (IllegalAccessException e) {
            throw new SQLException("Lỗi truy cập field: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public <T> T selectLastRow(String tableName, String orderByColumn, Class<T> modelClass) throws SQLException {
        String sql = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 1", tableName, orderByColumn);

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                T obj = modelClass.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    setProperty(obj, columnName, value);
                }

                return obj;
            }

        } catch (SQLException | ReflectiveOperationException e) {
            throw new SQLException("Lỗi khi thực hiện truy vấn: " + e.getMessage(), e);
        }

        return null;
    }

    public <T> boolean deleteById(String tableName, int id, String columnName, Class<T> modelClass)
            throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, columnName);

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }

    }

    public boolean createSeatTableIfNotExists(String roomName, LocalDate date) {
        String tableName = roomName.toLowerCase().replaceAll("\\s+", "_") + "_"
                + date.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String checkSql = "SHOW TABLES LIKE ?";
        String createSql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "phong_chieu VARCHAR(50), "
                + "suat_chieu INT, "
                + "hang CHAR(1), "
                + "cot INT, "
                + "trang_thai TINYINT DEFAULT 0, "
                + "ma_khach_hang INT DEFAULT NULL"
                + ")";

        try (Connection conn = connect()) {
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới database!");
            }

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, tableName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Bảng đã tồn tại: " + tableName);
                    return false;
                }
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createSql);
                System.out.println("Đã tạo bảng: " + tableName);
            }

            // Chèn dữ liệu ghế
            String insertSql = "INSERT INTO " + tableName + " (phong_chieu, suat_chieu, hang, cot) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                String hangChu = "ABCDEFGHIJ"; // 10 hàng

                for (int suat = 1; suat <= 6; suat++) {
                    for (int i = 0; i < 10; i++) {
                        char hang = hangChu.charAt(i);
                        for (int cot = 1; cot <= 15; cot++) {
                            insertStmt.setString(1, roomName);
                            insertStmt.setInt(2, suat);
                            insertStmt.setString(3, String.valueOf(hang));
                            insertStmt.setInt(4, cot);
                            insertStmt.addBatch();
                        }
                    }
                }
                insertStmt.executeBatch();
                System.out.println("Đã chèn dữ liệu 900 ghế.");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
