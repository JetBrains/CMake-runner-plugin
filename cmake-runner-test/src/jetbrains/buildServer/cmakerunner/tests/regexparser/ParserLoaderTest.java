

package jetbrains.buildServer.cmakerunner.tests.regexparser;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.regexparser.ParserLoader;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * @author Vladislav.Rassokhin
 */
public class ParserLoaderTest extends BaseTestCase {

  public static final String TEST_PARSER_RESOURCE_NAME = "/sample-parser.xml";

  @Test
  public void testLoadFromResource() throws Exception {
    Assert.assertNotNull(this.getClass().getResourceAsStream(TEST_PARSER_RESOURCE_NAME));
    final RegexParser parser = ParserLoader.loadParser(TEST_PARSER_RESOURCE_NAME, this.getClass());
    Assert.assertNotNull(parser);
  }

  @Test
  public void testLoadFromStream() throws Exception {
    final InputStream resourceStream = this.getClass().getResourceAsStream(TEST_PARSER_RESOURCE_NAME);
    Assert.assertNotNull(resourceStream);
    final RegexParser parser = ParserLoader.loadParser(resourceStream);
    Assert.assertNotNull(parser);
  }
}