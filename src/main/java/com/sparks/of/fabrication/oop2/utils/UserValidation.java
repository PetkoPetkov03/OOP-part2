package com.sparks.of.fabrication.oop2.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;



public class UserValidation implements Validation {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_REGEX_SPECIAL_CHARS = Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*");
    private static final Pattern PASSWORD_REGEX_CHARS = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern PASSWORD_REGEX_NUMS = Pattern.compile(".*[0-9].*");

    private final Logger log = LogManager.getLogger(this.getClass());

    private Pair<Boolean, String> validateEmail(String email) {
        if(email == null) {
            log.error("Email is null");

            return new Pair<>(false, "Email is null");
        }

        if(email.isEmpty()) {
            log.error("Email string is empty!");

            return new Pair<>(false, "Email string is empty!");
        }

        if(!EMAIL_REGEX.matcher(email).matches()) {
            return new Pair<>(false, "The email is not valid!");
        }

        return new Pair<>(true, "Email is valid!");
    }

    private Pair<Boolean, String> validatePassword(String password) {
        if(password == null) {
            log.error("Password is null!");

            return new Pair<>(false, "Password is null!");
        }

        if(password.isEmpty()) {
            log.error("Password field is empty!");

            return new Pair<>(false, "Password field is empty!");
        }

        if(password.length() < 9 || password.length() > 30) {
            return new Pair<>(false, "Password should be between 9 and 30 characters long!");
        }

        if(!PASSWORD_REGEX_CHARS.matcher(password).matches()) {
            return new Pair<>(false, "Password should contain at least one character a - Z");
        }

        if(!PASSWORD_REGEX_NUMS.matcher(password).matches()) {
            return new Pair<>(false, "Password should contain at least one number!");
        }

        if(!PASSWORD_REGEX_SPECIAL_CHARS.matcher(password).matches()) {
            return new Pair<>(false, "Password should contain at least one special character!");
        }

        return new Pair<>(true, "Password is valid");
    }

    @Override
    public Pair<Boolean, List<String>> validate(List<ValidationTypes> vTypes, List<String> inputs) {
        List<String> responses = new ArrayList<>();
        List<Boolean> boolResponses = new ArrayList<>();
        if(vTypes == null || inputs == null) {
            log.error("Validation error: One or both input lists are null.");
            throw new IllegalArgumentException("Input lists cannot be null.");
        }

        if(vTypes.size() != inputs.size()) {
            log.error("Validation error: Non-matching sizes. ValidationTypes size: {}, Inputs size: {}",
                    vTypes.size(), inputs.size());
            throw new IllegalArgumentException("ValidationTypes and inputs lists must have the same size.");
        }

        AtomicInteger index = new AtomicInteger();

        vTypes.forEach((type) -> {
            switch (type) {
                case EMAIL -> {
                    Pair<Boolean, String> validatedEmail = validateEmail(inputs.get(index.getAndIncrement()));

                    responses.add(validatedEmail.y());
                    boolResponses.add(validatedEmail.x());
                }
                case PASSWORD -> {
                    Pair<Boolean, String> validatedPassword = validatePassword(inputs.get(index.getAndIncrement()));

                    responses.add(validatedPassword.y());
                    boolResponses.add(validatedPassword.x());
                }
            }
        });

        return new Pair<>(!boolResponses.contains(Boolean.FALSE), responses);
    }
}
