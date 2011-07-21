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

package jetbrains.buildServer.makerunner.tests.agent.output;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.makerunner.agent.output.MakeParserManager;
import jetbrains.buildServer.makerunner.tests.util.MatchingTester;
import org.testng.annotations.Test;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserManagerTest extends BaseTestCase {

  private final String[][] DIRECTORY_LEAVE_TEST_OK = {
          {"make.exe[1]: Leaving directory `/cygdrive/c/TeamCity/'", "1", "/cygdrive/c/TeamCity/"},
          {"   GNUmake: Leaving directory `~/home/build1'", null, "~/home/build1"},
          {"make[2]: Leaving directory `somedir'", "2", "somedir"}
  };
  private final String[] DIRECTORY_LEAVE_TEST_ERR = {
          "make.exe1]: Leaving directory `/cygdrive/c/TeamCity/'",
          "make Leaving directOOry `~/home/build1'",
          "make[2]: Leaving directory somedir"
  };

  private final String[][] MAKING_IN_TEST_OK = {
          {"Making all in ."},
          {"  Making something in somewhere"},
          {"Making some\\thing in so-a:de:r"}
  };
  private final String[] MAKING_IN_TEST_ERR = {
          "Makeng a in b",
          "making a a in b",
  };

  @Test
  public void testParsingLeaveDirectory() throws Exception {
    MatchingTester.testMatching(MakeParserManager.DIRECTORY_LEAVE_PATTERN, DIRECTORY_LEAVE_TEST_OK);
    MatchingTester.testNonMatching(MakeParserManager.DIRECTORY_LEAVE_PATTERN, DIRECTORY_LEAVE_TEST_ERR);
  }

  @Test
  public void testParsingMakingIn() throws Exception {
    MatchingTester.testMatching(MakeParserManager.MAKING_IN_PATTERN, MAKING_IN_TEST_OK);
    MatchingTester.testNonMatching(MakeParserManager.MAKING_IN_PATTERN, MAKING_IN_TEST_ERR);
  }

}
