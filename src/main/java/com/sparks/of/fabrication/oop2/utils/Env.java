package com.sparks.of.fabrication.oop2.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that loads and provides access to environment variables for database configuration.
 */
public class Env {
    private static final Logger log = LogManager.getLogger(Env.class);
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     * Constructor that loads environment variables for the database URL, user, and password.
     */
    public Env() {
        Dotenv dotenv = Dotenv.load();

        try {
            dbUrl = dotenv.get("DB_URL");
            dbUser = dotenv.get("DB_USER");
            dbPassword = dotenv.get("DB_PASSWORD");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the database URL.
     *
     * @return The database URL as a string.
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Gets the database user.
     *
     * @return The database user as a string.
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Gets the database password.
     *
     * @return The database password as a string.
     */
    public String getDbPassword() {
        return dbPassword;
    }
}
