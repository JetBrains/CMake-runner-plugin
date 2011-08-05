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

package jetbrains.buildServer.cmakerunner.server;


/**
 * @author : Vladislav.Rassokhin
 */

import jetbrains.buildServer.cmakerunner.CMakeBuildConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class CMakeBuildRunType extends RunType {

  public CMakeBuildRunType(@NotNull final RunTypeRegistry runTypeRegistry) {
    runTypeRegistry.registerRunType(this);
  }

  @NotNull
  @Override
  public String getType() {
    return CMakeBuildConstants.TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return CMakeBuildConstants.DISPLAY_NAME;

  }

  @NotNull
  @Override
  public String getDescription() {
    return CMakeBuildConstants.DESCRIPTION;
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    // Do nothing
    return null;
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return "../cmake-runner/editCMakeBuildRunner.jsp";
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return "../cmake-runner/viewCMakeBuildRunner.jsp";
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    final Map<String, String> ret = new HashMap<String, String>();
    ret.put(CMakeBuildConstants.UI_REDIRECT_STDERR, Boolean.toString(true));
    return ret;
  }
}
