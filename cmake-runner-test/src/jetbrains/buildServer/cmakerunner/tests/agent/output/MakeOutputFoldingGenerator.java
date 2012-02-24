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

package jetbrains.buildServer.cmakerunner.tests.agent.output;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeOutputFoldingGenerator {

  public static String generateStartingTargetMessage(final String target, final String dirName) {
    return String.format("Making %s in %s", target, dirName);
  }

  public static String generateEnterMessage(final String dirName, final int level) {
    if (level == -1) {
      return String.format("make: Entering directory `%s'", dirName);
    }
    return String.format("make[%d]: Entering directory `%s'", level, dirName);
  }

  public static String generateLeaveMessage(final String dirName, final int level) {
    if (level == -1) {
      return String.format("make: Leaving directory `%s'", dirName);
    }
    return String.format("make[%d]: Leaving directory `%s'", level, dirName);
  }
}
