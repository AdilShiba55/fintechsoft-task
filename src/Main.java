import util.UtFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final static String TEMPLATE_REGEX = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
    private final static Integer DEFAULT_MATCH_COUNT = 1; // кол-во повторений по умолчанию, если оно не было задано в шаблоне

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String filesPath = "src/file/";
        File textFile = new File(filesPath + scanner.next());
        File patternsFile = new File(filesPath + scanner.next());
        File resultFile = new File(filesPath + scanner.next());
        if(!textFile.exists() || !patternsFile.exists() || !resultFile.exists()) {
            throw new RuntimeException("Не найден файл по названию");
        }

        String text = UtFile.getContent(textFile);
        List<String> patterns = UtFile.getContentAsList(patternsFile);

        String[] words = text.split("\\s+");
        StringBuilder contentToWrite = new StringBuilder();
        for(String pattern : patterns) {
            Integer patternMatchedCount = getPatternMatchedCount(words, pattern);
            contentToWrite.append(pattern).append(" : ").append(patternMatchedCount).append("\n");
            UtFile.writeWithRemove(resultFile, contentToWrite.toString());
            System.out.println(pattern + " : " + patternMatchedCount);
        }
    }

    private static Integer getPatternMatchedCount(String[] words, String pattern) {
        Map<String, Integer> subPatterns = getSubPatterns(pattern);
        int matchedWordCount = 0;
        for(String word : words) {
            if(isMatched(word, subPatterns)) {
                matchedWordCount++;
            }
        }
        return matchedWordCount;
    }

    private static Boolean isMatched(String word, Map<String, Integer> subPatterns) {
        int matchedSubPatternsCount = 0;
        for(String subPatternKey : subPatterns.keySet()) {
            Integer subPatternRepeatCount = subPatterns.get(subPatternKey);
            if(isMatched(word, subPatternKey, subPatternRepeatCount)) {
                matchedSubPatternsCount++;
            }
        }
        return matchedSubPatternsCount == subPatterns.size();
    }

    private static Boolean isMatched(String word, String subPatternKey, Integer subPatternRepeatCount) {
        String regex = String.format("(?:.*%s){%s}", subPatternKey, subPatternRepeatCount);
        Pattern patternReader = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = patternReader.matcher(word);
        return matcher.find();
    }

    private static Map<String, Integer> getSubPatterns(String patternsStr) {
        Map<String, Integer> result = new HashMap<>();
        String[] patterns = patternsStr.split(TEMPLATE_REGEX);
        for(int i = 0; i < patterns.length; i+=2) {
            String key = patterns[i];
            Integer value = i+1 < patterns.length ? Integer.valueOf(patterns[i+1]) : DEFAULT_MATCH_COUNT;
            result.put(key, value);
        }
        return result;
    }
}
