/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.makerunner.tests;

import jetbrains.buildServer.makerunner.agent.util.LoggerAdapter;
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
