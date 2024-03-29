

package jetbrains.buildServer.cmakerunner.tests.agent.output;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.regexparser.ParserManager;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserTest extends BaseTestCase {

  private RegexParser myParser;

  private static final String[] MAKE_OUTPUT0 = {
          // Infos
          "make: [Hello.o] Error 1 (ignored)",
          "make[2]: [all] Error 2 (ignored)",
          // Warnings
          "make: [Hello.o] Error 1",
          "make: Circular .folder/file.h <- .folder/file2.h dependency dropped.",
          "make[1]: Circular folder/file.h <- Makefile dependency dropped.",
          // Errors
          "make: *** [Hello.o] Error 1",
          "make[3]: *** [Hello.o] Error 1",
          "make: *** No rule to make target `one', needed by `all'.  Stop.",
          "make: *** No rule to make target `all'.  Stop.",
          "make: *** missing.mk: No such file or directory.  Stop.",
          "make: Target `all' not remade because of errors.",
          // Ignored
          "make[3]: Nothing to be done for `all'.",
          "make[2]: `all' is up to date.",
  };
  private static final int MAKE_OUTPUT0_MESSAGES = 4;
  private static final int MAKE_OUTPUT0_WARNINGS = 3;
  private static final int MAKE_OUTPUT0_ERRORS = 6;
  private static final int MAKE_OUTPUT0_SPECIAL = 0;

  private static final String[] MAKE_OUTPUT1 = {
          // Warning
          "GNUmakefile:12: warning: overriding commands for target `target'",
          "Makefile1:10: include.mk: No such file or directory",
          "mytest.cpp:19: warning: unused variable 'i'",
          // Errors
          "Makefile2:10: *** missing separator.  Stop.",
          "Makefile3:10: *** missing separator (did you mean TAB instead of 8 spaces?).  Stop.",
          "Makefile4:10: *** commands commence before first target. Stop.",
          "Makefile5:10: *** Recursive variable 'VAR' references itself (eventually). Stop.",
          "Makefile6:10: *** target pattern contains no `%'.  Stop.",
          // Ignored. Do not intercept compiler warnings
          "hello.c:14:17: error: foo.h: No such file or directory",
  };
  private static final int MAKE_OUTPUT1_MESSAGES = 1;
  private static final int MAKE_OUTPUT1_WARNINGS = 3;
  private static final int MAKE_OUTPUT1_ERRORS = 5;

  private static final String[] MAKE_OUTPUT2 = {
          // Errors
          "gmake[3]: *** [Hello.o] Error 1",
          "make-381.exe: *** [Hello.o] Error 1",
          "gmake381: Target `all' not remade because of errors.",
  };
  private static final int MAKE_OUTPUT2_WARNINGS = 0;
  private static final int MAKE_OUTPUT2_ERRORS = 3;

  @Test
  public void testPack0() throws Exception {
    runParserMessagesCheck(MAKE_OUTPUT0, MAKE_OUTPUT0_ERRORS, MAKE_OUTPUT0_WARNINGS, MAKE_OUTPUT0_MESSAGES, MAKE_OUTPUT0_SPECIAL);
  }

  @Test
  public void testPack1() throws Exception {
    runParserMessagesCheck(MAKE_OUTPUT1, MAKE_OUTPUT1_ERRORS, MAKE_OUTPUT1_WARNINGS, MAKE_OUTPUT1_MESSAGES, null);
  }

  @Test
  public void testPack2() throws Exception {
    runParserMessagesCheck(MAKE_OUTPUT2, MAKE_OUTPUT2_ERRORS, MAKE_OUTPUT2_WARNINGS, null, null);
  }


  @Override
  @BeforeClass
  public void setUpClass() throws Exception {
    final InputStream parserConfigStream = this.getClass().getResourceAsStream("/make-parser.xml");
    Assert.assertNotNull(parserConfigStream);

    myParser = RegexParser.deserialize(parserConfigStream);
    Assert.assertNotNull(myParser);
  }

  void runParserMessagesCheck(@NotNull final String[] lines, @Nullable final Integer estimateErrors, @Nullable final Integer estimateWarnings, @Nullable final Integer estimateMessages, @Nullable final Integer estimateSpecial) {
    Assert.assertNotNull(myParser);

    final CounterLogger logger = new CounterLogger();
    final ParserManager context = new ParserManager(logger) {
      @Override
      protected boolean specialParse(@NotNull final String line) {
        ((CounterLogger) getLogger()).special();
        return true;
      }
    };

    for (final String line : lines) {
      if (!myParser.processLine(line, context)) {
        logger.message(line);
      }
    }
    if (estimateErrors != null) {
      Assert.assertEquals(logger.error, estimateErrors.intValue(), "Errors count mismatch");
    }
    if (estimateWarnings != null) {
      Assert.assertEquals(logger.warning, estimateWarnings.intValue(), "Warnings count mismatch");
    }
    if (estimateMessages != null) {
      Assert.assertEquals(logger.message, estimateMessages.intValue(), "Messages count mismatch");
    }
    if (estimateSpecial != null) {
      Assert.assertEquals(logger.special, estimateSpecial.intValue(), "Special count mismatch");
    }
  }

}