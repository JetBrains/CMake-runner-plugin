

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