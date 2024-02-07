
package jetbrains.buildServer.cmakerunner.agent.util;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class SimpleLogger extends LoggerAdapter {
  @NotNull
  private final BuildProgressLogger myBuildLogger;

  public SimpleLogger(@NotNull final BuildProgressLogger buildLogger) {
    myBuildLogger = buildLogger;
  }

  @Override
  public void message(@NotNull final String message) {
    myBuildLogger.message(message);
  }

  @Override
  public void error(@NotNull final String message) {
    myBuildLogger.error(message);
  }

  @Override
  public void warning(@NotNull final String message) {
    myBuildLogger.warning(message);
  }

  @Override
  public void blockStart(@NotNull final String name) {
    myBuildLogger.activityStarted(name, DefaultMessagesInfo.BLOCK_TYPE_TARGET);
  }

  @Override
  public void blockFinish(@NotNull final String name) {
    myBuildLogger.activityFinished(name, DefaultMessagesInfo.BLOCK_TYPE_TARGET);
  }

  @Override
  public void compilationBlockStart(@NotNull final String name) {
    myBuildLogger.activityStarted(name, DefaultMessagesInfo.BLOCK_TYPE_COMPILATION);
  }

  @Override
  public void compilationBlockFinish(@NotNull final String name) {
    myBuildLogger.activityFinished(name, DefaultMessagesInfo.BLOCK_TYPE_COMPILATION);
  }
}