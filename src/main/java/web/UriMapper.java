package web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriMapper {
    private volatile static UriMapper instance;
    private static final Logger logger = LoggerFactory.getLogger(UriMapper.class);
    private static final Map<String, HttpProcessor> URI_MAP = new HashMap<>();

    private UriMapper() {
        setUriMap();
    }

    public static synchronized UriMapper getInstance() {
        if (instance == null) {
            synchronized (UriMapper.class) {
                if (instance == null) {
                    instance = new UriMapper();
                }
            }
        }
        return instance;
    }

    private static void setUriMap() {
        URI_MAP.put("/", new HtmlProcessor());
        URI_MAP.put("/index.html", new HtmlProcessor());
        URI_MAP.put("/registration", new MemberSave());
    }

    public Optional<HttpProcessor> getProcessor(String uri) {
        if (!URI_MAP.containsKey(uri)) {
            logger.debug("[STATIC MAPPER] NOT FOUND: {}", uri);
            return Optional.empty();
        }
        return Optional.of(URI_MAP.get(uri));
    }
}
