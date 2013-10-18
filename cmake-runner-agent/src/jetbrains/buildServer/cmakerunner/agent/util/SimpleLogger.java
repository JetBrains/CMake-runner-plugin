/*
 * Copyright 2000-2013 JetBrains s.r.o.
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

package jetbrains.buildServer.cmakerunner.agent.util;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class SimpleLogger extends LoggerAdapter {
  @NotNull
  private final BuildProgressLogger myBuildLogger;

  public SimpleLogger(@NotNull final BuildProgressLogger buildLogger) {
    myBuildLogger = buildLogger;
  }

  @Override
  public void message(@NotNull final String message) {
    myBuildLogger.message(message);
  }

  @Override
  public void error(@NotNull final String message) {
    myBuildLogger.error(message);
  }

  @Override
  public void warning(@NotNull final String message) {
    myBuildLogger.warning(message);
  }

  @Override
  public void blockStart(@NotNull final String name) {
    myBuildLogger.targetStarted(name);
  }

  @Override
  public void blockFinish(@NotNull final String name) {
    myBuildLogger.targetFinished(name);
  }
}
