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

import jetbrains.buildServer.makerunner.agent.output.messages.MakefileMessage;
import jetbrains.buildServer.makerunner.agent.output.messages.PrintMessage;
import jetbrains.buildServer.makerunner.agent.output.messages.RecipeTargetMessage;
import jetbrains.buildServer.makerunner.agent.util.parser.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserFactory implements ParserFactory {
//  private static final Pattern MAKE_MUST_REMAKE_TARGET_PATTERN = Pattern.compile("Must remake target `(.*)'\\.");
//  private static final Pattern MAKE_TARGET_REMADE_PATTERN = Pattern.compile("Successfully remade target file `(.*)'\\.");

  private static final ParserPattern PATTERN_MAKING_SOMETHING = new ParserPattern("Making (\\S+) in (\\S+)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;

      final String directory = matcher.group(2);
      return new ParsedMessage() {
        @NotNull
        private final MakeTarget myTarget = new MakeTarget(line, directory, line);

        public void apply(@NotNull final Context context) {
          if (context instanceof MakeContext && !directory.equals(".")) {
            final MakeContext moc = (MakeContext) context;
            moc.targetStart(line, directory, line);
          }
        }
      };
    }
  };
  private static final ParserPattern PATTERN_DIRECTORY_ENTER = new ParserPattern(".*make.*: Entering directory `(.*)'") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      return new PrintMessage(line, PrintMessage.PrintType.NONE);
    }
  };
  private static final ParserPattern PATTERN_DIRECTORY_LEAVE = new ParserPattern(".*make.*: Leaving directory `(.*)'") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      final String dirName = matcher.group(1);
      final String dirShortName = new File(dirName).getName();
      return new ParsedMessage() {
        public void apply(@NotNull final Context context) {
          if (context instanceof MakeContext) {
            final MakeContext moc = (MakeContext) context;
            if (moc.isLastTargetDirectory(dirShortName)) {
              moc.targetFinish();
            }
            moc.checkMainTaskFinished(dirShortName);
          }
        }
      };
    }
  };

  private static final ParserPattern PATTERN_MAKEFILE_ERROR = new ParserPattern("(.*):(\\d*): (\\*\\*\\* .*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      final String file = matcher.group(1);
      int lineNumber = 0;
      try {
        lineNumber = Integer.parseInt(matcher.group(2));
      } catch (NumberFormatException ignored) {
      }
      final String description = matcher.group(3);
      return new MakefileMessage(description, PrintMessage.PrintType.ERROR, file, lineNumber);
    }
  };
  private static final ParserPattern PATTERN_MAKEFILE_NO_SUCH_FILE_WARNING = new ParserPattern("(.*):(\\d*): (\\S*: No such file or directory)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      final String file = matcher.group(1);
      int lineNumber = 0;
      try {
        lineNumber = Integer.parseInt(matcher.group(2));
      } catch (NumberFormatException ignored) {
      }
      final String description = matcher.group(3);
      return new MakefileMessage(description, PrintMessage.PrintType.WARNING, file, lineNumber);
    }
  };
  private static final ParserPattern PATTERN_MAKEFILE_WARNING = new ParserPattern("(.*[Mm]akefile):(\\d*): warning: (.*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      final String file = matcher.group(1);
      int lineNumber = 0;
      try {
        lineNumber = Integer.parseInt(matcher.group(2));
      } catch (NumberFormatException ignored) {
      }
      final String description = matcher.group(3);
      return new MakefileMessage(description, PrintMessage.PrintType.WARNING, file, lineNumber);
    }
  };

  private static final ParserPattern PATTERN_TARGET_NOT_REMADE = new ParserPattern(".*make.*: (Target (.*) not remade because of errors.)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      if (!(m.find() && m.group(0).length() == line.length())) return null;
      return new PrintMessage(m.group(1), PrintMessage.PrintType.ERROR);
    }
  };
  private static final ParserPattern PATTERN_TARGET_NO_RULE = new ParserPattern("No rule to make target `(.*)'\\.") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      if (!(m.find() && m.group(0).length() == line.length())) return null;
//      return new PrintMessage(m.group(1), PrintMessage.PrintType.ERROR);
      return new RecipeTargetMessage(line, PrintMessage.PrintType.ERROR, m.group(1));
    }
  };

  private static final ParserPattern PATTERN_TARGET_RECIPE_ERROR = new ParserPattern(".*make.*: \\*\\*\\* \\[(\\S+)\\] (.*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      if (!(m.find() && m.group(0).length() == line.length())) return null;
      return new RecipeTargetMessage(m.group(2), PrintMessage.PrintType.ERROR, m.group(1));
    }
  };


  private static final ParserPattern PATTERN_IGNORED_ERROR = new ParserPattern(".*make.*:(.*Error.*\\(ignored\\))") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      final String description = matcher.group(1);
      return new PrintMessage(description, PrintMessage.PrintType.INFO);
    }
  };
  private static final ParserPattern PATTERN_MAKE_ERROR = new ParserPattern(".*make.*: \\*\\*\\* (.*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      if (!(m.find() && m.group(0).length() == line.length())) return null;
      return new PrintMessage(m.group(1), PrintMessage.PrintType.ERROR);
    }
  };

  private static final ParserPattern PATTERN_COMMAND_NOT_FOUND = new ParserPattern(".*command not found.*") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      if (!(m.find() && m.group(0).length() == line.length())) return null;
      return new PrintMessage(m.group(0), PrintMessage.PrintType.ERROR);
    }
  };
  private static final ParserPattern PATTERN_BASIC_ERROR = new ParserPattern("Error:\\s*(.*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher m = getMatcher(line);
      return m.matches() ? new PrintMessage(line, PrintMessage.PrintType.ERROR) : null;
    }
  };

  private static final ParserPattern PATTERN_RECIPE_WARNING = new ParserPattern(".*make.*\\[.*\\] (Error [\\-]?\\d*.*)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      return new PrintMessage(matcher.group(1), PrintMessage.PrintType.WARNING);
    }
  };
  private static final ParserPattern PATTERN_CIRCULAR_DEP_DROPPED = new ParserPattern(".*make.*: (Circular .* dependency dropped.)") {
    @Override
    public ParsedMessage tryParse(@NotNull final String line) {
      final Matcher matcher = getMatcher(line);
      if (!(matcher.find() && matcher.group(0).length() == line.length())) return null;
      return new PrintMessage(matcher.group(1), PrintMessage.PrintType.WARNING);
    }
  };

  public Parser createParser() {
    final List<ParserPattern> patterns = new ArrayList<ParserPattern>(15);

    patterns.add(PATTERN_MAKING_SOMETHING);
    patterns.add(PATTERN_DIRECTORY_ENTER);
    patterns.add(PATTERN_DIRECTORY_LEAVE);

//    patterns.add(PATTERN_MAKEFILE_ERROR);
//    patterns.add(PATTERN_MAKEFILE_WARNING);
//    patterns.add(PATTERN_MAKEFILE_NO_SUCH_FILE_WARNING);

//    patterns.add(PATTERN_TARGET_NO_RULE);
//    patterns.add(PATTERN_TARGET_NOT_REMADE);
//    patterns.add(PATTERN_TARGET_RECIPE_ERROR);

//    patterns.add(PATTERN_IGNORED_ERROR);
//    patterns.add(PATTERN_MAKE_ERROR);
//    patterns.add(PATTERN_COMMAND_NOT_FOUND);

//    patterns.add(PATTERN_BASIC_ERROR);
//    patterns.add(PATTERN_RECIPE_WARNING);
//    patterns.add(PATTERN_CIRCULAR_DEP_DROPPED);

    return new MakeParser(patterns);
  }

}
