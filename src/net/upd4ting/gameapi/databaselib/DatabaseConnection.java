package net.upd4ting.gameapi.databaselib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

// Pour une meilleure représentation
// Map<String, Parameter> = un tuple SQL, -> clé = colonne, valeur = valeur de la colonne sous forme d'un object

public interface DatabaseConnection {
    
    enum ConnectionType {
        SQL, ORACLE
    }
    
    class Credentials {
    	final String ip;
    	final int port;
    	final String database;
    	final String username;
    	final String password;
    	
    	public Credentials(String ip, int port, String database, String username, String password) {
    		this.ip = ip;
    		this.port = port;
    		this.database = database;
    		this.username = username;
    		this.password = password;
    	}
    }

    boolean connect(Credentials cred);
    ArrayList<Map<String, Object>> executeQuery(String query, ArrayList<Object> parameters);
    boolean executeUpdate(String query, ArrayList<Object> parameters);
    boolean close();
    boolean isConnected();
    ArrayList<String> getColumns(String table);
    ArrayList<String> getTables();
    
    static DatabaseConnection create(ConnectionType type) {
        DatabaseConnection connection = null;
        
        if (type == ConnectionType.ORACLE)
            connection = new OracleConnection();
        else if (type == ConnectionType.SQL)
            connection = new SqlConnection();
        
        return connection;
    }
    
    // JPA like utils
    static <T> T map(Map<String, Object> result, Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            
            for (String f : result.keySet()) {
                Field field = clazz.getDeclaredField(f);
                field.setAccessible(true);
                field.set(object, result.get(f));
            }
            
            return object;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
