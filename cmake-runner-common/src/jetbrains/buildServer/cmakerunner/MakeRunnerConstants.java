
package jetbrains.buildServer.cmakerunner;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Vladislav.Rassokhin
 */
public interface MakeRunnerConstants {
  @NotNull
  @NonNls
  String TYPE = "jetbrains-cmake-gnumake";

  @NotNull
  @NonNls
  String DESCRIPTION = "Runner for executing Make tasks";
  @NotNull
  @NonNls
  String DISPLAY_NAME = "GNU Make";

  @NotNull
  @NonNls
  String UI_PREFIX = "ui-" + TYPE + "-";

  @NotNull
  @NonNls
  String UI_MAKE_TASKS = UI_PREFIX + "make-tasks";
  @NotNull
  @NonNls
  String UI_MAKE_ADDITIONAL_CMD_PARAMS = UI_PREFIX + "additional-cmd-params";
  @NotNull
  @NonNls
  String UI_MAKE_KEEP_GOING = UI_PREFIX + "keep-going";
  @NotNull
  @NonNls
  String UI_REDIRECT_STDERR = UI_PREFIX + "redirect-stderr";
  @NotNull
  @NonNls
  String UI_MAKE_PROGRAM_PATH = UI_PREFIX + "make-program-path";

  // Make arguments
  @NotNull
  @NonNls
  String MAKE_CMDLINE_OPTIONS_MAKEFILE = "--file";
  @NotNull
  @NonNls
  String MAKE_CMDLINE_OPTIONS_KEEP_GOING = "--keep-going";

  @NotNull
  @NonNls
  String TEAMCITY_MAKE_OUTPUT_PATTERNS_FILE_PROPERTY = "teamcity.make.output.patterns.file";
  @NotNull
  @NonNls
  String TEAMCITY_MAKE_OUTPUT_DEFAULT_PATTERNS_ENABLED_PROPERTY = "teamcity.make.output.default.patterns.enabled";
}