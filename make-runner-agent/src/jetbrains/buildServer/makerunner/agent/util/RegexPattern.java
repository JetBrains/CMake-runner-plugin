/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.makerunner.agent.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexPattern {
  private static final Severity DEFAULT_SEVERITY = Severity.SPECIAL;
  private static final String DEFAULT_DESCRIPTION_EXPR = "$0";
  private static final boolean DEFAULT_EAT_LINE = true;

  private final java.util.regex.Pattern myPattern;
  private String myDescriptionExpression;
  private Severity mySeverity;
  private Boolean myEatLine;

  public RegexPattern(@NotNull final Pattern pattern, @NotNull final String descriptionExpression, @NotNull final Severity severity, final boolean eatLine) {
    this.myPattern = pattern;
    this.myDescriptionExpression = descriptionExpression;
    this.mySeverity = severity;
    this.myEatLine = eatLine;
  }

  public Pattern getPattern() {
    return myPattern;
  }

  public String getDescriptionExpression() {
    return myDescriptionExpression;
  }

  public Severity getSeverity() {
    return mySeverity;
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
   * @param matcher - matcher to parse the input line.
   * @return parsed description or {@code null}.
   */
  protected String getDescription(@NotNull final Matcher matcher) {
    return parseStr(matcher, myDescriptionExpression);
  }


  /**
   * Parse a line of build output and register error/warning for
   * Problems view.
   *
   * @param line    - one line of output.
   * @param manager - {@link Manager}.
   * @return {@code true} if error/warning/info problem was found.
   */
  public boolean processLine(@NotNull final String line, @NotNull final Manager manager) {
    final Matcher matcher = getMatcher(line);
    // pattern should cover the whole line
    if (!(matcher.find() && matcher.group(0).length() == line.length()))
      return false;

    applyToManager(matcher, manager);
    return myEatLine;
  }

  /**
   * Log matched string into {@link Manager}.
   *
   * @param matcher - matcher to parse the input line.
   * @param manager - {@link Manager}.
   */
  protected void applyToManager(final Matcher matcher, final Manager manager) {
    manager.log(getDescription(matcher), mySeverity);
  }

  public boolean getEatLine() {
    return myEatLine;
  }

  /**
   * Special for XStream. Setting null params to defaults.
   *
   * @return this
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private Object readResolve() {
    if (myEatLine == null) myEatLine = DEFAULT_EAT_LINE;
    if (myDescriptionExpression == null) myDescriptionExpression = DEFAULT_DESCRIPTION_EXPR;
    if (mySeverity == null) mySeverity = DEFAULT_SEVERITY;
    return this;
  }
}
