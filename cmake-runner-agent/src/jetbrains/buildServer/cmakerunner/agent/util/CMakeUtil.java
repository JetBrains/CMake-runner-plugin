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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class CMakeUtil {

  private static final Pattern GENERATOR_PATTERN = Pattern.compile("\\s*(.*)\\s*(=.*)?");

  public static boolean isGeneratorSupported(final BuildAgentConfiguration agentConfiguration) {
    final Collection<String> availableGenerators = getAvailableGenerators(agentConfiguration.getTempDirectory());
    final String genName = agentConfiguration.getConfigurationParameters().get(CMakeRunnerConstants.UI_MAKEFILE_GENERATOR);
    final CMakeGenerator generator = CMakeGenerator.valueOf(genName);
    if (generator == CMakeGenerator.DEFAULT) return !availableGenerators.isEmpty();
    return availableGenerators.contains(generator.getNormalName());
  }

  private static Collection<String> getAvailableGenerators(final File tmpDir) {
    final String[] cmdline = {"cmake", "--help"};
    final Collection<String> generators = new HashSet<String>();
    try {
      final Process process = Runtime.getRuntime().exec(cmdline, new String[0], tmpDir);
      final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String s;
      do {
        s = in.readLine();
      } while (!s.startsWith("The following generators are available on this platform:"));

      do {
        final Matcher m = GENERATOR_PATTERN.matcher(s);
        if (!m.find()) continue;
        generators.add(m.group(1));
      } while ((s = in.readLine()) != null);

      process.waitFor();
    } catch (IOException ignored) {
    } catch (InterruptedException ignored) {
    }
    return generators;
  }
}
