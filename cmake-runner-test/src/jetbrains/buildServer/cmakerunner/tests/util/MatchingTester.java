

package jetbrains.buildServer.cmakerunner.tests.util;

import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class MatchingTester {
  /**
   * Tests tests for matching pattern & matching groups.
   * <p/>
   * Each test must contain testing string and may contains expected groups after matching.
   *
   * @param pattern pattern to test
   * @param tests   array of tests
   */
  public static void testMatching(final Pattern pattern, final String[]... tests) {
    for (final String[] test : tests) {
      final Matcher m = pattern.matcher(test[0]);
      Assert.assertTrue(m.find(), test[0]);
      for (int i = 0; i < test.length; i++) {
        Assert.assertEquals(test[i], m.group(i), test[0]);
      }
    }
  }

  /**
   * Tests tests for non-matching pattern.
   *
   * @param pattern pattern to test
   * @param tests   stings to test
   */
  public static void testNonMatching(final Pattern pattern, final String... tests) {
    for (final String test : tests) {
      final Matcher m = pattern.matcher(test);
      Assert.assertFalse(m.find(), test);
    }
  }
}