package se.d2collective.puttputtmatch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by msve on 2016-02-25.
 */
public final class StringValidator {
    public static boolean isNameValid(String name) {
        String namePattern = "^[a-zA-ZäöüåÄÖÜÅ ]+$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches() && !name.isEmpty();
    }
}
