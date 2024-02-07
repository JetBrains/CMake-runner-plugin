

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildSettings;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.BuildCommandLineProcessor;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generates OS specific launcher for ProgramCommandLine.
 * Redirects StdErr into StdOut.
 */
public class OutputRedirectProcessor implements BuildCommandLineProcessor {
  public static final String ENABLE_STDERR_REDIRECT_CONFIG_PARAMETER = "teamcity.command.line.redirect.stderr";
  @NotNull
  private static final String RUNNER_SCRIPT_UNIX = " \"$@\" 2>&1";

  /**
   * Generates OS specific launcher for ProgramCommandLine.
   * Redirects StdErr into StdOut.
   *
   * @param pcl original ProgramCommandLine instance
   * @return see above
   */
  @NotNull
  public static ProgramCommandLine wrap(@NotNull final AgentRunningBuild build, @NotNull final ProgramCommandLine pcl) throws RunBuildException {
    final Map<String, String> environment = pcl.getEnvironment();

    final String shell;
    try {
      shell = OSUtil.getCLIFullPath(environment);
    } catch (final RunBuildException e) {
      // Unsupported OS. Warn and do nothing.
      build.getBuildLogger().warning("Cannot wrap execution: " + e.getMessage());
      return pcl;
    }

    final List<String> arguments = new ArrayList<String>();
    if (SystemInfo.isUnix) {  // sh -c "make $@ 2>&1" -- clean all
      arguments.add("-c");
      arguments.add(pcl.getExecutablePath() + RUNNER_SCRIPT_UNIX);
      arguments.add("--");
      arguments.addAll(pcl.getArguments());
      return new SimpleProgramCommandLine(environment, pcl.getWorkingDirectory(), shell, arguments);
    } else if (SystemInfo.isWindows) {
      final File script = createScriptForWindows(pcl, build);
      arguments.add("/C");
      arguments.add(script.getAbsolutePath());
      return new SimpleProgramCommandLine(environment, pcl.getWorkingDirectory(), shell, arguments);
    } else {
      // Unsupported OS. Warn and do nothing.
      build.getBuildLogger().warning("Cannot wrap execution: Unsupported OS.");
      return pcl;
    }
  }

  @NotNull
  private static File createScriptForWindows(@NotNull final ProgramCommandLine pcl, @NotNull final AgentBuildSettings settings) throws RunBuildException {
    final File script;
    try {
      script = File.createTempFile("wrap", ".cmd", settings.getAgentTempDirectory());
      final StringBuilder content = new StringBuilder();
      content.append("cd ").append(pcl.getWorkingDirectory()).append("\r\n");
      content.append(createOriginalCommandLine(pcl));
      content.append(" ").append("2>&1");
      content.append("\r\n");
      content.append("exit %errorlevel%");
      FileUtil.writeFileAndReportErrors(script, content.toString());
    } catch (final IOException e) {
      throw new RunBuildException("Failed to create temp file, error: " + e.toString());
    }
    return script;
  }

  @NotNull
  private static String createOriginalCommandLine(@NotNull final ProgramCommandLine pcl) throws RunBuildException {
    final StringBuilder sb = new StringBuilder();
    sb.append(pcl.getExecutablePath());
    for (final String arg : pcl.getArguments()) {
      sb.append(" ");
      final boolean hasSpaces = arg.indexOf(' ') != -1;
      if (hasSpaces) {
        sb.append("\"");
      }
      sb.append(StringUtil.escapeQuotes(arg));
      if (hasSpaces) {
        sb.append("\"");
      }
    }
    return sb.toString();
  }

  @NotNull
  public ProgramCommandLine process(@NotNull final BuildRunnerContext context, @NotNull final ProgramCommandLine pcl) throws RunBuildException {
    final AgentRunningBuild build = context.getBuild();
    if (StringUtil.isTrue(context.getConfigParameters().get(ENABLE_STDERR_REDIRECT_CONFIG_PARAMETER))) {
      return wrap(build, pcl);
    } else {
      return pcl;
    }
  }
}