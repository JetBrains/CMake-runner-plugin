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


import org.jetbrains.annotations.NotNull;

import static jetbrains.buildServer.cmakerunner.CMakeBuildConstants.*;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeBuildBean {

  @NotNull
  public String getNativeToolParamsKey() {
    return UI_NATIVE_TOOL_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return UI_REDIRECT_STDERR;
  }

  @NotNull
  public String getBuildTargetKey() {
    return UI_BUILD_TARGET;
  }

  @NotNull
  public String getCmakeCommandKey() {
    return UI_CMAKE_COMMAND;
  }

  @NotNull
  public String getBuildPathKey() {
    return UI_BUILD_PATH;
  }

  @NotNull
  public String getCleanBeforeBuildKey() {
    return UI_BUILD_CLEAN_FIRST;
  }

  @NotNull
  public String getBuildConfigurationKey() {
    return UI_BUILD_CONFIGURATION;
  }

}
