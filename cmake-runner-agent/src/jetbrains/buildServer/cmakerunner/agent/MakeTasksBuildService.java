/*
 * Copyright 2000-2014 JetBrains s.r.o.
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

import com.intellij.util.containers.HashMap;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.ProcessListener;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.cmakerunner.agent.output.MakeOutputListener;
import jetbrains.buildServer.cmakerunner.agent.util.FileUtil;
import jetbrains.buildServer.cmakerunner.agent.util.OutputRedirectProcessor;
import jetbrains.buildServer.cmakerunner.agent.util.SimpleLogger;
import jetbrains.buildServer.runner.BuildFileRunnerUtil;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static jetbrains.buildServer.cmakerunner.MakeRunnerConstants.*;
import static jetbrains.buildServer.runner.BuildFileRunnerConstants.BUILD_FILE_PATH_KEY;

/**
 * @author : Vladislav.Rassokhin
 */
public class MakeTasksBuildService extends ExtendedBuildServiceAdapter {
  @NotNull
  private final AtomicReference<List<String>> myMakeTasks = new AtomicReference<List<String>>(new ArrayList<String>());
  @NotNull
  private static final List<String> ONE_TASK_LIST = Collections.singletonList("default");
  @NotNull
  private static final String DEFAULT_MAKE_PROGRAM = "make";
  @Nullable private File myCustomPattersFile;

  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {

    final List<String> arguments = new ArrayList<String>();
    final Map<String, String> runnerParameters = getRunnerParameters(); // all server-ui options
    final Map<String, String> environment = new HashMap<String, String>(getBuildParameters().getEnvironmentVariables());

    // Path to 'make'

    String programPath = runnerParameters.get(UI_MAKE_PROGRAM_PATH);
    if (programPath == null) {
      programPath = DEFAULT_MAKE_PROGRAM;
    }

    // Check for program exist
//    if (!FileUtil.checkIfExists(programPath) && FileUtil.findExecutableByNameInPATH(programPath, environment) == null)
//      throw new RunBuildException("Cannot locate `" + programPath + "' executable");


    // Make options

    // Custom Makefile if specified
    final File buildFile = getBuildFile(runnerParameters);
    if (buildFile != null) {
      arguments.add(MAKE_CMDLINE_OPTIONS_MAKEFILE);
      arguments.add(buildFile.getAbsolutePath());
    }

    // Keep-going
    if (Boolean.valueOf(runnerParameters.get(UI_MAKE_KEEP_GOING))) {
      arguments.add(MAKE_CMDLINE_OPTIONS_KEEP_GOING);
    }

    // Other arguments
    addCustomArguments(arguments, runnerParameters.get(UI_MAKE_ADDITIONAL_CMD_PARAMS));

    // Tasks names
    final String makeTasksStr = runnerParameters.get(UI_MAKE_TASKS);
    addCustomArguments(arguments, makeTasksStr);

    myMakeTasks.set(splitMakeTasks(makeTasksStr));

    final String customPattersFilePath = getRunnerContext().getConfigParameters().get(TEAMCITY_MAKE_OUTPUT_PATTERNS_FILE_PROPERTY);
    if (FileUtil.checkIfExists(customPattersFilePath)) {
      final File file = FileUtil.getCanonicalFile(new File(customPattersFilePath));
      if (file.exists()) {
        myCustomPattersFile = file;
      }
    }

    final boolean redirectStdErr = Boolean.valueOf(runnerParameters.get(UI_REDIRECT_STDERR));
    // Result:
    final SimpleProgramCommandLine pcl = new SimpleProgramCommandLine(environment,
            getWorkingDirectory().getAbsolutePath(),
            programPath,
            arguments);
    return redirectStdErr ? OutputRedirectProcessor.wrap(getBuild(), pcl) : pcl;
  }

  @NotNull
  private List<String> splitMakeTasks(@Nullable final String tasksStr) {
    if (tasksStr == null) return ONE_TASK_LIST;
    final List<String> tasks = StringUtil.split(tasksStr, " ");
    if (tasks.isEmpty()) return ONE_TASK_LIST;
    return tasks;
  }

  @NotNull
  @Override
  public List<ProcessListener> getListeners() {
    return Collections.<ProcessListener>singletonList(new MakeOutputListener(new SimpleLogger(getLogger()), myMakeTasks, myCustomPattersFile));
  }

  @Nullable
  private File getBuildFile(@NotNull final Map<String, String> runParameters) throws RunBuildException {
    final File buildFile;
    if (BuildFileRunnerUtil.isCustomBuildFileUsed(runParameters)) {
      buildFile = BuildFileRunnerUtil.getBuildFile(runParameters);
      myFilesToDelete.add(buildFile);
    } else {
      final String buildFilePath = runParameters.get(BUILD_FILE_PATH_KEY);
      if (PropertiesUtil.isEmptyOrNull(buildFilePath)) {
        //use make defaults
        buildFile = null;
      } else {
        buildFile = BuildFileRunnerUtil.getBuildFile(runParameters);
      }
    }
    return buildFile;
  }

}


