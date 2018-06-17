package net.upd4ting.gameapi.databaselib;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import net.upd4ting.gameapi.databaselib.DatabaseConnection.ConnectionType;
import net.upd4ting.gameapi.databaselib.DatabaseConnection.Credentials;

public class ConnectionPool {
    
    public interface Listener {
        void onConnectionCreated();
        void onConnectionRemoved();
    }
    
    private final Credentials credentials;
    private final ConnectionType type;
    private final LinkedList<DatabaseConnection> pools;
    private final LinkedList<Timer> timers;
    private final ArrayList<Listener> listeners;
    
    public ConnectionPool(ConnectionType type, Credentials credentials) {
        this.type = type;
        this.credentials = credentials;
        this.pools = new LinkedList<>();
        this.timers = new LinkedList<>();
        this.listeners = new ArrayList<>();
    }
    
    public DatabaseConnection getConnection() {
        if (this.pools.isEmpty()) {
            DatabaseConnection con = DatabaseConnection.create(type);
            con.connect(this.credentials);
            this.pools.add(con);
            
            for (Listener listener : listeners)
                listener.onConnectionCreated();
        } else {
            Timer timer = this.timers.pop();
            timer.cancel();
        }
        
        return this.pools.pop();
    }
    
    public void returnConnection(DatabaseConnection connection) {
        this.pools.push(connection);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                connection.close();
                timers.remove(timer);
                pools.remove(connection);
                
                for (Listener listener : listeners)
                    listener.onConnectionRemoved();
            }
        }, 5 * 60 * 1000L); // 5 Minutes d'inactivité on ferme la connection
        this.timers.push(timer);
    }
    
    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public ConnectionType getType() {
        return type;
    }
}
