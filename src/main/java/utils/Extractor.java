package utils;

import constant.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    public static String extractId(String url) {
    String regex = Constant.ARTICLE_REGEX;
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(url);
    while (matcher.find()) {
        return matcher.group(1);
    }
    return null;
}
}
