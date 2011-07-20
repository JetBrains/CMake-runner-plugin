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

package jetbrains.buildServer.makerunner;

/**
 * @author : Vladislav.Rassokhin
 */
public interface MakeRunnerConstants {
  String RUNNER_TYPE = "make-runner";

  String RUNNER_DESCRIPTION = "Runner for executing Make tasks";
  String RUNNER_DISPLAY_NAME = "GNU Make";

  String RUNNER_MAKE_TASKS = "ui.makeRunner.make.tasks";
  String RUNNER_MAKE_ADDITIONAL_CMD_PARAMS = "ui.makeRunner.make.additional.cmd.params";
  String RUNNER_MAKE_KEEP_GOING = "ui.makeRunner.make.keep.going";
  String RUNNER_MAKE_PROGRAM_PATH = "ui.makeRunner.make.program.path";

  // Make arguments
  String MAKE_CMDLINE_OPTIONS_MAKEFILE = "--file";
  String MAKE_CMDLINE_OPTIONS_KEEP_GOING = "--keep-going";

}
