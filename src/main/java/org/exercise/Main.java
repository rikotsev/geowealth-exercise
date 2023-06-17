package org.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final boolean ENABLE_VALIDATION = false;

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

        final Set<String> check = new HashSet<>(words);
        final Set<String> result = new HashSet<>();
        final Map<String, List<String>> validation = new HashMap<>();

        //n
        for(final String word : words) {
            //9^2
            if(word.length() == 9 && (word.contains("I") || word.contains("A"))) {
                final List<String> composingWords = new ArrayList<>();

                if(matches(word, check, composingWords)) {
                    if(ENABLE_VALIDATION) {
                        validation.put(word, composingWords);
                    }
                    result.add(word);
                }
            }
        }

        final long end = System.currentTimeMillis();
        if(ENABLE_VALIDATION) {
            System.out.println("validation = " + validation + "\n");
        }
        return new Result(result, end - start);
    }

    //9!
    private static boolean matches(final String word, final Set<String> check, List<String> composingWords) {
        if(word.length() == 1 && ("A".equals(word) || "I".equals(word))) {
            //1
            if(ENABLE_VALIDATION) {
                composingWords.add(word);
            }
            return true;
        }

        if(word.length() > 1 && check.contains(word)) {// 1
            //9
            for(int i = 0; i < word.length(); i++) {
                //              a1 a2 ... a8 - 8
                //              |
                //          b1 b2 ... b7 - 7
                //          |
                //     c1 c2 ... c6 - 6 * 7 * 8
                //     .... 5 * 6 * 7 * 8
                //     .... 4 * 5 * 6 * 7 * 8
                //     .... 3 * 4 * 5 * 6 * 7 * 8
                //     .... 2 * 3 * 4 * 5 * 6 * 7 * 8
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

}
