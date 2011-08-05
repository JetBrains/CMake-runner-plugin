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

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class PathUtil {
  private static final Pattern winDrivePath = Pattern.compile("^([a-zA-Z]):(.*)$");
  @NotNull
  private static final String SSLASH = "/";
  @NotNull
  private static final String BSLASH = "\\\\";

  @NotNull
  public static String toUnixStylePath(@NotNull final String path) {
    if (path.startsWith("/")) return path;

    if (SystemInfo.isUnix) {
      return path;
    }

    if (SystemInfo.isWindows) {
      final Matcher m = winDrivePath.matcher(path);
      if (m.find()) {
        final StringBuilder newPath = new StringBuilder();
        newPath.append("/cygdrive/").append(m.group(1).toLowerCase());
        newPath.append(m.group(2).replaceAll(BSLASH, SSLASH));
        return newPath.toString();
      }
    }
    return path;
  }
}
