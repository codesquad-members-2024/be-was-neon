package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String YAML_FILE = "config.yaml";

    private static Map<String, Object> getYamlConfigs() {
        try (InputStream inputStream = new FileInputStream(YAML_FILE)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("[ERROR] config.yaml 파일을 찾을 수 없습니다: " + YAML_FILE);
            throw new IllegalArgumentException();
        } catch (IOException e) {
            logger.error("[ERROR] IO 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getEncoding() {
        Map<String, Object> yamlConfigs = getYamlConfigs();
        Object encoding = yamlConfigs.get("encoding");
        if (encoding == null) {
            logger.error("[ERROR] yaml.file의 encoding 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        return (String) encoding;
    }

    public static int getPort() {
        Map<String, Object> yamlConfigs = getYamlConfigs();
        Object port = yamlConfigs.get("port");
        if (port == null) {
            logger.error("[ERROR] yaml.file의 port 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        return (int) port;
    }

    public static String getStaticRoute() {
        Map<String, Object> yamlConfigs = getYamlConfigs();
        Object staticRoute = yamlConfigs.get("static_route");
        if (staticRoute == null) {
            logger.error("[ERROR] yaml.file의 static_route 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        return  (String) staticRoute;
    }



}
