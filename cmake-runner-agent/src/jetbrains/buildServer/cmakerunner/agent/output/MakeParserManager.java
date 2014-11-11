/*
 * Copyright 2000-2014 JetBrains s.r.o.
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

package jetbrains.buildServer.cmakerunner.agent.output;

import jetbrains.buildServer.cmakerunner.agent.util.PathUtil;
import jetbrains.buildServer.cmakerunner.regexparser.Logger;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class MakeParserManager extends jetbrains.buildServer.cmakerunner.regexparser.ParserManager<MakeParserManager.Target> {
  @NotNull
  private final AtomicReference<String> myWorkingDirectory;
  @NotNull
  private final AtomicReference<List<String>> myMainMakeTasks;
  private ListIterator<String> myMainMakeTasksIterator;


  public MakeParserManager(@NotNull final Logger logger, @NotNull final AtomicReference<List<String>> mainMakeTasks) {
    super(logger);
    this.myWorkingDirectory = new AtomicReference<String>();
    this.myMainMakeTasks = mainMakeTasks;
  }

  private boolean hasTargets() {
    return !myBlocksStack.empty();
  }

  private boolean isLastTargetDirectory(@NotNull final String dir) {
    return hasTargets() && new File(myBlocksStack.peek().getDirectory()).equals(new File(dir));
  }

  private boolean isLastTargetLevel(final int level) {
    return hasTargets() && myBlocksStack.peek().getLevel() == level;
  }

  boolean isWorkingDirectory(@NotNull final String dir) {
    return new File(myWorkingDirectory.get()).equals(new File(PathUtil.toUnixStylePath(dir)));
  }

  void directoryStart(@NotNull String directory, int level) {
    directory = PathUtil.toUnixStylePath(directory);
    if (!hasTargets()) {
      // Starting new Main task
      final String targetName = getNextMainTarget();
      final String targetDescription = targetName != null ? "Making " + targetName + " in ." : "Making unknown target in .";
      myBlocksStack.push(new Target(myWorkingDirectory.get(), targetDescription, 0));
      myLogger.blockStart(targetDescription);

      if (isWorkingDirectory(directory)) {
        printPostponedMessages();
        return;
      }
    }
    final String relativePath = getRelativePath(getPrevTargetDirectory(), directory);
    if (!isWorkingDirectory(directory) && !StringUtil.isEmpty(relativePath)) {
      if (level == -1) level = getPrevTargetLevel() + 1;
      final String description = toPrintAfterDirectoryStart.isEmpty() ? relativePath : toPrintAfterDirectoryStart.peek();
      @SuppressWarnings({"ConstantConditions"})
      final Target mt = new Target(directory, description, level);
      myBlocksStack.push(mt);
      myLogger.blockStart(description);
      printPostponedMessages();
    } else if (isWorkingDirectory(directory)) {
      printPostponedMessages();
    }
  }

  private void printPostponedMessages() {
    for (final String line : toPrintAfterDirectoryStart) {
      myLogger.message(line);
    }
    toPrintAfterDirectoryStart.clear();
  }

  @Nullable
  private String getRelativePath(@NotNull final String base, @NotNull final String directory) {
    final File base1 = new File(PathUtil.toUnixStylePath(base));
    final File dir = new File(PathUtil.toUnixStylePath(directory));
    return jetbrains.buildServer.util.FileUtil.getRelativePath(base1, dir);
  }

  void directoryFinish(@NotNull String directory, final int level) {
    directory = PathUtil.toUnixStylePath(directory);
    if (!isLastTargetDirectory(directory)) return;
    if (isLastTargetLevel(level)) {
      final Target mt = myBlocksStack.pop();
      myLogger.blockFinish(mt.getName());
    } else {
      checkMainTaskFinished(directory, level);
    }
  }

  int getPrevTargetLevel() {
    return hasTargets() ? myBlocksStack.peek().getLevel() : -1;
  }

  private void checkMainTaskFinished(@NotNull final String dirName, final int level) {
    if (level <= 1 && isWorkingDirectory(dirName) && isLastTargetDirectory(myWorkingDirectory.get())) {
      myLogger.blockFinish(myBlocksStack.pop().getName());
    }
  }

  private boolean initMainTasksIterator() {
    if (myMainMakeTasksIterator != null) return true;
    if (myMainMakeTasks.get() == null) return false;
    myMainMakeTasksIterator = myMainMakeTasks.get().listIterator();
    return true;
  }

  @NotNull
  private final Queue<String> toPrintAfterDirectoryStart = new ArrayDeque<String>();

  public void finishAllTargets() {
    // Closing all opened targets in exit.
    while (!myBlocksStack.isEmpty()) {
      final Target mt = myBlocksStack.pop();
      myLogger.blockFinish(mt.getName());
//      checkMainTaskFinished(mt.getDirectory());
    }
  }

  @NotNull
  @NonNls
  private static final String MAKING_IN = ".*[Mm]aking (\\S+) in (\\S+)";
  @NotNull
  @NonNls
  private static final String DIRECTORY_LEAVE = ".*[Mm]ake[^\\[\\]]*(?:\\[(\\d+)\\])?: Leaving directory `(.*)'";
  @NotNull
  @NonNls
  private static final String DIRECTORY_ENTER = ".*[Mm]ake[^\\[\\]]*(?:\\[(\\d+)\\])?: Entering directory `(.*)'";

  public static final Pattern MAKING_IN_PATTERN = Pattern.compile(MAKING_IN);
  public static final Pattern DIRECTORY_LEAVE_PATTERN = Pattern.compile(DIRECTORY_LEAVE);
  public static final Pattern DIRECTORY_ENTER_PATTERN = Pattern.compile(DIRECTORY_ENTER);

  @Override
  protected Target createBlock(@NotNull final String name) {
    return new Target(name, name, myBlocksStack.size());
  }

  @Override
  protected Target createCompilationBlock(@NotNull final String name) {
    return new Target(name, name, myBlocksStack.size());
  }

  @Override
  protected boolean specialParse(@NotNull final String text) {
    Matcher m;
    m = MAKING_IN_PATTERN.matcher(text);
    if (m.find()) {
      toPrintAfterDirectoryStart.add(text);
      return true;
    }

    m = DIRECTORY_ENTER_PATTERN.matcher(text);
    if (m.find()) {
      final String dirName = m.group(2);
      final String levelStr = m.group(1);
      int level = 0;
      if (levelStr != null) {
        level = Integer.parseInt(levelStr);
      }
      directoryStart(dirName, level);
      myLogger.message(text);
      return true;
    }
    m = DIRECTORY_LEAVE_PATTERN.matcher(text);
    if (m.find()) {
      myLogger.message(text);
      final String dirName = m.group(2);
      final String levelStr = m.group(1);
      int level = 0;
      if (levelStr != null) {
        level = Integer.parseInt(levelStr);
      }
      directoryFinish(dirName, level);
      return true;
    }

    return super.specialParse(text);
  }

  @NotNull
  private String getPrevTargetDirectory() {
    return hasTargets() ? myBlocksStack.peek().getDirectory() : myWorkingDirectory.get();
  }

  @Nullable
  private String getNextMainTarget() {
    return initMainTasksIterator() && myMainMakeTasksIterator.hasNext() ? myMainMakeTasksIterator.next() : null;
  }

  public void setWorkingDirectory(@NotNull final String workingDirectory) {
    myWorkingDirectory.set(workingDirectory);
  }

  public static class Target extends jetbrains.buildServer.cmakerunner.regexparser.ParserManager.Block {
    @NotNull
    private final String myDirectory;
    private final int myLevel;

    public Target(@NotNull final String directory, @NotNull final String description, final int level) {
      super(description);
      this.myDirectory = directory;
      this.myLevel = level;
    }

    @NotNull
    public String getDirectory() {
      return myDirectory;
    }

    public int getLevel() {
      return myLevel;
    }
  }
}
