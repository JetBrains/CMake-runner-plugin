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

import jetbrains.buildServer.makerunner.agent.util.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class Manager {
  protected final Logger myLogger;

  public Manager(@NotNull final Logger myLogger) {
    this.myLogger = myLogger;
  }

  public final void log(@NotNull final String description, @NotNull final Severity severity) {
    switch (severity) {
      case INFO:
        myLogger.info(description);
        break;
      case WARN:
        myLogger.warning(description);
        break;
      case ERROR:
        myLogger.error(description);
        break;
      case SPECIAL:
        this.specialParse(description);
    }
  }

  protected void specialParse(@NotNull final String line) {
  }

  public void parsingError(@NotNull final String message) {
    myLogger.debug(message);
  }
}
