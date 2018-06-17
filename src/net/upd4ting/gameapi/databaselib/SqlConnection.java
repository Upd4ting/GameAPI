package net.upd4ting.gameapi.databaselib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection extends BaseConnection {

    @Override
    public boolean connect(Credentials cred) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + cred.ip + ":" + Integer.toString(cred.port) + "/" + cred.database, cred.username, cred.password);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }    
    }
    
}
