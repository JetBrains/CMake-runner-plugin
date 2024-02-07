

package jetbrains.buildServer.cmakerunner.tests.agent.output;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeOutputFoldingGenerator {

  public static String generateStartingTargetMessage(final String target, final String dirName) {
    return String.format("Making %s in %s", target, dirName);
  }

  public static String generateEnterMessage(final String dirName, final int level) {
    if (level == -1) {
      return String.format("make: Entering directory `%s'", dirName);
    }
    return String.format("make[%d]: Entering directory `%s'", level, dirName);
  }

  public static String generateLeaveMessage(final String dirName, final int level) {
    if (level == -1) {
      return String.format("make: Leaving directory `%s'", dirName);
    }
    return String.format("make[%d]: Leaving directory `%s'", level, dirName);
  }
}