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

package jetbrains.buildServer.cmakerunner.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.cmakerunner.agent.util.OSUtil;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static jetbrains.buildServer.cmakerunner.CMakeRunnerConstants.*;

/**
 * @author : Vladislav.Rassokhin
 */
public class CMakeTasksBuildService extends BuildServiceAdapter {
  // Tmp files set
  @NotNull
  private final Set<File> myFilesToDelete = new HashSet<File>();

  @NotNull
  private static final String DEFAULT_CMAKE_PROGRAM = "cmake";
  private static final String NEW_LINES_PATTERN = "[" + System.getProperty("line.separator", "\n") + "]+";

  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {

    final List<String> arguments = new ArrayList<String>();
    final Map<String, String> runnerParameters = getRunnerParameters(); // all server-ui options
    final Map<String, String> environment = new HashMap<String, String>(getBuildParameters().getEnvironmentVariables());

    // Path to 'cmake'
    String programPath = runnerParameters.get(UI_CMAKE_COMMAND);
    if (programPath == null) {
      programPath = DEFAULT_CMAKE_PROGRAM;
    }

    // Check for program exist
//    if (!FileUtil.checkIfExists(programPath) && FileUtil.findExecutableByNameInPATH(programPath, environment) == null)
//      throw new RunBuildException("Cannot locate `" + programPath + "' executable");


    // CMake options

    final Boolean devWarn = Boolean.valueOf(runnerParameters.get(UI_DEVELOPER_WARNINGS));
    arguments.add(devWarn ? RUNNER_DEVELOPER_WARNINGS_ON : RUNNER_DEVELOPER_WARNINGS_OFF);

    if (devWarn) {
      if (Boolean.valueOf(runnerParameters.get(UI_WARN_UNINITIALIZED))) {
        arguments.add(RUNNER_WARN_UNINITIALIZED);
      }
      if (Boolean.valueOf(runnerParameters.get(UI_WARN_UNUSED_VARS))) {
        arguments.add(RUNNER_WARN_UNUSED_VARS);
      }
      if (Boolean.valueOf(runnerParameters.get(UI_NO_WARN_UNUSED_CLI))) {
        arguments.add(RUNNER_NO_WARN_UNUSED_CLI);
      }
      if (Boolean.valueOf(runnerParameters.get(UI_PRINT_TRACE))) {
        arguments.add(RUNNER_PRINT_TRACE);
      }
      if (Boolean.valueOf(runnerParameters.get(UI_DEBUG_MODE))) {
        arguments.add(RUNNER_DEBUG_MODE);
      }
    }

    // Other arguments
    addCustomArguments(arguments, runnerParameters.get(UI_ADDITIONAL_PARAMS));

    final boolean redirectStdErr = Boolean.valueOf(runnerParameters.get(UI_REDIRECT_STDERR));
    // Result:
    final SimpleProgramCommandLine pcl = new SimpleProgramCommandLine(environment,
            getWorkingDirectory().getAbsolutePath(),
            programPath,
            arguments);
    return redirectStdErr ? OSUtil.makeOSSpecific(pcl) : pcl;
  }

  @Override
  public void afterProcessFinished() {
    // Remove tmp files
    for (final File file : myFilesToDelete) {
      jetbrains.buildServer.util.FileUtil.delete(file);
    }
    myFilesToDelete.clear();
  }

//  @NotNull
//  @Override
//  public List<ProcessListener> getListeners() {
//    return Collections.<ProcessListener>singletonList(new OutputListener(new SimpleMakeLogger(getLogger()), myMakeTasks));
//  }

  private void addCustomArguments(@NotNull final List<String> args, @Nullable final String parameters) {
    if (StringUtil.isEmptyOrSpaces(parameters)) return;
    //noinspection ConstantConditions
    for (final String _line : parameters.split(NEW_LINES_PATTERN)) {
      final String line = _line.trim();
      if (StringUtil.isEmptyOrSpaces(line)) continue;
      args.addAll(StringUtil.splitHonorQuotes(line));
    }
  }

}


