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

/**
 * @author : Vladislav.Rassokhin
 */
public interface CMakeRunnerConstants {
  String RUNNER_TYPE = "cmake-runner";

  String RUNNER_DESCRIPTION = "Runner for use CMake";
  String RUNNER_DISPLAY_NAME = "CMake";

  String RUNNER_MAKE_ADDITIONAL_CMD_PARAMS = "ui.cmakeRunner.additional.cmd.params";
  String RUNNER_REDIRECT_STDERR = "ui.cmakeRunner.redirect.stderr";

}
