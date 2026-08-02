package com.lms.frontend;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MessageBundleParityTest {

    @Test
    void englishAndVietnameseBundlesHaveTheSameNonBlankKeys() throws IOException {
        Properties english = load("messages.properties");
        Properties vietnamese = load("messages_vi.properties");

        assertEquals(english.stringPropertyNames(), vietnamese.stringPropertyNames(),
                "Every UI message must be available in both English and Vietnamese");
        english.forEach((key, value) -> assertFalse(value.toString().isBlank(), key + " is blank in English"));
        vietnamese.forEach((key, value) -> assertFalse(value.toString().isBlank(), key + " is blank in Vietnamese"));
    }

    private Properties load(String resource) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IOException("Missing resource: " + resource);
            }
            properties.load(input);
        }
        return properties;
    }
}
