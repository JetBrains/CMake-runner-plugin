

package jetbrains.buildServer.cmakerunner.tests.util;

import jetbrains.buildServer.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Vladislav.Rassokhin
 */
public class Paths {
  @NotNull
  public static File getTestDataPath() {
    return FileUtil.getCanonicalFile(new File("make-runner-test/testData"));
  }

  @NotNull
  public static File getTestDataPath(@NotNull final String p) {
    return FileUtil.getCanonicalFile(new File(getTestDataPath(), p));
  }
}