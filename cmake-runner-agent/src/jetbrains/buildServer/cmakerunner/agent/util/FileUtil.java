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

import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Roman.Chernyatchik
 */
public class FileUtil {

  /**
   * @param path Path to check
   * @return true, if path exists
   */
  public static boolean checkIfExists(@NotNull final String path) {
    return new File(path).exists();
  }


  @Nullable
  public static String findExecutableByNameInPATH(@NotNull final String exeName,
                                                  @NotNull final Map<String, String> environment) {
    final String path = OSUtil.getPathEnvVariable(environment);
    if (path != null) {
      final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

      //tokens - are pathes with system-dependent slashes
      while (st.hasMoreTokens()) {
        final String possible_path = st.nextToken() + "/" + exeName;
        if (FileUtil.checkIfExists(possible_path)) {
          return possible_path;
        }
      }
    }

    if (SystemInfo.isWindows && !exeName.endsWith(".exe") && !exeName.endsWith(".bat")) {
      String deep;
      deep = findExecutableByNameInPATH(exeName + ".exe", environment);
      if (deep == null) {
        deep = findExecutableByNameInPATH(exeName + ".bat", environment);
      }
      return deep;
    }
    return null;
  }
}
