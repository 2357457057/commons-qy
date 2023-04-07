package top.yqingyu.common.utils;



import java.util.regex.Pattern;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.RegExUtil
 * @description
 * @createTime 2023年04月08日 01:59:00
 */
public class RegExUtil {

    /**
     * <p>Removes each substring of the text String that matches the given regular expression pattern.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removeAll(null, *)      = null
     * StringUtil.removeAll("any", (Pattern) null)  = "any"
     * StringUtil.removeAll("any", Pattern.compile(""))    = "any"
     * StringUtil.removeAll("any", Pattern.compile(".*"))  = ""
     * StringUtil.removeAll("any", Pattern.compile(".+"))  = ""
     * StringUtil.removeAll("abc", Pattern.compile(".?"))  = ""
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\nB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL))  = "AB"
     * StringUtil.removeAll("ABCabc123abc", Pattern.compile("[a-z]"))     = "ABC123"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex  the regular expression to which this string is to be matched
     * @return  the text with any removes processed,
     *              {@code null} if null String input
     *
     * @see #replaceAll(String, Pattern, String)
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see java.util.regex.Pattern
     */
    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes each substring of the text String that matches the given regular expression.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>Unlike in the {@link #removePattern(String, String)} method, the {@link Pattern#DOTALL} option
     * is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.removeAll(null, *)      = null
     * StringUtil.removeAll("any", (String) null)  = "any"
     * StringUtil.removeAll("any", "")    = "any"
     * StringUtil.removeAll("any", ".*")  = ""
     * StringUtil.removeAll("any", ".+")  = ""
     * StringUtil.removeAll("abc", ".?")  = ""
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\nB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * StringUtil.removeAll("ABCabc123abc", "[a-z]")     = "ABC123"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex  the regular expression to which this string is to be matched
     * @return  the text with any removes processed,
     *              {@code null} if null String input
     *
     * @throws  java.util.regex.PatternSyntaxException
     *              if the regular expression's syntax is invalid
     *
     * @see #replaceAll(String, String, String)
     * @see #removePattern(String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String removeAll(final String text, final String regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes the first substring of the text string that matches the given regular expression pattern.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removeFirst(null, *)      = null
     * StringUtil.removeFirst("any", (Pattern) null)  = "any"
     * StringUtil.removeFirst("any", Pattern.compile(""))    = "any"
     * StringUtil.removeFirst("any", Pattern.compile(".*"))  = ""
     * StringUtil.removeFirst("any", Pattern.compile(".+"))  = ""
     * StringUtil.removeFirst("abc", Pattern.compile(".?"))  = "bc"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\n&lt;__&gt;B"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * StringUtil.removeFirst("ABCabc123", Pattern.compile("[a-z]"))          = "ABCbc123"
     * StringUtil.removeFirst("ABCabc123abc", Pattern.compile("[a-z]+"))      = "ABC123abc"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex  the regular expression pattern to which this string is to be matched
     * @return  the text with the first replacement processed,
     *              {@code null} if null String input
     *
     * @see #replaceFirst(String, Pattern, String)
     * @see java.util.regex.Matcher#replaceFirst(String)
     * @see java.util.regex.Pattern
     */
    public static String removeFirst(final String text, final Pattern regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes the first substring of the text string that matches the given regular expression.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.removeFirst(null, *)      = null
     * StringUtil.removeFirst("any", (String) null)  = "any"
     * StringUtil.removeFirst("any", "")    = "any"
     * StringUtil.removeFirst("any", ".*")  = ""
     * StringUtil.removeFirst("any", ".+")  = ""
     * StringUtil.removeFirst("abc", ".?")  = "bc"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\n&lt;__&gt;B"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * StringUtil.removeFirst("ABCabc123", "[a-z]")          = "ABCbc123"
     * StringUtil.removeFirst("ABCabc123abc", "[a-z]+")      = "ABC123abc"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex  the regular expression to which this string is to be matched
     * @return  the text with the first replacement processed,
     *              {@code null} if null String input
     *
     * @throws  java.util.regex.PatternSyntaxException
     *              if the regular expression's syntax is invalid
     *
     * @see #replaceFirst(String, String, String)
     * @see String#replaceFirst(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String removeFirst(final String text, final String regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes each substring of the source String that matches the given regular expression using the DOTALL option.</p>
     *
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, StringUtil.EMPTY)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removePattern(null, *)       = null
     * StringUtil.removePattern("any", (String) null)   = "any"
     * StringUtil.removePattern("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")  = "AB"
     * StringUtil.removePattern("ABCabc123", "[a-z]")    = "ABC123"
     * </pre>
     *
     * @param text
     *            the source string
     * @param regex
     *            the regular expression to which this string is to be matched
     * @return The resulting {@code String}
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String removePattern(final String text, final String regex) {
        return replacePattern(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Replaces each substring of the text String that matches the given regular expression pattern with the given replacement.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.replaceAll(null, *, *)       = null
     * StringUtil.replaceAll("any", (Pattern) null, *)   = "any"
     * StringUtil.replaceAll("any", *, null)   = "any"
     * StringUtil.replaceAll("", Pattern.compile(""), "zzz")    = "zzz"
     * StringUtil.replaceAll("", Pattern.compile(".*"), "zzz")  = "zzz"
     * StringUtil.replaceAll("", Pattern.compile(".+"), "zzz")  = ""
     * StringUtil.replaceAll("abc", Pattern.compile(""), "ZZ")  = "ZZaZZbZZcZZ"
     * StringUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")                 = "z\nz"
     * StringUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL), "z") = "z"
     * StringUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")             = "z"
     * StringUtil.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_")       = "ABC___123"
     * StringUtil.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123"
     * StringUtil.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123"
     * StringUtil.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param regex  the regular expression pattern to which this string is to be matched
     * @param replacement  the string to be substituted for each match
     * @return  the text with any replacements processed,
     *              {@code null} if null String input
     *
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see java.util.regex.Pattern
     */
    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (ObjectUtil.anyNull(text, regex, replacement)) {
            return text;
        }
        return regex.matcher(text).replaceAll(replacement);
    }

    /**
     * <p>Replaces each substring of the text String that matches the given regular expression
     * with the given replacement.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>Unlike in the {@link #replacePattern(String, String, String)} method, the {@link Pattern#DOTALL} option
     * is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.replaceAll(null, *, *)       = null
     * StringUtil.replaceAll("any", (String) null, *)   = "any"
     * StringUtil.replaceAll("any", *, null)   = "any"
     * StringUtil.replaceAll("", "", "zzz")    = "zzz"
     * StringUtil.replaceAll("", ".*", "zzz")  = "zzz"
     * StringUtil.replaceAll("", ".+", "zzz")  = ""
     * StringUtil.replaceAll("abc", "", "ZZ")  = "ZZaZZbZZcZZ"
     * StringUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\nz"
     * StringUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
     * StringUtil.replaceAll("ABCabc123", "[a-z]", "_")       = "ABC___123"
     * StringUtil.replaceAll("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
     * StringUtil.replaceAll("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
     * StringUtil.replaceAll("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param regex  the regular expression to which this string is to be matched
     * @param replacement  the string to be substituted for each match
     * @return  the text with any replacements processed,
     *              {@code null} if null String input
     *
     * @throws  java.util.regex.PatternSyntaxException
     *              if the regular expression's syntax is invalid
     *
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String replaceAll(final String text, final String regex, final String replacement) {
        if (ObjectUtil.anyNull(text, regex, replacement)) {
            return text;
        }
        return text.replaceAll(regex, replacement);
    }

    /**
     * <p>Replaces the first substring of the text string that matches the given regular expression pattern
     * with the given replacement.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.replaceFirst(null, *, *)       = null
     * StringUtil.replaceFirst("any", (Pattern) null, *)   = "any"
     * StringUtil.replaceFirst("any", *, null)   = "any"
     * StringUtil.replaceFirst("", Pattern.compile(""), "zzz")    = "zzz"
     * StringUtil.replaceFirst("", Pattern.compile(".*"), "zzz")  = "zzz"
     * StringUtil.replaceFirst("", Pattern.compile(".+"), "zzz")  = ""
     * StringUtil.replaceFirst("abc", Pattern.compile(""), "ZZ")  = "ZZabc"
     * StringUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")      = "z\n&lt;__&gt;"
     * StringUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")  = "z"
     * StringUtil.replaceFirst("ABCabc123", Pattern.compile("[a-z]"), "_")          = "ABC_bc123"
     * StringUtil.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123abc"
     * StringUtil.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123abc"
     * StringUtil.replaceFirst("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum  dolor   sit"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param regex  the regular expression pattern to which this string is to be matched
     * @param replacement  the string to be substituted for the first match
     * @return  the text with the first replacement processed,
     *              {@code null} if null String input
     *
     * @see java.util.regex.Matcher#replaceFirst(String)
     * @see java.util.regex.Pattern
     */
    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null|| replacement == null ) {
            return text;
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    /**
     * <p>Replaces the first substring of the text string that matches the given regular expression
     * with the given replacement.</p>
     *
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.replaceFirst(null, *, *)       = null
     * StringUtil.replaceFirst("any", (String) null, *)   = "any"
     * StringUtil.replaceFirst("any", *, null)   = "any"
     * StringUtil.replaceFirst("", "", "zzz")    = "zzz"
     * StringUtil.replaceFirst("", ".*", "zzz")  = "zzz"
     * StringUtil.replaceFirst("", ".+", "zzz")  = ""
     * StringUtil.replaceFirst("abc", "", "ZZ")  = "ZZabc"
     * StringUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\n&lt;__&gt;"
     * StringUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
     * StringUtil.replaceFirst("ABCabc123", "[a-z]", "_")          = "ABC_bc123"
     * StringUtil.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "_")  = "ABC_123abc"
     * StringUtil.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "")   = "ABC123abc"
     * StringUtil.replaceFirst("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum  dolor   sit"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param regex  the regular expression to which this string is to be matched
     * @param replacement  the string to be substituted for the first match
     * @return  the text with the first replacement processed,
     *              {@code null} if null String input
     *
     * @throws  java.util.regex.PatternSyntaxException
     *              if the regular expression's syntax is invalid
     *
     * @see String#replaceFirst(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String replaceFirst(final String text, final String regex, final String replacement) {
        if (text == null || regex == null|| replacement == null ) {
            return text;
        }
        return text.replaceFirst(regex, replacement);
    }

    /**
     * <p>Replaces each substring of the source String that matches the given regular expression with the given
     * replacement using the {@link Pattern#DOTALL} option. DOTALL is also known as single-line mode in Perl.</p>
     *
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.replacePattern(null, *, *)       = null
     * StringUtil.replacePattern("any", (String) null, *)   = "any"
     * StringUtil.replacePattern("any", *, null)   = "any"
     * StringUtil.replacePattern("", "", "zzz")    = "zzz"
     * StringUtil.replacePattern("", ".*", "zzz")  = "zzz"
     * StringUtil.replacePattern("", ".+", "zzz")  = ""
     * StringUtil.replacePattern("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")       = "z"
     * StringUtil.replacePattern("ABCabc123", "[a-z]", "_")       = "ABC___123"
     * StringUtil.replacePattern("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
     * StringUtil.replacePattern("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
     * StringUtil.replacePattern("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text
     *            the source string
     * @param regex
     *            the regular expression to which this string is to be matched
     * @param replacement
     *            the string to be substituted for each match
     * @return The resulting {@code String}
     * @see #replaceAll(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String replacePattern(final String text, final String regex, final String replacement) {
        if (ObjectUtil.anyNull(text, regex, replacement)) {
            return text;
        }
        return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
    }

}
