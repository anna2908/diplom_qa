package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public DBUtils() {
    }

    private static String url = System.getProperty("db.url");
    private static String user = System.getProperty("db.user");
    private static String password = System.getProperty("db.password");


    public static void clearTables() {
        val deleteCreditRequestEntity = "DELETE FROM credit_request_entity";
        val deleteOrderEntity = "DELETE FROM order_entity";
        val deletePaymentEntity = "DELETE FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                url, user, password)
        ) {
            runner.update(conn, deleteCreditRequestEntity);
            runner.update(conn, deleteOrderEntity);
            runner.update(conn, deletePaymentEntity);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    public static String getStringData(String query) {
        String result = "";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                url, user, password)
        ) {
            result = runner.query(conn, query, new ScalarHandler<String>());
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static int getIntegerData(String query) {
        int result = -1;
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                url, user, password)
        ) {
            result = runner.query(conn, query, new ScalarHandler<Integer>());
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return -1;
    }
    public static String getPaymentStatus() {
        String status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getStringData(status);
    }


    public static String getCreditStatus() {
        String status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getStringData(status);
    }

    public static int getAmount() {
        String amount = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getIntegerData(amount);
    }

    public static String getTransactionId() {
        String id = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getStringData(id);
    }

    public static String getCreditId() {
        String id = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1";
        return getStringData(id);
    }

    public static String getPaymentId() {
        String id = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        return getStringData(id);
    }

    public static String getBankId() {
        String id = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getStringData(id);
    }
}