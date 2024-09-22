package com.example.twitsnapgateway.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    /**
     * Loads environment variables from the .env file and sets them
     * as system properties.
     */
    public static void setEnv(){
        try {
            Dotenv dotenv = Dotenv.load();
            setProperties(dotenv);
        } catch(Exception e){
            System.out.println("Error loading environment variables: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void setProperties(Dotenv dotenv){
        setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));

        setProperty("GENERAL_LOG_PATH", dotenv.get("GENERAL_LOG_PATH"));
        setProperty("GENERAL_LOG_LEVEL", dotenv.get("GENERAL_LOG_LEVEL"));
        setProperty(("MAX_GENERAL_LOGS"), dotenv.get("MAX_GENERAL_LOGS"));
        setProperty(("GENERAL_ROTATED_LOG_PATTERN"), dotenv.get("GENERAL_ROTATED_LOG_PATTERN"));
        setProperty(("GENERAL_LOG_PATTERN"), dotenv.get("GENERAL_LOG_PATTERN"));

        setProperty(("TRAFFIC_LOG_PATH"), dotenv.get("TRAFFIC_LOG_PATH"));
        setProperty(("TRAFFIC_LOG_LEVEL"), dotenv.get("TRAFFIC_LOG_LEVEL"));
        setProperty(("MAX_TRAFFIC_LOGS"), dotenv.get("MAX_TRAFFIC_LOGS"));
        setProperty(("TRAFFIC_ROTATED_LOG_PATTERN"), dotenv.get("TRAFFIC_ROTATED_LOG_PATTERN"));
        setProperty(("TRAFFIC_LOG_PATTERN"), dotenv.get("TRAFFIC_LOG_PATTERN"));
        setProperty(("ROOT_LOG_LEVEL"), dotenv.get("ROOT_LOG_LEVEL"));
    }

    /**
     * Sets a system property with the provided key and value.
     *
     * @param key   The key of the property to set.
     * @param value The value of the property to set.
     */
    private static void setProperty(String key, String value){
        System.setProperty(key, value);
    }
}
