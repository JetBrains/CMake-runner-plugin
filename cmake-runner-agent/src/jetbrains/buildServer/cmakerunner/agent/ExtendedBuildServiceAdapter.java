

package jetbrains.buildServer.cmakerunner.agent;

import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladislav.Rassokhin
 */
public abstract class ExtendedBuildServiceAdapter extends BuildServiceAdapter {
  @NotNull
  private static final String NEW_LINES_PATTERN = "[" + System.getProperty("line.separator", "\n") + "]+";

  // Tmp files set
  @NotNull
  protected final Set<File> myFilesToDelete = new HashSet<File>();

  @Override
  public void afterProcessFinished() {
    // Remove tmp files
    for (final File file : myFilesToDelete) {
      jetbrains.buildServer.util.FileUtil.delete(file);
    }
    myFilesToDelete.clear();
  }

  // Work with arguments

  protected void addCustomArguments(@NotNull final List<String> args, @Nullable final String parameters) {
    if (StringUtil.isEmptyOrSpaces(parameters)) return;
    //noinspection ConstantConditions
    for (final String _line : parameters.split(NEW_LINES_PATTERN)) {
      final String line = _line.trim();
      if (StringUtil.isEmptyOrSpaces(line)) continue;
      args.addAll(StringUtil.splitHonorQuotes(line));
    }
  }

  protected void addVariablesToArguments(@NotNull final List<String> args, @NotNull final Map<String, String> variables) {
    for (final Map.Entry<String, String> entry : variables.entrySet()) {
      args.add(getVariableToArgument(entry.getKey(), entry.getValue()));
    }
  }

  @NotNull
  protected String getVariableToArgument(@NotNull final String name, @NotNull final String value) {
    return "-D" + name + "=" + value;
  }
}