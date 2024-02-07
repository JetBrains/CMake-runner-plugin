

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