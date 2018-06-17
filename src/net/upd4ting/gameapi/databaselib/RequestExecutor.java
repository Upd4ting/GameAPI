package net.upd4ting.gameapi.databaselib;

import java.util.ArrayList;
import java.util.Map;

public class RequestExecutor {
    
    public static void executeQuery(DatabaseConnection connection, String query, ArrayList<Object> parameters, Callback callback, Callback error) {
        Thread thread = new Thread(() -> {
            ArrayList<Map<String, Object>> result = connection.executeQuery(query, parameters);

            if (result == null)
                callback.run(result);
            else
                error.run(null);
        });
        
        thread.start();
    }
    
    public static void executeUpdate(DatabaseConnection connection, String query, ArrayList<Object> parameters, Callback callback, Callback error) {
        Thread thread = new Thread(() -> {
            boolean success = connection.executeUpdate(query, parameters);

            if (success)
                callback.run(null);
            else
                error.run(null);
        });
        
        thread.start();
    }
}
