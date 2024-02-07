

package jetbrains.buildServer.cmakerunner.regexparser;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
@XStreamAlias("pattern")
public class RegexPattern {
  @NotNull
  private static final Severity DEFAULT_SEVERITY = Severity.SPECIAL;
  @NotNull
  private static final String DEFAULT_DESCRIPTION_EXPR = "$0";
  private static final boolean DEFAULT_EAT_LINE = true;

  @NotNull
  @XStreamAlias("regex")
  @XStreamAsAttribute
  @XStreamConverter(PatternConverter.class)
  private final Pattern myPattern;
  @XStreamAlias("output-expr")
  @XStreamAsAttribute
  private String myDescriptionExpression;
  @XStreamAlias("severity")
  @XStreamAsAttribute
//  @XStreamConverter(SeverityConverter.class)
  private Severity mySeverity;
  @XStreamAlias("eat-line")
  @XStreamAsAttribute
  private Boolean myEatLine;

  public RegexPattern(@NotNull final Pattern pattern, @NotNull final String descriptionExpression, @NotNull final Severity severity, final boolean eatLine) {
    this.myPattern = pattern;
    this.myDescriptionExpression = descriptionExpression;
    this.mySeverity = severity;
    this.myEatLine = eatLine;
  }

  @NotNull
  public Pattern getPattern() {
    return myPattern;
  }

  public String getDescriptionExpression() {
    return myDescriptionExpression;
  }

  public Severity getSeverity() {
    return mySeverity;
  }

  public boolean getEatLine() {
    return myEatLine;
  }

  private String parseStr(@NotNull final Matcher matcher, @NotNull final String str) {
    return matcher.replaceAll(str);
  }

  /**
   * @param input - input line.
   * @return matcher to interpret the input line.
   */
  @NotNull
  private Matcher getMatcher(@NotNull final CharSequence input) {
    return myPattern.matcher(input);
  }

  /**
   * @param line    - input line
   * @param matcher - matcher to parse the input line.
   * @return parsed description or {@code null}.
   */
  protected String getDescription(String line, @NotNull final Matcher matcher) {
    if ("$line".equals(myDescriptionExpression)) {
      return line;
    }
    if (StringUtil.isEmptyOrSpaces(myDescriptionExpression)) {
      return line;
    }
    return parseStr(matcher, myDescriptionExpression);
  }


  /**
   * Parse a line of build output and register error/warning for
   * Problems view.
   *
   * @param line          - one line of output.
   * @param parserManager - {@link ParserManager}.
   * @return {@code true} if error/warning/info problem was found.
   */
  public boolean processLine(@NotNull final String line, @NotNull final ParserManager parserManager) {
    final Matcher matcher = getMatcher(line);
    // pattern should cover the whole line
    if (!(matcher.find() && matcher.group(0).length() == line.length()))
      return false;

    applyToManager(line, matcher, parserManager);
    return myEatLine;
  }

  /**
   * Log matched string into {@link ParserManager}.
   * @param line          - input line
   * @param matcher       - matcher to parse the input line.
   * @param parserManager - {@link ParserManager}.
   */
  protected void applyToManager(@NotNull final String line, @NotNull final Matcher matcher, @NotNull final ParserManager parserManager) {
    parserManager.log(getDescription(line, matcher), mySeverity);
  }

  /**
   * Special for XStream. Setting null params to defaults.
   *
   * @return this
   */
  @NotNull
  @SuppressWarnings({"UnusedDeclaration"})
  private Object readResolve() {
    if (myEatLine == null) myEatLine = DEFAULT_EAT_LINE;
    if (myDescriptionExpression == null) myDescriptionExpression = DEFAULT_DESCRIPTION_EXPR;
    if (mySeverity == null) mySeverity = DEFAULT_SEVERITY;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof RegexPattern) {
      final RegexPattern rp = (RegexPattern) o;
      return this.myEatLine.equals(rp.myEatLine)
              && this.mySeverity.equals(rp.mySeverity)
              && this.myDescriptionExpression.equals(rp.myDescriptionExpression)
              && this.myPattern.pattern().equals(rp.myPattern.pattern());
    }
    return super.equals(o);
  }

  public static class PatternConverter extends AbstractSingleValueConverter {
    public String toString(@NotNull final Object o) {
      return ((Pattern) o).pattern();
    }

    public Object fromString(final String s) {
      return Pattern.compile(s);
    }

    public boolean canConvert(final Class aClass) {
      return Pattern.class.equals(aClass);
    }
  }

}