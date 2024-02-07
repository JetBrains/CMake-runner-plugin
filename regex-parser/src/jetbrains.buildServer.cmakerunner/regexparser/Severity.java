
package jetbrains.buildServer.cmakerunner.regexparser;

/**
 * @author Vladislav.Rassokhin
 */
public enum Severity {
  WARN,
  ERROR,
  INFO,
  SPECIAL,
  BLOCK_START,
  BLOCK_FINISH,
  BLOCK_CHANGE,
  COMPILATION_START,
  COMPILATION_FINISH,
  COMPILATION_CHANGE,
}