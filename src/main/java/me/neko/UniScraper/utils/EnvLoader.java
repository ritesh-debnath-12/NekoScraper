package me.neko.UniScraper.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class EnvLoader {
    public static Map<String, String> loadEnv(String path) throws IOException {
        Map<String, String> env = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;

        while ((line = reader.readLine()) != null) {
            if ((line.trim().isEmpty())) {
                continue;
            }
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                env.put(parts[0].trim(), parts[1].trim());
            }
        }
        return env;
    }
}
