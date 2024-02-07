

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OSUtilTest {

  @Test
  public void testIsCLIExist() throws Exception {
    Assert.assertTrue(OSUtil.isCLIExist(System.getenv()), "CLI should exist");
  }

  @Test
  public void testGetCLIFullPathDoesNotThrowsException() throws Exception {
    final String path = OSUtil.getCLIFullPath(System.getenv());
    Assert.assertNotNull(path);
    Assert.assertTrue(FileUtil2.checkIfExists(path));
  }

  @DataProvider(name = "UnixOnly")
  public static Object[][] UnixOnly() {
    if (SystemInfo.isUnix) return new Object[][]{{true}};
    return new Object[][]{};
  }

  @Test(dataProvider = "UnixOnly")
  public void testGuessUnixShell(final Boolean unix) throws Exception {
    final String shell = OSUtil.guessUnixShell(System.getenv());
    Assert.assertNotNull(shell, "Shell should be guessed on unix machines");
  }
}