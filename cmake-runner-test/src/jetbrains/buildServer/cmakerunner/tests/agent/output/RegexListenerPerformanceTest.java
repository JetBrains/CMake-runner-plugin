/*
 * Copyright 2000-2017 JetBrains s.r.o.
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
import jetbrains.buildServer.cmakerunner.agent.util.FileUtil;
import jetbrains.buildServer.cmakerunner.regexparser.ParserManager;
import jetbrains.buildServer.util.WatchDog;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexListenerPerformanceTest extends BaseTestCase {
  @DataProvider(name = "CMakeOutput")
  public static Object[][] CMakeOutput() {
    return new Object[][]{
        new String[]{"cmake.log"},
        new String[]{"cmake-trace.log"},
    };
  }

  @Test(dataProvider = "CMakeOutput")
  public void testHugeCMakeOutput(@NotNull final String file) throws Exception {
    final CounterLogger logger = new CounterLogger();
    final ParserManager manager = new ParserManager(logger);
    final RegexParsersBasedOutputListener listener = new RegexParsersBasedOutputListener(manager, "/cmake-parser.xml");
    Assert.assertEquals(listener.getParsers().size(), 1);
    Assert.assertEquals(listener.getManager(), manager);

    final List<String> strings = FileUtil.readFile(new File("testData/logs", file));

    final WatchDog watchDog = new WatchDog("PerformanceTest");
    for (int i = 0; i < 10; i++) {
      logger.message = 0;
      for (final String string : strings) {
        listener.onStandardOutput(string);
      }
      Assert.assertEquals(logger.message, strings.size());
      watchDog.watchAndReset("file processed " + i);
    }
  }
}
