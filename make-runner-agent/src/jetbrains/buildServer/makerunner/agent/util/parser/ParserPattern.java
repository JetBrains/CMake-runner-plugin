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

package jetbrains.buildServer.makerunner.agent.util.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public abstract class ParserPattern {
  private final Pattern myPattern;

  public ParserPattern(@NotNull final String pattern) {
    this.myPattern = Pattern.compile(pattern);
  }

  public ParserPattern(@NotNull final Pattern pattern) {
    this.myPattern = pattern;
  }

  public String getPattern() {
    return myPattern.toString();
  }

  @NotNull
  public Matcher getMatcher(@NotNull final CharSequence input) {
    return myPattern.matcher(input);
  }

  /**
   * @param line input text line
   * @return parsed message or null if cannot parse
   */
  @Nullable
  abstract public ParsedMessage tryParse(@NotNull final String line);

}
