
package jetbrains.buildServer.cmakerunner.regexparser;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class LoggerAdapter implements Logger {
  public void message(@NotNull final String message) {
  }

  public void error(@NotNull final String message) {
  }

  public void warning(@NotNull final String message) {
  }

  public void blockStart(@NotNull final String name) {
  }

  public void blockFinish(@NotNull final String name) {
  }

  public void compilationBlockStart(@NotNull final String name) {
  }

  public void compilationBlockFinish(@NotNull final String name) {
  }
}