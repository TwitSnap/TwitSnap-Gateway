package com.example.twitsnapgateway.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;

public class Config {
    /**
     * Loads environment variables from the .env file and sets them
     * as system properties.
     */
    public static void setEnv(){
        try {
            File envFile = new File(".env");
            Dotenv dotenv = (envFile.exists() && !envFile.isDirectory()) ? Dotenv.load() : null;

            setProperties(dotenv);
        } catch(Exception e){
            System.out.println("Error loading environment variables: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void setProperties(Dotenv dotenv) {
        setEnvVar("SERVER_HOST", dotenv);
        setEnvVar("SERVER_PORT", dotenv);

        setEnvVar("GENERAL_LOG_PATH", dotenv);
        setEnvVar("GENERAL_LOG_LEVEL", dotenv);
        setEnvVar("MAX_GENERAL_LOGS", dotenv);
        setEnvVar("GENERAL_ROTATED_LOG_PATTERN", dotenv);
        setEnvVar("GENERAL_LOG_PATTERN", dotenv);

        setEnvVar("TRAFFIC_LOG_PATH", dotenv);
        setEnvVar("TRAFFIC_LOG_LEVEL", dotenv);
        setEnvVar("MAX_TRAFFIC_LOGS", dotenv);
        setEnvVar("TRAFFIC_ROTATED_LOG_PATTERN", dotenv);
        setEnvVar("TRAFFIC_LOG_PATTERN", dotenv);

        setEnvVar("ROOT_LOG_LEVEL", dotenv);
    }

    /**
     * Sets a system property with the provided key, using a lambda to check both
     * the .env file and system environment variables.
     *
     * @param key The key of the property to set.
     * @param dotenv The Dotenv object that holds the .env variables (can be null).
     */
    private static void setEnvVar(String key, Dotenv dotenv) {
        setProperty(key, () -> dotenv != null ? dotenv.get(key) : System.getenv(key));
    }

    /**
     * Functional interface to supply a value.
     */
    @FunctionalInterface
    interface ValueSupplier {
        String getValue();
    }

    /**
     * Sets a system property with a provided key and value retrieved via a lambda function.
     *
     * @param key   The key of the property to set.
     * @param valueSupplier A lambda function to supply the value.
     */
    private static void setProperty(String key, ValueSupplier valueSupplier) {
        String value = valueSupplier.getValue();

        if (value != null) System.setProperty(key, value);
    }
}
