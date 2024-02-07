
package jetbrains.buildServer.cmakerunner.regexparser;

import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public interface Logger {

  void message(@NotNull String message);

  void error(@NotNull String message);

  void warning(@NotNull String message);

  void blockStart(@NotNull String name);

  void blockFinish(@NotNull String name);

  void compilationBlockStart(@NotNull String name);

  void compilationBlockFinish(@NotNull String name);

}