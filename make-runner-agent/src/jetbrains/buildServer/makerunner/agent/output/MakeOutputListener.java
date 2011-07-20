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

import jetbrains.buildServer.agent.runner.ProcessListenerAdapter;
import jetbrains.buildServer.makerunner.agent.util.Logger;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeOutputListener extends ProcessListenerAdapter {
  @NotNull
  private final AtomicReference<File> myWorkingDirectory;
  @NotNull
  private final MakeParserManager myContext;
  @NotNull
  private final RegexParser myRegexParser;

  public MakeOutputListener(@NotNull final Logger logger, @NotNull final AtomicReference<List<String>> makeTasks) {
    myWorkingDirectory = new AtomicReference<File>();
    myContext = new MakeParserManager(logger, myWorkingDirectory, makeTasks);
    myRegexParser = loadParser(logger);
  }

  @Override
  public void processStarted(@NotNull final String programCommandLine, @NotNull final File workingDirectory) {
    myWorkingDirectory.set(workingDirectory);
  }

  @Override
  public void processFinished(final int exitCode) {
    myContext.finishAllTargets();
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


  @NotNull
  private RegexParser loadParser(@NotNull final Logger logger) {
    final InputStream parserConfigStream = this.getClass().getResourceAsStream("/make-parser.xml");
    RegexParser parser = null;
    if (parserConfigStream == null) {
      logger.warning("Parser configuration not found");
    } else {
      try {
        parser = RegexParser.deserialize(parserConfigStream);
      } catch (IOException e) {
        logger.warning("Parser read fail: " + StringUtil.stackTrace(e));
      }
    }
    if (parser == null) {
      // Cannot deserialize parser config
      // Using default empty parser
      logger.warning("Using default empty parser");
      parser = new RegexParser("default empty parser", "default empty parser");
    }
    return parser;
  }

  public RegexParser getParser() {
    return myRegexParser;
  }
}
