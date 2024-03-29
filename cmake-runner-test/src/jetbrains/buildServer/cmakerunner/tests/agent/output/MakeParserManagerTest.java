

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.agent.output.MakeParserManager;
import jetbrains.buildServer.cmakerunner.tests.util.MatchingTester;
import org.testng.annotations.Test;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserManagerTest extends BaseTestCase {

  private final String[][] DIRECTORY_ENTER_TEST_OK = {
          {"make.exe[1]: Entering directory `/cygdrive/c/TeamCity/'", "1", "/cygdrive/c/TeamCity/"},
          {"   GNUmake: Entering directory `~/home/build1'", null, "~/home/build1"},
          {"make[2]: Entering directory `somedir'", "2", "somedir"}
  };
  private final String[] DIRECTORY_ENTER_TEST_ERR = {
          "make.exe1]: Entering directory `/cygdrive/c/TeamCity/'",
          "make Entering directory `~/home/build1'",
          "make[2]: Entering directory somedir"
  };

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
  public void testParsingEnterDirectory() throws Exception {
    MatchingTester.testMatching(MakeParserManager.DIRECTORY_ENTER_PATTERN, DIRECTORY_ENTER_TEST_OK);
    MatchingTester.testNonMatching(MakeParserManager.DIRECTORY_ENTER_PATTERN, DIRECTORY_ENTER_TEST_ERR);
  }

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