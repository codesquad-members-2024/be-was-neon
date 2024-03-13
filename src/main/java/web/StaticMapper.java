package web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticMapper {
    private volatile static StaticMapper instance;
    private static final Logger logger = LoggerFactory.getLogger(StaticMapper.class);
    private static final Map<String, HttpProcessor> urlMap = new HashMap<>();

    private StaticMapper() {
        setUrlMap();
    }

    public static synchronized StaticMapper getInstance() {
        if (instance == null) {
            synchronized (StaticMapper.class) {
                if (instance == null) {
                    instance = new StaticMapper();
                }
            }
        }
        return instance;
    }

    private static void setUrlMap() {
        urlMap.put("/", new HtmlProcessor());
        urlMap.put("/index.html", new HtmlProcessor());
        urlMap.put("/registration", new MemberSave());
    }

    public Optional<HttpProcessor> getProcessor(String uri) {
        if (!urlMap.containsKey(uri)) {
            logger.debug("[STATIC MAPPER] NOT FOUND: {}", uri);
            return Optional.empty();
        }
        return Optional.of(urlMap.get(uri));
    }
}
