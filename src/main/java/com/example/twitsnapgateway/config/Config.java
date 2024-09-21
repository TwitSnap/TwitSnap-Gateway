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

            setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
            setProperty("LOG_PATH", dotenv.get("LOG_PATH"));
            setProperty("LOG_LEVEL", dotenv.get("LOG_LEVEL"));
        } catch(Exception e){
            System.out.println("Error loading environment variables: " + e.getMessage());
            System.exit(1);
        }
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
