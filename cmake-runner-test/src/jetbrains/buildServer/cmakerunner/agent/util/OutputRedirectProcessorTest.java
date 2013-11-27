/*
 * Copyright 2000-2013 JetBrains s.r.o.
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

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import jetbrains.buildServer.RunnerTest2Base;
import jetbrains.buildServer.agent.impl.SpringContextFixture;
import jetbrains.buildServer.agent.impl.SpringContextXmlBean;
import jetbrains.buildServer.runner.SimpleRunnerConstants;
import jetbrains.buildServer.serverSide.SFinishedBuild;
import jetbrains.buildServer.serverSide.SimpleParameter;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.io.File;

@SpringContextFixture(beans = {@SpringContextXmlBean(name = "wrapper", clazz = OutputRedirectProcessor.class)})
public class OutputRedirectProcessorTest extends RunnerTest2Base {
  @Test
  public void testWrap() throws Throwable {
    getBuildType().addConfigParameter(new SimpleParameter(OutputRedirectProcessor.ENABLE_STDERR_REDIRECT_CONFIG_PARAMETER, "true"));
    addRunParameter(SimpleRunnerConstants.USE_CUSTOM_SCRIPT, "true");
    if (SystemInfo.isWindows) {
      addRunParameter(SimpleRunnerConstants.SCRIPT_CONTENT, "ECHO ON\r\n" +
          "ECHO \"StdOut\"\r\n" +
          "ECHO \"StdErr\" 1>&2\r\n");
    } else if (SystemInfo.isUnix) {
      addRunParameter(SimpleRunnerConstants.SCRIPT_CONTENT, "echo \"StdOut\"\n" +
          "echo \"StdErr\" 1>&2\n");
    } else {
      throw new SkipException("Unsupported OS");
    }
    assertBuildLogContains("StdOut NORMAL", "StdErr NORMAL");
  }


  protected SFinishedBuild assertBuildLogContains(final String... expectedLines) throws Throwable {
    final SFinishedBuild build = doTest(null);

    dumpBuildLog(build);

    final String buildLog = preprocessActualString(getBuildLog(build), getCurrentDir(), getCurrentOwnPort()).replaceAll("[\r\n]+", "\n");

    for (final String expectedLine : expectedLines) {
      final boolean contains = buildLog.contains(expectedLine);
      Assert.assertTrue(contains, "Actual build log: " + buildLog);
    }
    return build;
  }


  @NotNull
  private File getTestData(@NotNull final String path) {
    return new File(new File("cmake-runner-test/testData").getAbsoluteFile(), path);
  }

  @NotNull
  @Override
  protected String getRunnerType() {
    return SimpleRunnerConstants.TYPE;
  }

  @Override
  protected String getTestDataPrefixPath() {
    return "cmake-runner-test/testData/";
  }

  @Override
  protected String getTestDataSuffixPath() {
    return "";
  }
}
