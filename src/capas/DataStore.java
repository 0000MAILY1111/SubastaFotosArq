package capas;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, Database> tables = new HashMap<>();

    private DataStore() {}

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Database table(String name) {
        Database db = tables.get(name);
        if (db == null) {
            db = new Database();
            tables.put(name, db);
        }
        return db;
    }
}

