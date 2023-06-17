package org.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final boolean ENABLE_VALIDATION = true;

    record Result(Set<String> words, long timeInMilliseconds) {
        @Override
        public String toString() {
            return String.format("""
                    timeInMilliseconds = %d\n
                    Words: [
                    %s
                    ]
                    """, timeInMilliseconds, String.join("\n", words));
        }
    }


    public static void main(String[] args) {
        try {
            final Result result = exercise();
            System.out.println(result);
        }
        catch(final Exception e) {
            throw new Error(e);
        }
    }

    static Result exercise() throws IOException {
        final Set<String> words = words();

        final long start = System.currentTimeMillis();

        final Set<String> input = new HashSet<>();
        final Set<String> check = new HashSet<>();
        final Set<String> result = new HashSet<>();
        final Map<String, List<String>> validation = new HashMap<>();
        //n
        for(final String word : words) {
            //9^2
            if(word.length() == 9 && (word.contains("I") || word.contains("A"))) {
                input.add(word);
            }
            else if(word.length() < 9) {
                check.add(word);
            }
        }

        //m
        for(final String word : input) {
            //9
            for(int i = 0; i < word.length(); i++) {
                final List<String> composingWords = new ArrayList<>();
                // 8 * 5040
                if(matches(new StringBuilder(word).deleteCharAt(i).toString(), check, composingWords)) {
                    if(ENABLE_VALIDATION) {
                        validation.put(word, composingWords);
                    }
                    result.add(word);
                    break;
                }
            }
        }

        final long end = System.currentTimeMillis();
        System.out.println("validation = " + validation + "\n");
        return new Result(result, end - start);
    }

    private static boolean matches(final String word, final Set<String> check, List<String> composingWords) {
        if(word.length() == 1 && ("A".equals(word) || "I".equals(word))) {
            //1
            if(ENABLE_VALIDATION) {
                composingWords.add(word);
            }
            return true;
        }

        if(word.length() > 1 && check.contains(word)) {// 1
            //8
            for(int i = 0; i < word.length(); i++) {
                //          b1 b2 ... b7 - 7
                //          |
                //     c1 c2 ... c6 - 6 * 7 = 42
                //     .... 5 * 6 * 7 = 210
                //     .... 4 * 5 * 6 * 7 = 840
                //     .... 3 * 4 * 5 * 6 * 7 = 2520
                //     .... 2 * 3 * 4 * 5 * 6 * 7 = 5040
                //     1
                //=
                //
                if(matches(new StringBuilder(word).deleteCharAt(i).toString(), check, composingWords)) {
                    if(ENABLE_VALIDATION) {
                        composingWords.add(word);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    static Set<String> words() throws IOException {
        final URL url = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try(final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().skip(2).collect(Collectors.toSet());
        }
    }

}
