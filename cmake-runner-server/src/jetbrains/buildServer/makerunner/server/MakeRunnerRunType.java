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

package jetbrains.buildServer.makerunner.server;


/**
 * @author : Vladislav.Rassokhin
 */

import jetbrains.buildServer.makerunner.MakeRunnerConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class MakeRunnerRunType extends RunType {

  public MakeRunnerRunType(final RunTypeRegistry runTypeRegistry) {
    runTypeRegistry.registerRunType(this);
  }

  @NotNull
  @Override
  public String getType() {
    return MakeRunnerConstants.RUNNER_TYPE;
  }

  @Override
  public String getDisplayName() {
    return MakeRunnerConstants.RUNNER_DISPLAY_NAME;

  }

  @Override
  public String getDescription() {
    return MakeRunnerConstants.RUNNER_DESCRIPTION;
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    // Do nothing
    return null;
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return "editMakeRunner.jsp";
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return "viewMakeRunner.jsp";
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return null;
  }
}
