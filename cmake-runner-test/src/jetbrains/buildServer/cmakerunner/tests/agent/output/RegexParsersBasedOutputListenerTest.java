

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.agent.output.RegexParsersBasedOutputListener;
import jetbrains.buildServer.cmakerunner.regexparser.Logger;
import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import jetbrains.buildServer.cmakerunner.regexparser.ParserManager;
import jetbrains.buildServer.cmakerunner.tests.regexparser.ParserLoaderTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexParsersBasedOutputListenerTest extends BaseTestCase {
  @Test
  public void testNoParsersFlow() throws Exception {
    final CounterLogger logger = new CounterLogger();
    final ParserManager manager = new ParserManager(logger);
    final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(manager);
    Assert.assertEquals(listener.getParsers().size(), 0);
    Assert.assertEquals(listener.getManager(), manager);

    final int outCount = 12;
    final int errCount = 4;
    for (int i = 0; i < outCount; ++i) {
      listener.onStandardOutput("test");
    }
    for (int i = 0; i < errCount; ++i) {
      listener.onErrorOutput("test");
    }
    Assert.assertEquals(logger.message, outCount);
    Assert.assertEquals(logger.warning, errCount);
  }

  @Test
  public void testConstructorsWithoutParsers() throws Exception {
    {
      final Logger logger = new LoggerAdapter();
      final ParserManager manager = new ParserManager(logger);
      final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(manager);
      Assert.assertEquals(listener.getParsers().size(), 0);
      Assert.assertEquals(listener.getManager(), manager);
    }
    {
      final Logger logger = new LoggerAdapter();
      final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(logger);
      Assert.assertEquals(listener.getParsers().size(), 0);
      Assert.assertEquals(listener.getManager().getLogger(), logger);
    }
  }

  @Test
  public void testConstructorsWithSampleParser() throws Exception {
    {
      final Logger logger = new LoggerAdapter();
      final ParserManager manager = new ParserManager(logger);
      final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(manager, ParserLoaderTest.TEST_PARSER_RESOURCE_NAME);
      Assert.assertEquals(listener.getParsers().size(), 1);
      Assert.assertEquals(listener.getManager(), manager);
    }
    {
      final Logger logger = new LoggerAdapter();
      final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(logger, ParserLoaderTest.TEST_PARSER_RESOURCE_NAME);
      Assert.assertEquals(listener.getParsers().size(), 1);
      Assert.assertEquals(listener.getManager().getLogger(), logger);
    }
  }
}