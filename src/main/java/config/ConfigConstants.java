package config;

public class ConfigConstants {

    public static final String BASE_URL =
            System.getProperty("base.url", "https://wiremock:8080");

    public static final String SCHEMA_PATH = "/schema/CreateUser.json";
}