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
import jetbrains.buildServer.cmakerunner.agent.util.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Vladislav.Rassokhin
 */
public class OutputListener extends ProcessListenerAdapter {
  private final Logger myLogger;

  public OutputListener(@NotNull final Logger logger) {
    myLogger = logger;
  }

  @Override
  public void onStandardOutput(@NotNull final String text) {
    myLogger.message(text);
  }

  @Override
  public void onErrorOutput(@NotNull final String text) {
    myLogger.error(text);
  }

  @Override
  public void processStarted(@NotNull final String programCommandLine, @NotNull final File workingDirectory) {
  }

  @Override
  public void processFinished(final int exitCode) {
  }
}
