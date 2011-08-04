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

package jetbrains.buildServer.cmakerunner;

import org.jetbrains.annotations.NonNls;

/**
 * @author : Vladislav.Rassokhin
 */
public interface CMakeBuildConstants {
  @NonNls
  String TYPE = "cmake-build";

  @NonNls
  String DESCRIPTION = "Using CMake for build project";
  @NonNls
  String DISPLAY_NAME = "CMake build";

  @NonNls
  String UI_PREFIX = "ui-" + TYPE + "-";

  @NonNls
  String UI_NATIVE_TOOL_PARAMS = UI_PREFIX + "native-tool-params";
  @NonNls
  String UI_REDIRECT_STDERR = UI_PREFIX + "redirect-stderr";
  @NonNls
  String UI_BUILD_TARGET = UI_PREFIX + "build-target";


  @NonNls
  String UI_CMAKE_COMMAND = UI_PREFIX + "cmake-command";

  @NonNls
  String UI_BUILD_CLEAN_FIRST = UI_PREFIX + "clean-before-build";

  @NonNls
  String UI_BUILD_CONFIGURATION = UI_PREFIX + "cmake-build-configuration";

  @NonNls
  String UI_BUILD_PATH = UI_PREFIX + "source-path";

}
