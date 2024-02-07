

package jetbrains.buildServer.cmakerunner.tests.regexparser;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.regexparser.RegexPattern;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexPatternTest extends BaseTestCase {


  @Test
  public void testPatternConverter() throws Exception {
    final RegexPattern.PatternConverter pc = new RegexPattern.PatternConverter();

    Assert.assertTrue(pc.canConvert(Pattern.class));
    Assert.assertFalse(pc.canConvert(String.class));

    final String patternString = "(aba)+";
    final Pattern pattern = Pattern.compile(patternString);

    final String serialized = pc.toString(pattern);
    Assert.assertEquals(serialized, patternString);

    final Object parsedObject = pc.fromString(serialized);
    Assert.assertSame(Pattern.class, parsedObject.getClass());

    Assert.assertEquals(((Pattern) parsedObject).pattern(), patternString);
  }
}