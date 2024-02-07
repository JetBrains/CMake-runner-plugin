

package jetbrains.buildServer.cmakerunner.agent.util;

import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.cmakerunner.CMakeConfigureConstants;
import jetbrains.buildServer.cmakerunner.agent.CMakeConfigureBuildService;
import jetbrains.buildServer.util.FileUtil;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class CMakeUtil {

  public static final Pattern GENERATOR_PATTERN = Pattern.compile("\\s*([^=]+)\\s*(.*)?");

  public static boolean isGeneratorSupported(@NotNull final BuildAgentConfiguration agentConfiguration) {
    final String genName = agentConfiguration.getConfigurationParameters().get(CMakeConfigureConstants.UI_MAKEFILE_GENERATOR);
    if (genName == null) return true;
    final Collection<String> availableGenerators = getAvailableGenerators();
    if (genName.equalsIgnoreCase("default")) {
      return !availableGenerators.isEmpty();
    } else {
      return availableGenerators.contains(genName);
    }
  }

  @NotNull
  public static Collection<String> getAvailableGenerators() {
    final String[] cmdline = {"cmake", "--help"};
    final Collection<String> generators = new HashSet<String>();
    try {

      final Process process = Runtime.getRuntime().exec(cmdline);
      final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String s;
      do {
        s = in.readLine();
      } while (s != null && !s.startsWith("The following generators are available on this platform:"));

      while ((s = in.readLine()) != null) {
        final Matcher m = GENERATOR_PATTERN.matcher(s);
        if (!m.find()) continue;
        final String generator = m.group(1).trim();
        if (!StringUtil.isEmptyOrSpaces(generator)) {
          generators.add(generator);
        }
      }

      process.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return generators;
  }

  public static boolean isAgentSupported(@NotNull final BuildAgentConfiguration agentConf) {
    return isGeneratorSupported(agentConf) && isCMakeExist(agentConf);
  }

  public static boolean isCMakeExist(@NotNull final BuildAgentConfiguration agentConf) {
    final File cmakeFile = getCMakeExecutable(agentConf);
    return cmakeFile == null || cmakeFile.exists();
  }

  @Nullable
  public static File getCMakeExecutable(@NotNull final BuildAgentConfiguration agentConf) {
    File cmakeFile = null;
    final Map<String, String> buildParams = agentConf.getConfigurationParameters();
    final String cmakeCommandStr = buildParams.get(CMakeConfigureConstants.UI_CMAKE_COMMAND);
    if (cmakeCommandStr != null) {
      final File workDirectory = agentConf.getWorkDirectory();
      final File pathFile = new File(cmakeCommandStr);
      if (pathFile.isAbsolute()) {
        cmakeFile = pathFile;
      } else {
        final String rel = FileUtil.normalizeRelativePath(cmakeCommandStr);
        if (rel.startsWith("..")) {
          String a = cmakeCommandStr.substring(2);
          while (a.startsWith("\\")) a = a.substring(1);
          while (a.startsWith("/")) a = a.substring(1);
          cmakeFile = new File(workDirectory, a);
        } else {
          // Do not check (cmake may be in repository)
        }
      }
    } else {
      final String path = FileUtil2.findExecutableByNameInPATH(CMakeConfigureBuildService.DEFAULT_CMAKE_PROGRAM, agentConf.getBuildParameters().getEnvironmentVariables());
      if (path == null) {
        return null;
      }
      cmakeFile = new File(path);
    }
    return cmakeFile;
  }
}