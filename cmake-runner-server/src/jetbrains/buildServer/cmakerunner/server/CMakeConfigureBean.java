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

package jetbrains.buildServer.cmakerunner.server;


import jetbrains.buildServer.cmakerunner.CMakeBuildType;
import jetbrains.buildServer.cmakerunner.CMakeGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static jetbrains.buildServer.cmakerunner.CMakeConfigureConstants.*;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeConfigureBean {

  // Keys
  @NotNull
  public String getAdditionalParamsKey() {
    return UI_ADDITIONAL_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return UI_REDIRECT_STDERR;
  }

  @NotNull
  public String getGeneratorKey() {
    return UI_MAKEFILE_GENERATOR;
  }

  @NotNull
  public String getBuildTypeKey() {
    return UI_CMAKE_BUILD_TYPE;
  }

  @NotNull
  public String getDeveloperWarningsKey() {
    return UI_DEVELOPER_WARNINGS;
  }

  @NotNull
  public String getWarnUninitializedKey() {
    return UI_WARN_UNINITIALIZED;
  }

  @NotNull
  public String getWarnUnusedVarsKey() {
    return UI_WARN_UNUSED_VARS;
  }

  @NotNull
  public String getNoWarnUnusedCliKey() {
    return UI_NO_WARN_UNUSED_CLI;
  }

  @NotNull
  public String getPrintTraceKey() {
    return UI_PRINT_TRACE;
  }

  @NotNull
  public String getDebugModeKey() {
    return UI_DEBUG_MODE;
  }

  @NotNull
  public String getAdditionalDebugOptions() {
    return UI_ADDITIONAL_DEBUG_OPTIONS;
  }

  @NotNull
  public String getCmakeCommandKey() {
    return UI_CMAKE_COMMAND;
  }

  @NotNull
  public Collection<CMakeBuildType> getBuildTypesSet() {
    return Arrays.asList(CMakeBuildType.values());
  }

  @NotNull
  public String getSourcePathKey() {
    return UI_SOURCE_PATH;
  }

  @NotNull
  public ArrayList<String> getGeneratorsNames() {
    final ArrayList<String> ret = new ArrayList<String>();
    for (final String s : CMakeGenerator.KNOWN_GENERATORS) {
      ret.add("\"" + s + '\"');
    }
    return ret;
  }
}
