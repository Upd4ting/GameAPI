package net.upd4ting.gameapi.databaselib;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConnection implements DatabaseConnection {
    
    protected Connection connection;
    
    private PreparedStatement prepare(String query, List<Object> parameters) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            
            // On parcout la liste des parametres & on set
            for (int i = 1; i <= parameters.size(); i++) {
                System.out.println("I: " + i);
                Object o = parameters.get(i-1);
                statement.setObject(i, o);
            }
            
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public abstract boolean connect(Credentials cred);

    @Override
    public ArrayList<Map<String, Object>> executeQuery(String query, ArrayList<Object> parameters) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        
        PreparedStatement statement = prepare(query, parameters);
        
        try {
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                ResultSetMetaData metadata = rs.getMetaData();
                int columnsCount = metadata.getColumnCount();
                
                for (int i = 1; i <= columnsCount; i++) {
                    String name = metadata.getColumnName(i);
                    Object o = rs.getObject(i);
                    map.put(name, o);
                }
                
                result.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }

    @Override
    public boolean executeUpdate(String query, ArrayList<Object> parameters) {
        PreparedStatement statement = prepare(query, parameters);
        PreparedStatement statement2 = prepare("COMMIT", new ArrayList<>());
                
        try {
            statement.executeUpdate();
            statement2.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                statement.close();
                statement2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public ArrayList<String> getColumns(String table) {
        ArrayList<String> columns = new ArrayList<>();
        
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, table.toUpperCase(), null);

            while (resultSet.next()) {
                String name = resultSet.getString("COLUMN_NAME");
                columns.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return columns;
    }
    
    @Override
    public ArrayList<String> getTables() {
        ArrayList<String> tables = new ArrayList<>();
        
        List<Map<String, Object>> result = executeQuery("SELECT TABLE_NAME FROM USER_TABLES", new ArrayList<>());
        
        for (Map<String, Object> map : result)
            tables.add((String) map.get("TABLE_NAME"));
        
        return tables;
    }
}
