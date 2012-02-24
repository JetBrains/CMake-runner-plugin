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
public class LoggerAdapter implements Logger {
  public void message(@NotNull final String message) {
  }

  public void error(@NotNull final String message) {
  }

  public void warning(@NotNull final String message) {
  }

  public void blockStart(@NotNull final String name) {
  }

  public void blockFinish(@NotNull final String name) {
  }

}
