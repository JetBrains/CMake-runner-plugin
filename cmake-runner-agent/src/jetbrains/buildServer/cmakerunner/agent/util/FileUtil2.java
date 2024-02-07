

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Roman.Chernyatchik
 */
public class FileUtil2 {

  /**
   * @param path Path to check
   * @return true, if path exists
   */
  public static boolean checkIfExists(@NotNull final String path) {
    return new File(path).exists();
  }


  @Nullable
  public static String findExecutableByNameInPATH(@NotNull final String exeName,
                                                  @NotNull final Map<String, String> environment) {
    final String path = OSUtil.getPathEnvVariable(environment);
    if (path != null) {
      final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);

      //tokens - are pathes with system-dependent slashes
      while (st.hasMoreTokens()) {
        final String possible_path = st.nextToken() + "/" + exeName;
        if (FileUtil2.checkIfExists(possible_path)) {
          return possible_path;
        }
      }
    }

    if (SystemInfo.isWindows && !exeName.endsWith(".exe") && !exeName.endsWith(".bat")) {
      String deep;
      deep = findExecutableByNameInPATH(exeName + ".exe", environment);
      if (deep == null) {
        deep = findExecutableByNameInPATH(exeName + ".bat", environment);
      }
      return deep;
    }
    return null;
  }
}