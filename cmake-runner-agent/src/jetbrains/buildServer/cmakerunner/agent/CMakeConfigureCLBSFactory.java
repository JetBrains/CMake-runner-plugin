

package jetbrains.buildServer.cmakerunner.agent;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.cmakerunner.CMakeConfigureConstants;
import jetbrains.buildServer.cmakerunner.agent.util.CMakeUtil;
import jetbrains.buildServer.cmakerunner.agent.util.OSUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public class CMakeConfigureCLBSFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
  @NotNull
  public CommandLineBuildService createService() {
    return new CMakeConfigureBuildService();
  }

  @NotNull
  public AgentBuildRunnerInfo getBuildRunnerInfo() {
    return this;
  }

  @NotNull
  public String getType() {
    return CMakeConfigureConstants.TYPE;
  }

  public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
    return OSUtil.isOSSupported() && CMakeUtil.isAgentSupported(agentConfiguration);
  }
}