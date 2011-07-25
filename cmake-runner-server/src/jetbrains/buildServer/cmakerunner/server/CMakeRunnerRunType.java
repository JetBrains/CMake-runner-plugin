package jetbrains.buildServer.cmakerunner.server;/*
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


/**
 * @author : Vladislav.Rassokhin
 */

import jetbrains.buildServer.cmakerunner.CMakeGenerator;
import jetbrains.buildServer.cmakerunner.CMakeRunnerConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class CMakeRunnerRunType extends RunType {

  public CMakeRunnerRunType(final RunTypeRegistry runTypeRegistry) {
    runTypeRegistry.registerRunType(this);
  }

  @NotNull
  @Override
  public String getType() {
    return CMakeRunnerConstants.RUNNER_TYPE;
  }

  @Override
  public String getDisplayName() {
    return CMakeRunnerConstants.RUNNER_DISPLAY_NAME;

  }

  @Override
  public String getDescription() {
    return CMakeRunnerConstants.RUNNER_DESCRIPTION;
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    // Do nothing
    return null;
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return "editCMakeRunner.jsp";
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return "viewCMakeRunner.jsp";
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    final String trueStr = Boolean.toString(true);
    final String falseStr = Boolean.toString(false);

    final Map<String, String> ret = new HashMap<String, String>();
    ret.put(CMakeRunnerConstants.UI_NO_WARN_UNUSED_CLI, trueStr);
    ret.put(CMakeRunnerConstants.UI_MAKEFILE_GENERATOR, CMakeGenerator.DEFAULT.getNormalName());

    return ret;
  }
}
