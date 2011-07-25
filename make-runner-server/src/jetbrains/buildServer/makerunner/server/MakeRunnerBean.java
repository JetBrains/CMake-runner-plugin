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

package jetbrains.buildServer.makerunner.server;

import jetbrains.buildServer.makerunner.MakeRunnerConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class MakeRunnerBean {

  // Keys
  @NotNull
  public String getTasksKey() {
    return MakeRunnerConstants.RUNNER_MAKE_TASKS;
  }

  @NotNull
  public String getAdditionalCmdParamsKey() {
    return MakeRunnerConstants.RUNNER_MAKE_ADDITIONAL_CMD_PARAMS;
  }

  @NotNull
  public String getKeepGoingKey() {
    return MakeRunnerConstants.RUNNER_MAKE_KEEP_GOING;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return MakeRunnerConstants.RUNNER_REDIRECT_STDERR;
  }

  public String getMakeProgramPathKey() {
    return MakeRunnerConstants.RUNNER_MAKE_PROGRAM_PATH;
  }
}
