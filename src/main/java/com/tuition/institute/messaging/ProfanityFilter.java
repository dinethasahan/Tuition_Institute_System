package com.tuition.institute.messaging;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProfanityFilter {

    private final Set<String> blacklistedWords = new HashSet<>(Arrays.asList(
            "abuse", "harass", "hate", "scam", "cheat", "fraud"
    ));

    public boolean containsProfanity(String text) {
        if (text == null || text.isBlank()) return false;
        String lower = text.toLowerCase();
        for (String word : blacklistedWords) {
            if (lower.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
