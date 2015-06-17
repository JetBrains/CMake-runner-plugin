/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
import jetbrains.buildServer.agent.messages.KeepMessagesLogger;
import jetbrains.buildServer.agent.messages.regex.RegexParserToSimpleMessagesTranslatorAdapter;
import jetbrains.buildServer.agent.messages.regex.RegexParsersHelper;
import jetbrains.buildServer.agent.messages.regex.SimpleLogger;
import jetbrains.buildServer.agent.messages.regex.impl.ParsersRegistryImpl;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.cmakerunner.agent.output.MakeParserManager;
import jetbrains.buildServer.cmakerunner.agent.util.FileUtil;
import jetbrains.buildServer.cmakerunner.agent.util.OutputRedirectProcessor;
import jetbrains.buildServer.runner.BuildFileRunnerUtil;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.teamcity.util.regex.ParserLoader;
import jetbrains.teamcity.util.regex.RegexParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
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
  private final ParsersRegistryImpl myParsersRegistry;
  private final ArrayList<RegexParserToSimpleMessagesTranslatorAdapter> myRegisteredTranslators = new ArrayList<RegexParserToSimpleMessagesTranslatorAdapter>();
  private MakeParserManager myParserManager;

  public MakeTasksBuildService(@NotNull final ParsersRegistryImpl parsersRegistry) {
    myParsersRegistry = parsersRegistry;
  }

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

    // Register parsers
    final KeepMessagesLogger logger = new KeepMessagesLogger();
    myParserManager = new MakeParserManager(new SimpleLogger(logger), myMakeTasks);

    if (isDefaultParsersEnabled()) {
      final RegexParser parser = ParserLoader.loadParser("/make-parser.xml", this.getClass());
      if (parser == null) {
        getLogger().message("Cannot load default make parser");
      } else {
        final RegexParserToSimpleMessagesTranslatorAdapter adapter = new RegexParserToSimpleMessagesTranslatorAdapter(parser, myParserManager, logger);
        myRegisteredTranslators.add(adapter);
        myParsersRegistry.enable(adapter);
      }
    } else {
      getLogger().message("Default messages parser disabled");
    }

    final String customPattersFilePath = getRunnerContext().getConfigParameters().get(TEAMCITY_MAKE_OUTPUT_PATTERNS_FILE_PROPERTY);
    if (!StringUtil.isEmptyOrSpaces(customPattersFilePath) && FileUtil.checkIfExists(customPattersFilePath)) {
      final File file = FileUtil.getCanonicalFile(new File(customPattersFilePath));
      if (file.exists()) {
        final RegexParser parser = RegexParsersHelper.loadParserFromFile(file);
        if (parser != null) {
          final RegexParserToSimpleMessagesTranslatorAdapter adapter = new RegexParserToSimpleMessagesTranslatorAdapter(parser, myParserManager, logger);
          myRegisteredTranslators.add(adapter);
          myParsersRegistry.enable(adapter);
        } else {
          getLogger().message("Cannot load parser from custom path: " + file);
        }
      } else {
        getLogger().message("Custom parser file not found: " + customPattersFilePath);
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

  private boolean isDefaultParsersEnabled() {
    final String defaultParsers = getRunnerContext().getConfigParameters().get(TEAMCITY_MAKE_OUTPUT_DEFAULT_PATTERNS_ENABLED_PROPERTY);
    return StringUtil.isEmptyOrSpaces(defaultParsers) || PropertiesUtil.getBoolean(defaultParsers);
  }

  @NotNull
  private List<String> splitMakeTasks(@Nullable final String tasksStr) {
    if (tasksStr == null) return ONE_TASK_LIST;
    final List<String> tasks = StringUtil.split(tasksStr, " ");
    if (tasks.isEmpty()) return ONE_TASK_LIST;
    return tasks;
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

  @Override
  public void afterProcessFinished() {
    super.afterProcessFinished();
    // Finish all targets
    myParserManager.finishAllTargets();
    // Unregister parsers
    for (RegexParserToSimpleMessagesTranslatorAdapter adapter : myRegisteredTranslators) {
      myParsersRegistry.disable(adapter);
    }
  }

}


