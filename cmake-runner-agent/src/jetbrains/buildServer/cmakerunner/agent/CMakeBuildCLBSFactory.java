

package jetbrains.buildServer.cmakerunner.agent;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.cmakerunner.CMakeBuildConstants;
import jetbrains.buildServer.cmakerunner.agent.util.CMakeUtil;
import jetbrains.buildServer.cmakerunner.agent.util.OSUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public class CMakeBuildCLBSFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
  @NotNull
  public CommandLineBuildService createService() {
    return new CMakeBuildBS();
  }

  @NotNull
  public AgentBuildRunnerInfo getBuildRunnerInfo() {
    return this;
  }

  @NotNull
  public String getType() {
    return CMakeBuildConstants.TYPE;
  }

  public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
    return OSUtil.isOSSupported() && CMakeUtil.isCMakeExist(agentConfiguration);
  }
}