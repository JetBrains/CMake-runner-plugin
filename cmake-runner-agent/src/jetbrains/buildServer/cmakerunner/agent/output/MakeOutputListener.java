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
import jetbrains.buildServer.cmakerunner.agent.util.PathUtil;
import jetbrains.buildServer.cmakerunner.regexparser.Logger;
import jetbrains.buildServer.cmakerunner.regexparser.ParserLoader;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
    //TODO: extract "/make-parser.xml" as variable?
    RegexParser regexParser = ParserLoader.loadParser("/make-parser.xml", this.getClass());
    if (regexParser == null) {
      regexParser = new RegexParser("default empty parser", "default empty parser");
    }
    myRegexParser = regexParser;
  }

  @Override
  public void processStarted(@NotNull final String programCommandLine, @NotNull final File workingDirectory) {
    myWorkingDirectory.set(new File(PathUtil.toUnixStylePath(workingDirectory.getAbsolutePath())));
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

  public RegexParser getParser() {
    return myRegexParser;
  }
}
