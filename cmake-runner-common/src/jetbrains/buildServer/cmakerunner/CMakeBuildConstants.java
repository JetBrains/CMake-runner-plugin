

package jetbrains.buildServer.cmakerunner;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public interface CMakeBuildConstants {
  @NotNull
  @NonNls
  String TYPE = "jetbrains-cmake-build";

  @NotNull
  @NonNls
  String DESCRIPTION = "Using CMake for build project";
  @NotNull
  @NonNls
  String DISPLAY_NAME = "CMake build";

  @NotNull
  @NonNls
  String UI_PREFIX = "ui-" + TYPE + "-";

  @NotNull
  @NonNls
  String UI_NATIVE_TOOL_PARAMS = UI_PREFIX + "native-tool-params";
  @NotNull
  @NonNls
  String UI_REDIRECT_STDERR = UI_PREFIX + "redirect-stderr";
  @NotNull
  @NonNls
  String UI_BUILD_TARGET = UI_PREFIX + "build-target";


  @NotNull
  @NonNls
  String UI_CMAKE_COMMAND = UI_PREFIX + "cmake-command";

  @NotNull
  @NonNls
  String UI_BUILD_CLEAN_FIRST = UI_PREFIX + "clean-before-build";

  @NotNull
  @NonNls
  String UI_BUILD_CONFIGURATION = UI_PREFIX + "cmake-build-configuration";

  @NotNull
  @NonNls
  String UI_BUILD_PATH = UI_PREFIX + "source-path";

}