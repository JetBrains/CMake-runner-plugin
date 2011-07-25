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
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public interface CMakeRunnerConstants {
  @NotNull
  @NonNls
  String RUNNER_TYPE = "cmake-runner";

  @NotNull
  @NonNls
  String RUNNER_DESCRIPTION = "Runner for use CMake";
  @NotNull
  @NonNls
  String RUNNER_DISPLAY_NAME = "CMake";

  @NotNull
  @NonNls
  String UI_ADDITIONAL_PARAMS = "ui.cmakeRunner.additional.cmd.params";
  @NotNull
  @NonNls
  String UI_REDIRECT_STDERR = "ui.cmakeRunner.redirect.stderr";
  @NotNull
  @NonNls
  String UI_MAKEFILE_GENERATOR = "ui.cmakeRunner.makefile.generator";
  @NotNull
  @NonNls
  String UI_DEVELOPER_WARNINGS = "ui.cmakeRunner.developer.warnings";

  @NotNull
  @NonNls
  String UI_WARN_UNINITIALIZED = "ui.cmakeRunner.warn.uninitialized";
  @NotNull
  @NonNls
  String UI_WARN_UNUSED_VARS = "ui.cmakeRunner.warn.unused.vars";
  @NotNull
  @NonNls
  String UI_NO_WARN_UNUSED_CLI = "ui.cmakeRunner.no.warn.unused.cli";

  @NotNull
  @NonNls
  String UI_PRINT_TRACE = "ui.cmakeRunner.print.trace";
  @NotNull
  @NonNls
  String UI_DEBUG_MODE = "ui.cmakeRunner.debug.mode";

  @NotNull
  @NonNls
  String UI_ADDITIONAL_DEBUG_OPTIONS = "ui.cmakeRunner.additionalDebugOptions";

  @NotNull
  @NonNls
  String UI_CMAKE_COMMAND = "ui.cmakeRunner.cmake.command";

//  String RUNNER_MAKE_ADDITIONAL_CMD_PARAMS = "";
//  String RUNNER_REDIRECT_STDERR = "";

  @NotNull
  @NonNls
  String RUNNER_MAKEFILE_GENERATOR = "-G";
  @NotNull
  @NonNls
  String RUNNER_DEVELOPER_WARNINGS_ON = "-Wdev";
  @NotNull
  @NonNls
  String RUNNER_DEVELOPER_WARNINGS_OFF = "-Wno-dev";

  @NotNull
  @NonNls
  String RUNNER_WARN_UNINITIALIZED = "--warn-uninitialized";
  @NotNull
  @NonNls
  String RUNNER_WARN_UNUSED_VARS = "--warn-unused-vars";
  @NotNull
  @NonNls
  String RUNNER_NO_WARN_UNUSED_CLI = "--no-warn-unused-cli";

  @NotNull
  @NonNls
  String RUNNER_PRINT_TRACE = "--trace";
  @NotNull
  @NonNls
  String RUNNER_DEBUG_MODE = "--debug-output";


}
