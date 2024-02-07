

package jetbrains.buildServer.cmakerunner.server;


import org.jetbrains.annotations.NotNull;

import static jetbrains.buildServer.cmakerunner.CMakeBuildConstants.*;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeBuildBean {

  @NotNull
  public String getNativeToolParamsKey() {
    return UI_NATIVE_TOOL_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return UI_REDIRECT_STDERR;
  }

  @NotNull
  public String getBuildTargetKey() {
    return UI_BUILD_TARGET;
  }

  @NotNull
  public String getCmakeCommandKey() {
    return UI_CMAKE_COMMAND;
  }

  @NotNull
  public String getBuildPathKey() {
    return UI_BUILD_PATH;
  }

  @NotNull
  public String getCleanBeforeBuildKey() {
    return UI_BUILD_CLEAN_FIRST;
  }

  @NotNull
  public String getBuildConfigurationKey() {
    return UI_BUILD_CONFIGURATION;
  }

}