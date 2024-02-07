

package jetbrains.buildServer.cmakerunner.server;


import jetbrains.buildServer.cmakerunner.CMakeBuildType;
import jetbrains.buildServer.cmakerunner.CMakeGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static jetbrains.buildServer.cmakerunner.CMakeConfigureConstants.*;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"SameReturnValue"})
public class CMakeConfigureBean {

  // Keys
  @NotNull
  public String getAdditionalParamsKey() {
    return UI_ADDITIONAL_PARAMS;
  }

  @NotNull
  public String getRedirectStderrKey() {
    return UI_REDIRECT_STDERR;
  }

  @NotNull
  public String getGeneratorKey() {
    return UI_MAKEFILE_GENERATOR;
  }

  @NotNull
  public String getBuildTypeKey() {
    return UI_CMAKE_BUILD_TYPE;
  }

  @NotNull
  public String getDeveloperWarningsKey() {
    return UI_DEVELOPER_WARNINGS;
  }

  @NotNull
  public String getWarnUninitializedKey() {
    return UI_WARN_UNINITIALIZED;
  }

  @NotNull
  public String getWarnUnusedVarsKey() {
    return UI_WARN_UNUSED_VARS;
  }

  @NotNull
  public String getNoWarnUnusedCliKey() {
    return UI_NO_WARN_UNUSED_CLI;
  }

  @NotNull
  public String getPrintTraceKey() {
    return UI_PRINT_TRACE;
  }

  @NotNull
  public String getDebugModeKey() {
    return UI_DEBUG_MODE;
  }

  @NotNull
  public String getAdditionalDebugOptions() {
    return UI_ADDITIONAL_DEBUG_OPTIONS;
  }

  @NotNull
  public String getCmakeCommandKey() {
    return UI_CMAKE_COMMAND;
  }

  @NotNull
  public Collection<CMakeBuildType> getBuildTypesSet() {
    return Arrays.asList(CMakeBuildType.values());
  }

  @NotNull
  public String getSourcePathKey() {
    return UI_SOURCE_PATH;
  }

  @NotNull
  public ArrayList<String> getGeneratorsNames() {
    final ArrayList<String> ret = new ArrayList<String>();
    for (final String s : CMakeGenerator.KNOWN_GENERATORS) {
      ret.add("\"" + s + '\"');
    }
    return ret;
  }
}