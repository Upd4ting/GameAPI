package net.upd4ting.gameapi.databaselib;

import java.util.ArrayList;
import java.util.Map;

public interface Callback {
    void run(ArrayList<Map<String, Object>> result);
}
