/*
 * Copyright 2000-2015 JetBrains s.r.o.
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

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.agent.output.RegexParsersBasedOutputListener;
import jetbrains.teamcity.util.regex.*;
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
