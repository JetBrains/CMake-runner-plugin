

package jetbrains.buildServer.cmakerunner.tests.regexparser;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import jetbrains.buildServer.cmakerunner.regexparser.RegexPattern;
import jetbrains.buildServer.cmakerunner.regexparser.Severity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class ParserConfigReadWriteTest extends BaseTestCase {

  private static final int SAMPLE_PARSER_PATTERNS_COUNT = 3;
  private static final String[] SAMPLE_PARSER_PATTERNS_REGEX = {"error: (.*)", "warning .*", ".*"};
  private static final String[] SAMPLE_PARSER_PATTERNS_DESCRIPTION_EXPR = {"$1", "$0", "$0"};
  private static final Severity[] SAMPLE_PARSER_PATTERNS_SEVERITY = {Severity.ERROR, Severity.WARN, Severity.INFO};
  private static final boolean[] SAMPLE_PARSER_PATTERNS_EAT = {true, false, true};

  @Test
  public void testLoadingSample() throws Exception {
    final InputStream parserConfigStream = this.getClass().getResourceAsStream("/sample-parser.xml");
    Assert.assertNotNull(parserConfigStream);
    final RegexParser parser = RegexParser.deserialize(parserConfigStream);
    Assert.assertNotNull(parser);
    Assert.assertEquals(parser.getId(), "Sample parser");
    Assert.assertEquals(parser.getName(), "jetbrains.buildServer.cmakerunner.regexparser.SampleRegexParser");

    final List<RegexPattern> patterns = parser.getPatterns();
    Assert.assertEquals(patterns.size(), SAMPLE_PARSER_PATTERNS_COUNT);

    for (int i = 0; i < SAMPLE_PARSER_PATTERNS_COUNT; i++) {
      final RegexPattern pattern = patterns.get(i);
      Assert.assertEquals(pattern.getPattern().pattern(), SAMPLE_PARSER_PATTERNS_REGEX[i]);
      Assert.assertEquals(pattern.getDescriptionExpression(), SAMPLE_PARSER_PATTERNS_DESCRIPTION_EXPR[i]);
      Assert.assertEquals(pattern.getSeverity(), SAMPLE_PARSER_PATTERNS_SEVERITY[i]);
      Assert.assertEquals(pattern.getEatLine(), SAMPLE_PARSER_PATTERNS_EAT[i]);
    }
  }

  @Test
  public void testSaveLoad() throws Exception {
    // or = original; de = deserialized
    final RegexParser orParser = new RegexParser("id1", "name1");
    orParser.addPattern(new RegexPattern(Pattern.compile(".*"), "$0", Severity.INFO, true));
    orParser.addPattern(new RegexPattern(Pattern.compile("[a-z]*"), "\"'\"", Severity.WARN, false));

    final String serialized = orParser.serialize();
    System.out.println("serialized = " + serialized);
    final RegexParser deParser = RegexParser.deserialize(serialized);
    Assert.assertNotNull(deParser);
    Assert.assertEquals(deParser.getId(), orParser.getId());
    Assert.assertEquals(deParser.getName(), orParser.getName());
    Assert.assertEquals(deParser.getPatterns(), orParser.getPatterns());
  }
}