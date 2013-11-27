/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
import jetbrains.buildServer.agent.runner.ProcessListener;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.cmakerunner.CMakeBuildType;
import jetbrains.buildServer.cmakerunner.agent.output.RegexParsersBasedOutputListener;
import jetbrains.buildServer.cmakerunner.agent.util.OutputRedirectProcessor;
import jetbrains.buildServer.cmakerunner.agent.util.SimpleLogger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static jetbrains.buildServer.cmakerunner.CMakeConfigureConstants.*;

/**
 * @author : Vladislav.Rassokhin
 */
public class CMakeConfigureBuildService extends ExtendedBuildServiceAdapter {
  @NotNull
  public static final String DEFAULT_CMAKE_PROGRAM = "cmake";

  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {

    final List<String> arguments = new ArrayList<String>();
    final Map<String, String> runnerParameters = getRunnerParameters(); // all server-ui options
    final Map<String, String> environment = new HashMap<String, String>(System.getenv());
    environment.putAll(getBuildParameters().getEnvironmentVariables());

    // Path to 'cmake'
    String programPath = runnerParameters.get(UI_CMAKE_COMMAND);
    if (programPath == null) {
      programPath = DEFAULT_CMAKE_PROGRAM;
    }

    // Check for program exist
//    if (!FileUtil.checkIfExists(programPath) && FileUtil.findExecutableByNameInPATH(programPath, environment) == null)
//      throw new RunBuildException("Cannot locate `" + programPath + "' executable");


    // CMake options

    final String generator = runnerParameters.get(UI_MAKEFILE_GENERATOR);

    if (generator != null && !generator.equalsIgnoreCase("Default")) {
      arguments.add(RUNNER_MAKEFILE_GENERATOR);
      arguments.add(generator);
    }

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

    final String buildTypeName = runnerParameters.get(UI_CMAKE_BUILD_TYPE);
    CMakeBuildType buildType = null;
    for (final CMakeBuildType type : Arrays.asList(CMakeBuildType.values())) {
      if (type.getNormalName().equals(buildTypeName)) {
        buildType = type;
        break;
      }
    }
    if (buildType != null && buildType != CMakeBuildType.Default) {
      arguments.add(getVariableToArgument(RUNNER_CMAKE_BUILD_TYPE, buildType.getNormalName()));
    }

    // Other arguments
    addCustomArguments(arguments, runnerParameters.get(UI_ADDITIONAL_PARAMS));

    // Directory contains CMakeLists.txt
    String sourcePath = runnerParameters.get(UI_SOURCE_PATH);
    if (sourcePath == null) {
      sourcePath = ".";
    }
    arguments.add(sourcePath);

    final boolean redirectStdErr = Boolean.valueOf(runnerParameters.get(UI_REDIRECT_STDERR));
    // Result:
    final SimpleProgramCommandLine pcl = new SimpleProgramCommandLine(environment,
            getWorkingDirectory().getAbsolutePath(),
            programPath,
            arguments);
    return redirectStdErr ? OutputRedirectProcessor.wrap(getBuild(), pcl) : pcl;
  }

  @NotNull
  @Override
  public List<ProcessListener> getListeners() {
    return Collections.<ProcessListener>singletonList(new RegexParsersBasedOutputListener(new SimpleLogger(getLogger()), "/cmake-parser.xml"));
  }

}


