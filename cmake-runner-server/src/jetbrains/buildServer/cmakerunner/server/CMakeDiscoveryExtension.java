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

package jetbrains.buildServer.cmakerunner.server;

import jetbrains.buildServer.cmakerunner.CMakeBuildConstants;
import jetbrains.buildServer.cmakerunner.CMakeConfigureConstants;
import jetbrains.buildServer.serverSide.discovery.BreadthFirstRunnerDiscoveryExtension;
import jetbrains.buildServer.serverSide.discovery.DiscoveredObject;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.browser.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @since 8.1
 */
public class CMakeDiscoveryExtension extends BreadthFirstRunnerDiscoveryExtension {

  public static final String CMAKE_LISTS_TXT = "CMakeLists.txt";

  @NotNull
  @Override
  protected List<DiscoveredObject> discoverRunnersInDirectory(@NotNull final Element dir, @NotNull final List<Element> files) {
    final ArrayList<DiscoveredObject> discovered = new ArrayList<DiscoveredObject>();
    final List<Element> found = new LinkedList<Element>();
    for (final Element file : files) {
      if (CMAKE_LISTS_TXT.equalsIgnoreCase(file.getName())) {
        found.add(file);
      }
    }
    for (final Element file : found) {
      discovered.add(new DiscoveredObject(CMakeConfigureConstants.TYPE, CollectionsUtil.asMap(
          CMakeConfigureConstants.UI_SOURCE_PATH, file.getFullName()
      )));
      discovered.add(new DiscoveredObject(CMakeBuildConstants.TYPE, CollectionsUtil.asMap(
          CMakeBuildConstants.UI_BUILD_PATH, dir.getFullName()
      )));
    }
    return discovered;
  }
}
