package com.example.twitsnapgateway.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    /**
     * Loads environment variables from the .env file and sets them
     * as system properties.
     */
    public static void setEnv(){
        try {
            //Dotenv dotenv = Dotenv.load();
            setProperties(null);
        } catch(Exception e){
            System.out.println("Error loading environment variables: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void setProperties(Dotenv dotenv){
        //setProperty("SERVER_HOST", dotenv.get("SERVER_HOST"));
        setProperty("SERVER_PORT", System.getenv("SERVER_PORT"));

        setProperty("GENERAL_LOG_PATH", System.getenv("GENERAL_LOG_PATH"));
        setProperty("GENERAL_LOG_LEVEL", System.getenv("GENERAL_LOG_LEVEL"));
        setProperty(("MAX_GENERAL_LOGS"), System.getenv("MAX_GENERAL_LOGS"));
        setProperty(("GENERAL_ROTATED_LOG_PATTERN"), System.getenv("GENERAL_ROTATED_LOG_PATTERN"));
        setProperty(("GENERAL_LOG_PATTERN"), System.getenv("GENERAL_LOG_PATTERN"));

        setProperty(("TRAFFIC_LOG_PATH"), System.getenv("TRAFFIC_LOG_PATH"));
        setProperty(("TRAFFIC_LOG_LEVEL"), System.getenv("TRAFFIC_LOG_LEVEL"));
        setProperty(("MAX_TRAFFIC_LOGS"), System.getenv("MAX_TRAFFIC_LOGS"));
        setProperty(("TRAFFIC_ROTATED_LOG_PATTERN"), System.getenv("TRAFFIC_ROTATED_LOG_PATTERN"));
        setProperty(("TRAFFIC_LOG_PATTERN"), System.getenv("TRAFFIC_LOG_PATTERN"));
        setProperty(("ROOT_LOG_LEVEL"), System.getenv("ROOT_LOG_LEVEL"));
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
