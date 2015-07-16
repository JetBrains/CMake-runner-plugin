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

package jetbrains.buildServer.cmakerunner.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.cmakerunner.MakeRunnerConstants;
import jetbrains.buildServer.cmakerunner.agent.util.OSUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public class MakeRunnerCLBSFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
  private final static Logger LOG = Logger.getInstance(MakeRunnerCLBSFactory.class.getName());

  @NotNull
  public CommandLineBuildService createService() {
    return new MakeTasksBuildService();
  }

  @NotNull
  public AgentBuildRunnerInfo getBuildRunnerInfo() {
    return this;
  }

  @NotNull
  public String getType() {
    return MakeRunnerConstants.TYPE;
  }

  public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
    if (!OSUtil.isOSSupported()) {
      LOG.info(MakeRunnerConstants.DISPLAY_NAME + " runner disabled: OS '" + System.getProperty("os.name") + "' is not supported");
      return false;
    }
    if (!OSUtil.isCLIExist(agentConfiguration.getBuildParameters().getEnvironmentVariables())) {
      LOG.info(MakeRunnerConstants.DISPLAY_NAME + " runner disabled: shell not found");
      return false;
    }
    return true;
  }
}
