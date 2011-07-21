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

package jetbrains.buildServer.makerunner.agent;

import com.intellij.util.containers.HashMap;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProcessListener;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.makerunner.agent.output.OutputListener;
import jetbrains.buildServer.makerunner.agent.util.FileUtil;
import jetbrains.buildServer.makerunner.agent.util.OSUtil;
import jetbrains.buildServer.makerunner.agent.util.SimpleMakeLogger;
import jetbrains.buildServer.runner.BuildFileRunnerUtil;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static jetbrains.buildServer.makerunner.MakeRunnerConstants.*;
import static jetbrains.buildServer.runner.BuildFileRunnerConstants.BUILD_FILE_PATH_KEY;

/**
 * @author : Vladislav.Rassokhin
 */
public class MakeTasksBuildService extends BuildServiceAdapter {
  // Tmp files set
  @NotNull
  private final Set<File> myFilesToDelete = new HashSet<File>();

  @NotNull
  private final AtomicReference<List<String>> myMakeTasks = new AtomicReference<List<String>>(new ArrayList<String>());
  private static final List<String> ONE_TASK_LIST = Collections.singletonList("default");
  @NotNull
  private static final String DEFAULT_MAKE_PROGRAM = "make";
  private static final String NEW_LINES_PATTERN = "[" + System.getProperty("line.separator", "\n") + "]+";

  @NotNull
  @Override
  public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {

    final List<String> arguments = new ArrayList<String>();
    final Map<String, String> runnerParameters = getRunnerParameters(); // all server-ui options
    final Map<String, String> environment = new HashMap<String, String>(getBuildParameters().getEnvironmentVariables());

    // Path to 'make'

    String programPath = runnerParameters.get(RUNNER_MAKE_PROGRAM_PATH);
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
    if (Boolean.valueOf(runnerParameters.get(RUNNER_MAKE_KEEP_GOING))) {
      arguments.add(MAKE_CMDLINE_OPTIONS_KEEP_GOING);
    }

    // Other arguments
    addCustomArguments(arguments, runnerParameters.get(RUNNER_MAKE_ADDITIONAL_CMD_PARAMS));

    // Tasks names
    final String makeTasksStr = runnerParameters.get(RUNNER_MAKE_TASKS);
    addCustomArguments(arguments, makeTasksStr);

    myMakeTasks.set(splitMakeTasks(makeTasksStr));


    // Result:
    return OSUtil.makeOSSpecific(new SimpleProgramCommandLine(environment,
            getWorkingDirectory().getAbsolutePath(),
            programPath,
            arguments));
  }

  @NotNull
  private List<String> splitMakeTasks(@Nullable final String tasksStr) {
    if (tasksStr == null) return ONE_TASK_LIST;
    final List<String> tasks = StringUtil.split(tasksStr, " ");
    if (tasks.isEmpty()) return ONE_TASK_LIST;
    return tasks;
  }

  @Override
  public void afterProcessFinished() {
    // Remove tmp files
    for (final File file : myFilesToDelete) {
      jetbrains.buildServer.util.FileUtil.delete(file);
    }
    myFilesToDelete.clear();
  }

  @NotNull
  @Override
  public List<ProcessListener> getListeners() {
    return Collections.<ProcessListener>singletonList(new OutputListener(new SimpleMakeLogger(getLogger()), myMakeTasks));
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


