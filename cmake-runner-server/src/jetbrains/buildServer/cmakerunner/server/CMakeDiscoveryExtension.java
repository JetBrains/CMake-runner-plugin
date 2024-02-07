

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