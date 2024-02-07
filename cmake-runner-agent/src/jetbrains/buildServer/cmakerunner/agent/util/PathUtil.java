

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class PathUtil {
  private static final Pattern winDrivePath = Pattern.compile("^([a-zA-Z]):(.*)$");
  @NotNull
  private static final String SSLASH = "/";
  @NotNull
  private static final String BSLASH = "\\\\";

  @NotNull
  public static String toUnixStylePath(@NotNull final String path) {
    if (path.startsWith("/")) return path;

    if (SystemInfo.isUnix) {
      return path;
    }

    if (SystemInfo.isWindows) {
      final Matcher m = winDrivePath.matcher(path);
      if (m.find()) {
        final StringBuilder newPath = new StringBuilder();
        newPath.append("/cygdrive/").append(m.group(1).toLowerCase());
        newPath.append(m.group(2).replaceAll(BSLASH, SSLASH));
        return newPath.toString();
      }
    }
    return path;
  }
}