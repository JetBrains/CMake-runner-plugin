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


import jetbrains.buildServer.cmakerunner.CMakeRunnerConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeRunnerBean {

  // Keys
  @NotNull
  public String getAdditionalCmdParamsKey() {
    return CMakeRunnerConstants.RUNNER_MAKE_ADDITIONAL_CMD_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return CMakeRunnerConstants.RUNNER_REDIRECT_STDERR;
  }

}
