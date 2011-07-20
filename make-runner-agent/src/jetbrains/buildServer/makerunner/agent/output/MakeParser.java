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

package jetbrains.buildServer.makerunner.agent.output;

import com.intellij.openapi.util.text.CharFilter;
import jetbrains.buildServer.makerunner.agent.output.messages.PrintMessage;
import jetbrains.buildServer.makerunner.agent.util.parser.ParsedMessage;
import jetbrains.buildServer.makerunner.agent.util.parser.Parser;
import jetbrains.buildServer.makerunner.agent.util.parser.ParserPattern;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParser extends Parser {

  public MakeParser(final List<ParserPattern> patterns) {
    super(patterns);
  }

  @NotNull
  public ParsedMessage parse(@NotNull final String text) {
    final String line = StringUtil.stripLeftAndRight(text, CharFilter.WHITESPACE_FILTER);
    for (final ParserPattern pattern : myPatterns) {
      final ParsedMessage message = pattern.tryParse(line);
      if (message != null) {
        return message;
      }
    }
    return new PrintMessage(text, PrintMessage.PrintType.MESSAGE);
  }
//
//  @Nullable
//  static ParsedMessage tryParseMustRemakeMessage(@NotNull final String text) {
//    final Matcher matcher = MAKE_MUST_REMAKE_TARGET_PATTERN.matcher(text);
//    if (!matcher.find()) return null;
//    final String targetName = matcher.group(1).trim();
//    return new ParsedMessage(text) {
//      @Override
//      public void apply(@NotNull final MakeOutputContext context) {
//        context.getLogger().blockStart(targetName);
//      }
//    };
//  }
//
//  @Nullable
//  static ParsedMessage tryParseTargetRemadeMessage(@NotNull final String text) {
//    final Matcher matcher = MAKE_TARGET_REMADE_PATTERN.matcher(text);
//    if (!matcher.find()) return null;
//    final String targetName = matcher.group(1).trim();
//    return new ParsedMessage(text) {
//      @Override
//      public void apply(@NotNull final MakeOutputContext context) {
//        context.getLogger().blockFinish(targetName);
//      }
//    };
//  }
}
