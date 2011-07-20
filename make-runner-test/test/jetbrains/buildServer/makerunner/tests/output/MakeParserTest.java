///*
// * Copyright 2000-2011 JetBrains s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
package jetbrains.buildServer.makerunner.tests.output;

import jetbrains.buildServer.makerunner.agent.output.MakeParserFactory;
import jetbrains.buildServer.makerunner.agent.util.Logger;
import jetbrains.buildServer.makerunner.agent.util.LoggerAdapter;
import jetbrains.buildServer.makerunner.agent.util.parser.Context;
import jetbrains.buildServer.makerunner.agent.util.parser.Parser;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserTest extends TestCase {
  private static final MakeParserFactory MAKE_PARSER_FACTORY = new MakeParserFactory();
  //  private static final String OUTPUT_SAMPLES_PATH = "make-runner-test/src/test/resources/output-samples";
//  public static final FileFilter TXT_FILES_FILTER = new FileFilter() {
//    public boolean accept(final File file) {
//      return file.isFile() && file.getName().endsWith(".txt");
//    }
//  };
//
//  public void testErrorRecipeMessageParsing() throws Exception {
//    final ParsedMessage message = MakeOutputParser.tryParseRecipeErrorMessage("make[1]: *** [target] error1");
//    assertTrue(message instanceof RecipeError);
//    final RecipeError re = (RecipeError) message;
//    assertTrue(re.isImportant());
//    assertEquals("target", re.getTargetName());
//    assertEquals("error1", re.getDescription());
//  }
//
//  @NotNull
//  public Collection<File> getOutputSamples() {
//    final File[] files = new File(OUTPUT_SAMPLES_PATH).listFiles(TXT_FILES_FILTER);
//    return files == null ? Collections.<File>emptyList() : Arrays.asList(files);
//  }
//
//  /**
//   * @param name file name without extension
//   * @return file
//   */
//  @NotNull
//  public File getOutputSample(@NotNull final String name) {
//    return new File(OUTPUT_SAMPLES_PATH, name + ".txt");
//  }
//
//  abstract class TestingParsingFunctor {
//    abstract public boolean checkParseValid(@NotNull final String str);
//
//    public void checkParseValid(@NotNull final Collection<String> strings) {
//      for (final String string : strings) {
//        assertTrue("Cannot parse" + string, checkParseValid(string));
//      }
//    }
//
//    public void checkParseValidFromFile(@NotNull final String fileName) throws IOException {
//      final File file = getOutputSample(fileName);
//      assertTrue(file.exists());
//      checkParseValid(FileUtil.readFile(file));
//    }
//  }
//
//  public void testTestParsingNonErrors() throws Exception {
//    new TestingParsingFunctor() {
//      @Override
//      public boolean checkParseValid(@NotNull final String str) {
//        return MakeOutputParser.tryParseEnterLeaveDirectoryMessage(str) != null;
//      }
//    }.checkParseValidFromFile("enter-leave-msgs");
//
//    new TestingParsingFunctor() {
//      @Override
//      public boolean checkParseValid(@NotNull final String str) {
//        return MakeOutputParser.tryParseStartingTargetMessage(str) != null;
//      }
//    }.checkParseValidFromFile("starting-target-msgs");
//
//    new TestingParsingFunctor() {
//      @Override
//      public boolean checkParseValid(@NotNull final String str) {
//        return MakeOutputParser.tryParseMustRemakeMessage(str) != null;
//      }
//    }.checkParseValidFromFile("target-mst-remake-msgs");
//
//    new TestingParsingFunctor() {
//      @Override
//      public boolean checkParseValid(@NotNull final String str) {
//        return MakeOutputParser.tryParseTargetRemadeMessage(str) != null;
//      }
//    }.checkParseValidFromFile("target-remade-msgs");
//  }
//
//  public void testTestParsingErrorsMessages() throws Exception {
//    new TestingParsingFunctor() {
//      @Override
//      public boolean checkParseValid(@NotNull final String str) {
//        return MakeOutputParser.tryParseRecipeErrorMessage(str) != null;
//      }
//    }.checkParseValidFromFile("recipe-error-msgs");
//  }
//
//
//  public void testParseEnterLeaveGenerated() throws Exception {
//    assertNotNull(MakeOutputParser.tryParseEnterLeaveDirectoryMessage(generateEnterMessage("/some/dir", 1)));
//    assertNotNull(MakeOutputParser.tryParseEnterLeaveDirectoryMessage(generateLeaveMessage("/some/dir", 1)));
//  }
//
//  public void testParsingRecipeErrorArguments() throws Exception {
//    RecipeError re;
//    ParsedMessage m;
//
//    {
//      m = MakeOutputParser.parse(" make[2]: *** [joe.1] Error 127");
//      assertTrue(m instanceof RecipeError);
//      re = (RecipeError) m;
//      assertTrue(re.isImportant());
//      assertEquals("joe.1", re.getTargetName());
//      assertEquals("Error 127", re.getDescription());
//    }
//    {
//
//      m = MakeOutputParser.parse("make[1]: *** [all-recursive] Error 1");
//      assertTrue(m instanceof RecipeError);
//      re = (RecipeError) m;
//      assertTrue(re.isImportant());
//      assertEquals("all-recursive", re.getTargetName());
//      assertEquals("Error 1", re.getDescription());
//    }
//    {
//      m = MakeOutputParser.parse("  make: [all-recursive] Error 1");
//      assertTrue(m instanceof RecipeError);
//      re = (RecipeError) m;
//      assertFalse(re.isImportant());
//      assertEquals("all-recursive", re.getTargetName());
//      assertEquals("Error 1", re.getDescription());
//    }
//  }

//  private static final String[] GMAKE_ERROR_STREAM0 = {
//		// Infos
//		"make: [Hello.o] Error 1 (ignored)",
//		"make[2]: [all] Error 2 (ignored)",
//		// Warnings
//		"make: [Hello.o] Error 1",
//		"make: Circular .folder/file.h <- .folder/file2.h dependency dropped.",
//		"make[1]: Circular folder/file.h <- Makefile dependency dropped.",
//		// Errors
//		"make: *** [Hello.o] Error 1",
//		"make[3]: *** [Hello.o] Error 1",
//		"make: *** No rule to make target `one', needed by `all'.  Stop.",
//		"make: *** No rule to make target `all'.  Stop.",
//		"make: *** missing.mk: No such file or directory.  Stop.",
//		"make: Target `all' not remade because of errors.",
//		// Ignored
//		"make[3]: Nothing to be done for `all'.",
//		"make[2]: `all' is up to date.",
//	};
//	private static final int GMAKE_ERROR_STREAM0_INFOS = 2;
//	private static final int GMAKE_ERROR_STREAM0_WARNINGS = 3;
//	private static final int GMAKE_ERROR_STREAM0_ERRORS = 6;
//
//	private static final String[] GMAKE_ERROR_STREAM1 = {
//		// Warning
//		"GNUmakefile:12: warning: overriding commands for target `target'",
//		"Makefile1:10: include.mk: No such file or directory",
//		// Errors
//		"Makefile2:10: *** missing separator.  Stop.",
//		"Makefile3:10: *** missing separator (did you mean TAB instead of 8 spaces?).  Stop.",
//		"Makefile4:10: *** commands commence before first target. Stop.",
//		"Makefile5:10: *** Recursive variable 'VAR' references itself (eventually). Stop.",
//		"Makefile6:10: *** target pattern contains no `%'.  Stop.",
//		// Ignored. Do not intercept compiler warnings
//		"mytest.cpp:19: warning: unused variable 'i'",
//		"hello.c:14:17: error: foo.h: No such file or directory",
//	};
//	private static final int GMAKE_ERROR_STREAM1_WARNINGS = 2;
//	private static final int GMAKE_ERROR_STREAM1_ERRORS = 5;
//	private static final String[] GMAKE_ERROR_STREAM1_FILENAMES = {"GNUmakefile", "Makefile1",
//		"Makefile2", "Makefile3", "Makefile4", "Makefile5", "Makefile6"};
//
//	private static final String[] GMAKE_ERROR_STREAM2 = {
//		// Errors
//		"gmake[3]: *** [Hello.o] Error 1",
//		"make-381.exe: *** [Hello.o] Error 1",
//		"gmake381: Target `all' not remade because of errors.",
//	};
//	private static final int GMAKE_ERROR_STREAM2_WARNINGS = 0;
//	private static final int GMAKE_ERROR_STREAM2_ERRORS = 3;
//
//	public MakeErrorParserTests() {
//		super();
//	}
//
//	public static Test suite() {
//		TestSuite suite = new TestSuite(MakeErrorParserTests.class);
//		return suite;
//	}
//
//	public void testGmakeSanity() throws Exception {
//		assertNotNull(ErrorParserManager.getErrorParserCopy(GMAKE_ERROR_PARSER_ID));
//	}
//
//	public void testGmakeMessages0() throws IOException {
//		runParserTest(GMAKE_ERROR_STREAM0, GMAKE_ERROR_STREAM0_ERRORS, GMAKE_ERROR_STREAM0_WARNINGS, GMAKE_ERROR_STREAM0_INFOS,
//				null, null, new String[]{GMAKE_ERROR_PARSER_ID});
//	}
//
//	public void testGMakeMessages1() throws IOException {
//		runParserTest(GMAKE_ERROR_STREAM1, GMAKE_ERROR_STREAM1_ERRORS, GMAKE_ERROR_STREAM1_WARNINGS,
//				GMAKE_ERROR_STREAM1_FILENAMES, null, new String[]{GMAKE_ERROR_PARSER_ID});
//	}
//
//	public void testGmakeMessages2() throws IOException {
//		runParserTest(GMAKE_ERROR_STREAM2, GMAKE_ERROR_STREAM2_ERRORS, GMAKE_ERROR_STREAM2_WARNINGS,
//				null, null, new String[]{GMAKE_ERROR_PARSER_ID});
//	}


  class CounterLogger extends LoggerAdapter {
    int message, error, warning, debug, blockStart, blockFinish, info;

    @Override
    public void message(@NotNull final String message1) {
      message++;
    }

    @Override
    public void error(@NotNull final String message) {
      error++;
    }

    @Override
    public void warning(@NotNull final String message) {
      warning++;
    }

    @Override
    public void debug(@NotNull final String message) {
      debug++;
    }

    @Override
    public void blockStart(@NotNull final String name) {
      blockStart++;
    }

    @Override
    public void blockFinish(@NotNull final String name) {
      blockFinish++;
    }

    @Override
    public void info(@NotNull final String message) {
      info++;
    }
  }

  private static final String[] GMAKE_ERROR_STREAM0 = {
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
  private static final int GMAKE_ERROR_STREAM0_MESSAGES = 2;
  private static final int GMAKE_ERROR_STREAM0_INFOS = 2;
  private static final int GMAKE_ERROR_STREAM0_WARNINGS = 3;
  private static final int GMAKE_ERROR_STREAM0_ERRORS = 6;

  private static final String[] GMAKE_ERROR_STREAM1 = {
          // Warning
          "GNUmakefile:12: warning: overriding commands for target `target'",
          "Makefile1:10: include.mk: No such file or directory",
          // Errors
          "Makefile2:10: *** missing separator.  Stop.",
          "Makefile3:10: *** missing separator (did you mean TAB instead of 8 spaces?).  Stop.",
          "Makefile4:10: *** commands commence before first target. Stop.",
          "Makefile5:10: *** Recursive variable 'VAR' references itself (eventually). Stop.",
          "Makefile6:10: *** target pattern contains no `%'.  Stop.",
          // Ignored. Do not intercept compiler warnings
          "mytest.cpp:19: warning: unused variable 'i'",
          "hello.c:14:17: error: foo.h: No such file or directory",
  };
  private static final int GMAKE_ERROR_STREAM1_MESSAGES = 2;
  private static final int GMAKE_ERROR_STREAM1_WARNINGS = 2;
  private static final int GMAKE_ERROR_STREAM1_ERRORS = 5;

  private static final String[] GMAKE_ERROR_STREAM2 = {
          // Errors
          "gmake[3]: *** [Hello.o] Error 1",
          "make-381.exe: *** [Hello.o] Error 1",
          "gmake381: Target `all' not remade because of errors.",
  };
  private static final int GMAKE_ERROR_STREAM2_WARNINGS = 0;
  private static final int GMAKE_ERROR_STREAM2_ERRORS = 3;

  public void testPack0() throws Exception {
    runParserMessagesCheck(GMAKE_ERROR_STREAM0, GMAKE_ERROR_STREAM0_ERRORS, GMAKE_ERROR_STREAM0_WARNINGS, GMAKE_ERROR_STREAM0_INFOS, GMAKE_ERROR_STREAM0_MESSAGES);
  }

  public void testPack1() throws Exception {
    runParserMessagesCheck(GMAKE_ERROR_STREAM1, GMAKE_ERROR_STREAM1_ERRORS, GMAKE_ERROR_STREAM1_WARNINGS, null, GMAKE_ERROR_STREAM1_MESSAGES);
  }

  public void testPack2() throws Exception {
    runParserMessagesCheck(GMAKE_ERROR_STREAM2, GMAKE_ERROR_STREAM2_ERRORS, GMAKE_ERROR_STREAM2_WARNINGS, null, null);
  }

  void runParserMessagesCheck(@NotNull final String[] lines, @Nullable final Integer estimateErrors, @Nullable final Integer estimateWarnings, @Nullable final Integer estimateInfo, @Nullable final Integer estimateMessages) {
    final Parser parser = MAKE_PARSER_FACTORY.createParser();
    final CounterLogger logger = new CounterLogger();
    final Context context = new Context() {
      public Logger getLogger() {
        return logger;
      }
    };
    for (final String line : lines) {
      parser.parse(line).apply(context);
    }
    if (estimateErrors != null) {
      assertEquals("Errors count:", estimateErrors.intValue(), logger.error);
    }
    if (estimateWarnings != null) {
      assertEquals("Warnings count:", estimateWarnings.intValue(), logger.warning);
    }
    if (estimateInfo != null) {
      assertEquals("Info count:", estimateInfo.intValue(), logger.info);
    }
    if (estimateMessages != null) {
      assertEquals("Messages count:", estimateMessages.intValue(), logger.message);
    }
  }

//  public void testParse1() throws Exception {
//    Parser parser = new MakeParserFactory().createParser();
//    ParsedMessage message = parser.parse("make[2]: *** [joe.1] Error 127");
//    assertTrue(message instanceof RecipeTargetMessage);
//    RecipeTargetMessage pm = (RecipeTargetMessage) message;
//    assertEquals(PrintMessage.PrintType.ERROR, pm.getPrintType());
//    final CounterLogger logger = new CounterLogger();
//    pm.apply(new Context() {
//      public Logger getLogger() {
//        return logger;
//      }
//    });
//    assertEquals(1, logger.error);
//    assertEquals("joe.1", pm.getTargetName());
//    assertEquals("Error 127", pm.getPrintingLine());
//  }
//
//  public void testParse2() throws Exception {
//    Parser parser = new MakeParserFactory().createParser();
//    ParsedMessage message = parser.parse("No rule to make target `clean'.");
//    assertTrue(message instanceof RecipeTargetMessage);
//    RecipeTargetMessage pm = (RecipeTargetMessage) message;
//    assertEquals(PrintMessage.PrintType.ERROR, pm.getPrintType());
//    final CounterLogger logger = new CounterLogger();
//    pm.apply(new Context() {
//      public Logger getLogger() {
//        return logger;
//      }
//    });
//    assertEquals(1, logger.error);
//    assertEquals("clean", pm.getTargetName());
//    assertEquals("No rule to make target `clean'.", pm.getPrintingLine());
//  }
}
