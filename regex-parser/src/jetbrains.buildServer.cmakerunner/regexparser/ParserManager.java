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

package jetbrains.buildServer.cmakerunner.regexparser;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class ParserManager {
  @NotNull
  private final Logger myLogger;

  public ParserManager(@NotNull final Logger myLogger) {
    this.myLogger = myLogger;
  }

  @NotNull
  public Logger getLogger() {
    return myLogger;
  }

  public final void log(@NotNull final String text, @NotNull final Severity severity) {
    switch (severity) {
      case INFO:
        myLogger.message(text);
        break;
      case WARN:
        myLogger.warning(text);
        break;
      case ERROR:
        myLogger.error(text);
        break;
      case SPECIAL:
        if (!this.specialParse(text)) {
          myLogger.message(text);
        }
    }
  }

  protected boolean specialParse(@NotNull final String line) {
    return false;
  }

  public void parsingError(@NotNull final String message) {
    myLogger.warning("Parsing error: " + message);
  }
}
