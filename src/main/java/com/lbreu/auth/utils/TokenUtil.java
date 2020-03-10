package com.lbreu.auth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TokenUtil {

    private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);

    public static String createToken() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        while (stringBuilder.length() < 120) {
            stringBuilder.append(Integer.toHexString(random.nextInt()));
        }

        String token = stringBuilder.toString();
        log.info("token=" + token);
        return token;
    }
}
