package com.sparks.of.fabrication.oop2.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Env {
    private static final Logger log = LogManager.getLogger(Env.class);
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

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

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}

