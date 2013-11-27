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
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

/**
 * abbreviations:
 * CLI = Command Line Interpreter
 *
 * @author Vladislav.Rassokhin
 */
public class OSUtil {

  /**
   * Returns full path to OS command line scripts executor (CLI).
   * (like C:\Windows\system32\cmd.exe or /bin/bash)
   *
   * @param environment current system environment
   * @return see above
   * @throws jetbrains.buildServer.RunBuildException
   *          if OS not supported or cannot determine shell path
   */
  @NotNull
  public static String getCLIFullPath(@NotNull final Map<String, String> environment) throws RunBuildException {
    String scriptsRunner;
    if (SystemInfo.isWindows) {
      scriptsRunner = environment.get(SCRIPT_RUNNER_EXE_WIN_KEY);
      if (scriptsRunner == null) for (final String s : environment.keySet()) {
        if (StringUtil.areEqual(s.toUpperCase(), SCRIPT_RUNNER_EXE_WIN_KEY.toUpperCase())) {
          scriptsRunner = environment.get(s);
          break;
        }
      }
      if (scriptsRunner == null) {
        throw new RunBuildException("cannot locate commands launcher (env variable " + SCRIPT_RUNNER_EXE_WIN_KEY + " missing)");
      }
    } else if (SystemInfo.isUnix) {
      scriptsRunner = environment.get(SCRIPT_RUNNER_EXE_UNIX_KEY);
      if (scriptsRunner == null) {
        throw new RunBuildException("cannot locate commands launcher (env variable " + SCRIPT_RUNNER_EXE_UNIX_KEY + " missing)");
      }
    } else {
      throw new RunBuildException(OS_NOT_SUPPORTED);
    }
    return scriptsRunner;
  }

  /**
   * Returns true if current OS supported by this plugin.
   *
   * @return see above
   */
  public static boolean isOSSupported() {
    return SystemInfo.isUnix || SystemInfo.isWindows;
  }

  /**
   * Checks for path to CLI set and file exist.
   *
   * @param environment current system environment
   * @return true if path to CLI in environment is correct
   */
  public static boolean isCLIExist(@NotNull final Map<String, String> environment) {
    try {
      return new File(getCLIFullPath(environment)).exists();
    } catch (RunBuildException e) {
      return false;
    }
  }

  @NotNull
  private static final String SCRIPT_RUNNER_EXE_WIN_KEY = "ComSpec";
  @NotNull
  private static final String SCRIPT_RUNNER_EXE_UNIX_KEY = "SHELL";
  @NotNull
  private static final String OS_NOT_SUPPORTED = "OS not supported";

  @NotNull
  public static String getEnvPATHVariableName() {
    if (SystemInfo.isUnix) {
      return "PATH";
    } else if (SystemInfo.isWindows) {
      return "Path";
    } else {
      throw new RuntimeException(OS_NOT_SUPPORTED);
    }
  }

  @Nullable
  public static String getEnvironmentVariableValue(@NotNull final Map<String, String> environment, @NotNull final String name) {
    String path = environment.get(name);
    if (path != null)
      return path;

    // Using ignore case search
    for (final String s : environment.keySet()) {
      if (StringUtil.areEqual(s.toUpperCase(), name.toUpperCase())) {
        path = environment.get(s);
        break;
      }
    }

    return path;
  }

  @Nullable
  public static String getPathEnvVariable(@NotNull final Map<String, String> environment) {
    return getEnvironmentVariableValue(environment, getEnvPATHVariableName());
  }
}
