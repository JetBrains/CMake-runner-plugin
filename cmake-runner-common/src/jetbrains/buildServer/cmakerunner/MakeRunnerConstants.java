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

package jetbrains.buildServer.cmakerunner;

import org.jetbrains.annotations.NonNls;

/**
 * @author : Vladislav.Rassokhin
 */
public interface MakeRunnerConstants {
  @NonNls
  String TYPE = "make-runner";

  @NonNls
  String DESCRIPTION = "Runner for executing Make tasks";
  @NonNls
  String DISPLAY_NAME = "GNU Make";

  @NonNls
  String UI_PREFIX = "ui-" + TYPE + "-";

  @NonNls
  String UI_MAKE_TASKS = UI_PREFIX + "make-tasks";
  @NonNls
  String UI_MAKE_ADDITIONAL_CMD_PARAMS = UI_PREFIX + "additional-cmd-params";
  @NonNls
  String UI_MAKE_KEEP_GOING = UI_PREFIX + "keep-going";
  @NonNls
  String UI_REDIRECT_STDERR = UI_PREFIX + "redirect-stderr";
  @NonNls
  String UI_MAKE_PROGRAM_PATH = UI_PREFIX + "make-program-path";

  // Make arguments
  @NonNls
  String MAKE_CMDLINE_OPTIONS_MAKEFILE = "--file";
  @NonNls
  String MAKE_CMDLINE_OPTIONS_KEEP_GOING = "--keep-going";

}
