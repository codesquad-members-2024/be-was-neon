package webserver.requesthandler.http.message;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
    private final Map<String, String> store;

    public Parameters() {
        this.store = new HashMap<>();
    }

    public Parameters(Map<String, String> store) {
        this.store = store;
    }

    public String get(String paramKey) {
        return store.get(paramKey);
    }
}
