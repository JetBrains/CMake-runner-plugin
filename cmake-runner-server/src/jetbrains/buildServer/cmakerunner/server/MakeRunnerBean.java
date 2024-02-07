

package jetbrains.buildServer.cmakerunner.server;

import jetbrains.buildServer.cmakerunner.MakeRunnerConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class MakeRunnerBean {

  // Keys
  @NotNull
  public String getTasksKey() {
    return MakeRunnerConstants.UI_MAKE_TASKS;
  }

  @NotNull
  public String getAdditionalCmdParamsKey() {
    return MakeRunnerConstants.UI_MAKE_ADDITIONAL_CMD_PARAMS;
  }

  @NotNull
  public String getKeepGoingKey() {
    return MakeRunnerConstants.UI_MAKE_KEEP_GOING;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return MakeRunnerConstants.UI_REDIRECT_STDERR;
  }

  @NotNull
  public String getMakeProgramPathKey() {
    return MakeRunnerConstants.UI_MAKE_PROGRAM_PATH;
  }
}