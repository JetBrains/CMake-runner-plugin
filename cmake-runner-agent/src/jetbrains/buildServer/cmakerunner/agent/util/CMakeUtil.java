/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.cmakerunner.agent.util;

import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.cmakerunner.CMakeGenerator;
import jetbrains.buildServer.cmakerunner.CMakeRunnerConstants;
import jetbrains.buildServer.util.StringUtil;

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

  public static boolean isGeneratorSupported(final BuildAgentConfiguration agentConfiguration) {
    final Collection<String> availableGenerators = getAvailableGenerators();
    final String genName = agentConfiguration.getConfigurationParameters().get(CMakeRunnerConstants.UI_MAKEFILE_GENERATOR);
    if (genName == null) return true;
    final CMakeGenerator generator = CMakeGenerator.valueOf(genName);
    if (generator == CMakeGenerator.DEFAULT) {
      return !availableGenerators.isEmpty();
    } else {
      return availableGenerators.contains(generator.getNormalName());
    }
  }

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

  public static boolean isAgentSupported(final BuildAgentConfiguration agentConf) {
    return isGeneratorSupported(agentConf) && isCMakeExist(agentConf);
  }

  public static boolean isCMakeExist(final BuildAgentConfiguration agentConf) {
    final Map<String, String> buildParams = agentConf.getConfigurationParameters();
    final String cmakeCommandStr = buildParams.get(CMakeRunnerConstants.UI_CMAKE_COMMAND);
    File cmakeFile = null;
    if (cmakeCommandStr != null) {
      final File workDirectory = agentConf.getWorkDirectory();
      final File pathFile = new File(cmakeCommandStr);
      if (pathFile.isAbsolute()) {
        cmakeFile = pathFile;
      } else {
        if (cmakeCommandStr.startsWith("..")) {
          String a = cmakeCommandStr.substring(2);
          while (a.startsWith("\\")) a = a.substring(1);
          while (a.startsWith("/")) a = a.substring(1);
          cmakeFile = new File(workDirectory, a);
        } else {
          // Do not check (cmake may be in repository)
        }
      }
    } else {
      cmakeFile = new File(FileUtil.findExecutableByNameInPATH("cmake", agentConf.getBuildParameters().getEnvironmentVariables()));
    }
    return cmakeFile == null || cmakeFile.exists();
  }
}
