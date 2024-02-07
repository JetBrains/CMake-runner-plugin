
package jetbrains.buildServer.cmakerunner.regexparser;

import jetbrains.buildServer.util.StringUtil;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Vladislav.Rassokhin
 */
public class ParserLoader {
  @NotNull
  private static final Logger LOG = Logger.getInstance(ParserLoader.class);

  @Nullable
  public static RegexParser loadParser(@NotNull final String configResourceName, @NotNull final Class clazz) {
    final InputStream parserConfigStream = clazz.getResourceAsStream(configResourceName);
    if (parserConfigStream == null) {
      LOG.warn("Specified parser configuration resource not found (" + configResourceName + ")");
      return null;
    }
    return loadParser(parserConfigStream);
  }

  @Nullable
  public static RegexParser loadParser(@NotNull final InputStream parserConfigStream) {
    RegexParser parser = null;
    try {
      parser = RegexParser.deserialize(parserConfigStream);
    } catch (final IOException e) {
      LOG.warn("Reading configuration fail: " + StringUtil.stackTrace(e));
    }
    return parser;
  }
}