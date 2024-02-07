

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class BracketSequenceMakeLogger extends LoggerAdapter {
  public static final char OPEN_BRACKET = '(';
  public static final char CLOSE_BRACKET = ')';
  protected final StringBuilder myBuilder = new StringBuilder();
  private final boolean myIgnoreOtherSymbols;

  protected BracketSequenceMakeLogger(final boolean ignoreOtherSymbols) {
    this.myIgnoreOtherSymbols = ignoreOtherSymbols;
  }

  public BracketSequenceMakeLogger() {
    this(false);
  }

  @Override
  public void blockStart(@NotNull final String name) {
    myBuilder.append(OPEN_BRACKET);
  }

  @Override
  public void blockFinish(@NotNull final String name) {
    myBuilder.append(CLOSE_BRACKET);
  }

  public String getSequence() {
    return myBuilder.toString();
  }

  public void resetSequence() {
    myBuilder.setLength(0);
  }

  public boolean isSequenceCorrect() {
    final String str = myBuilder.toString();
    int opened = 0;
    for (int i = 0; i < str.length(); i++) {
      switch (str.charAt(i)) {
        case OPEN_BRACKET:
          opened++;
          break;
        case CLOSE_BRACKET:
          opened--;
          if (opened < 0) return false;
          break;
        default:
          if (!myIgnoreOtherSymbols) return false;
      }
    }
    return opened == 0;
  }
}