
package jetbrains.buildServer.cmakerunner.agent.output;

import jetbrains.buildServer.agent.runner.ProcessListenerAdapter;
import jetbrains.buildServer.cmakerunner.regexparser.Logger;
import jetbrains.buildServer.cmakerunner.regexparser.ParserLoader;
import jetbrains.buildServer.cmakerunner.regexparser.ParserManager;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import jetbrains.buildServer.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexParsersBasedOutputListener extends ProcessListenerAdapter {
  @NotNull
  private final List<RegexParser> myParsers;
  @NotNull
  protected final ParserManager myManager;

  public RegexParsersBasedOutputListener(@NotNull final ParserManager manager, @NotNull final String... parserConfigResourceNames) {
    this.myManager = manager;
    final ArrayList<RegexParser> parsers = new ArrayList<RegexParser>(parserConfigResourceNames.length);
    for (final String resourceName : parserConfigResourceNames) {
      final RegexParser regexParser = ParserLoader.loadParser(resourceName, this.getClass());
      if (regexParser != null) {
        parsers.add(regexParser);
      }
    }
    parsers.trimToSize();
    myParsers = parsers;
  }

  public RegexParsersBasedOutputListener(@NotNull final Logger logger, @NotNull final String... parserConfigResourceNames) {
    this(new ParserManager(logger), parserConfigResourceNames);
  }

  @Override
  public void onStandardOutput(@NotNull final String text) {
    for (final RegexParser parser : myParsers) {
      if (parser.processLine(text, myManager)) return;
    }
    myManager.getLogger().message(text);
  }

  @Override
  public void onErrorOutput(@NotNull final String text) {
    for (final RegexParser parser : myParsers) {
      if (parser.processLine(text, myManager)) return;
    }
    myManager.getLogger().warning(text);
  }

  @NotNull
  public List<RegexParser> getParsers() {
    return myParsers;
  }

  @NotNull
  public ParserManager getManager() {
    return myManager;
  }

  protected void addParserFromFile(@NotNull final File file) {
    if (file.exists() && file.isFile()) {
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(file);
        final RegexParser regexParser = ParserLoader.loadParser(fis);
        if (regexParser != null) {
          myManager.getLogger().message("Using custom messages parser from file: " + file.getAbsolutePath());
          myParsers.add(regexParser);
        }
      } catch (final FileNotFoundException ignored) {
      } finally {
        FileUtil.close(fis);
      }
    }
  }
}