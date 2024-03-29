

package jetbrains.buildServer.cmakerunner.server;

import jetbrains.MockBuildType;
import jetbrains.buildServer.cmakerunner.CMakeBuildConstants;
import jetbrains.buildServer.cmakerunner.CMakeConfigureConstants;
import jetbrains.buildServer.serverSide.discovery.DiscoveredObject;
import jetbrains.buildServer.util.browser.FileSystemBrowser;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CMakeDiscoveryExtensionTest {

  private CMakeDiscoveryExtension myExtension;

  @BeforeMethod
  public void setUp() throws Exception {
    myExtension = new CMakeDiscoveryExtension();
  }

  @Test
  public void testSimpleProjectDiscovery() throws Exception {
    final FileSystemBrowser browser = new FileSystemBrowser(getTestData("discovery/simple"));
    final List<DiscoveredObject> discover = myExtension.discover(new MockBuildType(), browser);
    assertNotNull(discover);
    assertFalse(discover.isEmpty());
    assertEquals(discover.size(), 2);
    for (final DiscoveredObject runner : discover) {
      assertNotNull(runner);
      final String type = runner.getType();
      Assert.assertNotNull(type);
      final Map<String, String> parameters = runner.getParameters();
      Assert.assertNotNull(parameters);
      if (CMakeConfigureConstants.TYPE.equals(type)) {
        assertTrue(parameters.containsKey(CMakeConfigureConstants.UI_SOURCE_PATH));
        assertEquals(parameters.get(CMakeConfigureConstants.UI_SOURCE_PATH), "CMakeLists.txt");
      } else if (CMakeBuildConstants.TYPE.equals(type)) {
        assertTrue(parameters.containsKey(CMakeBuildConstants.UI_BUILD_PATH));
        assertEquals(parameters.get(CMakeBuildConstants.UI_BUILD_PATH), ""); // ROOT
      } else {
        fail("Expected CMake configure or CMake build types");
      }
    }
  }


  @NotNull
  private static File getTestData(@NotNull final String path) {
    final File home = new File("cmake-runner-test/testData");
    return new File(home, path);
  }
}