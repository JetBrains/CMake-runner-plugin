

package jetbrains.buildServer.cmakerunner;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"UnusedDeclaration"})
public enum CMakeBuildType {
  Default("None"),
  Debug("Debug"),
  Release("Release"),
  RelWithDebInfo("RelWithDebInfo"),
  MinSizeRel("MinSizeRel");

  private final String myNormalName;

  CMakeBuildType(final String normalName) {
    myNormalName = normalName;
  }

  public String getNormalName() {
    return myNormalName;
  }
}