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

package jetbrains.buildServer.cmakerunner.server;


import jetbrains.buildServer.cmakerunner.CMakeGenerator;
import jetbrains.buildServer.cmakerunner.CMakeRunnerConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeRunnerBean {

  // Keys
  @NotNull
  public String getAdditionalParamsKey() {
    return CMakeRunnerConstants.UI_ADDITIONAL_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return CMakeRunnerConstants.UI_REDIRECT_STDERR;
  }

  @NotNull
  public Map<String, CMakeGenerator> getGeneratorsMap() {
    return CMakeGenerator.NAME_TO_GENERATOR_MAP;
  }

  @NotNull
  public String getMakefileGeneratorKey() {
    return CMakeRunnerConstants.UI_MAKEFILE_GENERATOR;
  }

  @NotNull
  public String getDeveloperWarningsKey() {
    return CMakeRunnerConstants.UI_DEVELOPER_WARNINGS;
  }

  @NotNull
  public String getWarnUninitializedKey() {
    return CMakeRunnerConstants.UI_WARN_UNINITIALIZED;
  }

  @NotNull
  public String getWarnUnusedVarsKey() {
    return CMakeRunnerConstants.UI_WARN_UNUSED_VARS;
  }

  @NotNull
  public String getNoWarnUnusedCliKey() {
    return CMakeRunnerConstants.UI_NO_WARN_UNUSED_CLI;
  }

  @NotNull
  public String getPrintTraceKey() {
    return CMakeRunnerConstants.UI_PRINT_TRACE;
  }

  @NotNull
  public String getDebugModeKey() {
    return CMakeRunnerConstants.UI_DEBUG_MODE;
  }

  @NotNull
  public String getAdditionalDebugOptions() {
    return CMakeRunnerConstants.UI_ADDITIONAL_DEBUG_OPTIONS;
  }

  @NotNull
  public String getCmakeCommandKey() {
    return CMakeRunnerConstants.UI_CMAKE_COMMAND;
  }
}
