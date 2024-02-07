

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class CounterLogger extends LoggerAdapter {
  public int message;
  public int error;
  public int warning;
  public int special;

  @Override
  public void message(@NotNull final String text) {
    message++;
  }

  @Override
  public void error(@NotNull final String text) {
    error++;
  }

  @Override
  public void warning(@NotNull final String text) {
    warning++;
  }

  public void special() {
    special++;
  }

}