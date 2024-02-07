
package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.cmakerunner.agent.output.MakeOutputListener;
import jetbrains.buildServer.cmakerunner.regexparser.LoggerAdapter;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static jetbrains.buildServer.cmakerunner.tests.agent.output.MakeOutputFoldingGenerator.generateEnterMessage;
import static jetbrains.buildServer.cmakerunner.tests.agent.output.MakeOutputFoldingGenerator.generateLeaveMessage;

/**
 * @author Vladislav.Rassokhin
 */
public class OutputListenerTest extends TestCase {

  @Test
  public void testTargetsFolding() throws Exception {
    final BracketSequenceMakeLogger logger = new BracketSequenceMakeLogger();
    final AtomicReference<List<String>> makeTasks = new AtomicReference<List<String>>(Arrays.asList("all", "clean"));
    final MakeOutputListener mll = new MakeOutputListener(logger, makeTasks, null, true);

    {
      final File workingDirectory = new File("");
      final String wdAp = workingDirectory.getAbsolutePath();
      mll.processStarted("make", workingDirectory);
      mll.onStandardOutput(generateEnterMessage(wdAp, -1));

      mll.onStandardOutput(generateEnterMessage(wdAp + "/b", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/b", 1));
      mll.onStandardOutput(generateEnterMessage(wdAp + "/c", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/c", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp + "/a", 1));
      mll.onStandardOutput(generateEnterMessage(wdAp + "/a/d", 2));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/a/d", 2));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/a", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp, 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp, 1));


      mll.onStandardOutput(generateEnterMessage(wdAp + "/c", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/c", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp, 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp, 1));

      mll.onStandardOutput(generateLeaveMessage(wdAp, -1));
      mll.processFinished(0);
    }

    Assert.assertTrue(logger.isSequenceCorrect());
    Assert.assertEquals(logger.getSequence(), "(()()(()))(())");
  }

  @Test
  public void testTargetsFoldingDirs() throws Exception {
    final BracketSequenceMakeLogger logger = new BracketSequenceMakeLogger(true) {
      @Override
      public void blockStart(@NotNull final String name) {
        super.blockStart(name);
        myBuilder.append(name.charAt(name.length() - 1));
      }

      @Override
      public void blockFinish(@NotNull final String name) {
        super.blockFinish(name);
        myBuilder.append(name.charAt(name.length() - 1));
      }
    };
    final AtomicReference<List<String>> makeTasks = new AtomicReference<List<String>>(Arrays.asList("all", "clean"));
    final MakeOutputListener mll = new MakeOutputListener(logger, makeTasks, null, true);

    {
      final File workingDirectory = new File("");
      final String wdAp = workingDirectory.getAbsolutePath();
      mll.processStarted("make", workingDirectory);
      mll.onStandardOutput(generateEnterMessage(wdAp, -1));

      mll.onStandardOutput(generateEnterMessage(wdAp + "/b", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/b", 1));
      mll.onStandardOutput(generateEnterMessage(wdAp + "/c", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/c", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp + "/a", 1));
      mll.onStandardOutput(generateEnterMessage(wdAp + "/a/d", 2));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/a/d", 2));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/a", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp, 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp, 1));


      mll.onStandardOutput(generateEnterMessage(wdAp + "/c", 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp + "/c", 1));

      mll.onStandardOutput(generateEnterMessage(wdAp, 1));
      mll.onStandardOutput(generateLeaveMessage(wdAp, 1));

      mll.onStandardOutput(generateLeaveMessage(wdAp, -1));
      mll.processFinished(0);
    }

    Assert.assertTrue(logger.isSequenceCorrect());
    Assert.assertEquals(logger.getSequence(), "(.(b)b(c)c(a(d)d)a).(.(c)c).");
  }

  @Test
  public void testSubstring() throws Exception {
    class TargetsCollector extends LoggerAdapter {
      final List<String> myTargets = new ArrayList<String>();

      @Override
      public void blockStart(@NotNull final String targetName) {
        myTargets.add(targetName.split(" ")[1]);
      }
    }
    final TargetsCollector tc = new TargetsCollector();
    final List<String> targets = Arrays.asList("all", "clean");
    final AtomicReference<List<String>> makeTasks = new AtomicReference<List<String>>(targets);
    final MakeOutputListener mll = new MakeOutputListener(tc, makeTasks, null, true);

    final File workingDir = new File("");
    mll.processStarted("make", workingDir);
    for (final String t : targets) {
      mll.onStandardOutput(generateEnterMessage(workingDir.getAbsolutePath(), -1));
      mll.onStandardOutput(generateLeaveMessage(workingDir.getAbsolutePath(), -1));
    }
    mll.processFinished(0);

    Assert.assertEquals(tc.myTargets, targets);
  }

}