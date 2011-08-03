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

package jetbrains.buildServer.cmakerunner.agent.output;

import jetbrains.buildServer.agent.runner.ProcessListenerAdapter;
import jetbrains.buildServer.cmakerunner.regexparser.Logger;
import jetbrains.buildServer.cmakerunner.regexparser.ParserLoader;
import jetbrains.buildServer.cmakerunner.regexparser.ParserManager;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Vladislav.Rassokhin
 */
public class CMakeConfigureOutputListener extends ProcessListenerAdapter {
  private final RegexParser myRegexParser;
  private final ParserManager myContext;

  public CMakeConfigureOutputListener(@NotNull final Logger logger) {
    myContext = new ParserManager(logger);
    //TODO: extract "/cmake-parser.xml" as variable?
    RegexParser regexParser = ParserLoader.loadParser("/cmake-parser.xml", this.getClass());
    if (regexParser == null) {
      regexParser = new RegexParser("default empty parser", "default empty parser");
    }
    myRegexParser = regexParser;
  }

  @Override
  public void onStandardOutput(@NotNull final String text) {
    if (!myRegexParser.processLine(text, myContext)) {
      myContext.getLogger().message(text);
    }
  }

  @Override
  public void onErrorOutput(@NotNull final String text) {
    if (!myRegexParser.processLine(text, myContext)) {
      myContext.getLogger().error(text);
    }
  }

  @Override
  public void processStarted(@NotNull final String programCommandLine, @NotNull final File workingDirectory) {
  }

  @Override
  public void processFinished(final int exitCode) {
  }

}
