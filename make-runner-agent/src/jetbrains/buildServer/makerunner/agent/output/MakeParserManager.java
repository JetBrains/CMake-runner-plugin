/*
 * Copyright 2000-2011 JetBrains s.r.o.
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

package jetbrains.buildServer.makerunner.agent.output;

import jetbrains.buildServer.makerunner.agent.util.Logger;
import jetbrains.buildServer.makerunner.agent.util.Manager;
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
public class MakeParserManager extends Manager {
  @NotNull
  private final Stack<Target> myTargetsStack = new Stack<Target>();
  @NotNull
  private final AtomicReference<File> myWorkingDirectory;
  @NotNull
  private final AtomicReference<List<String>> myMainMakeTasks;
  private ListIterator<String> myMainMakeTasksIterator;


  public MakeParserManager(@NotNull final Logger logger, @NotNull final AtomicReference<File> workingDirectory, @NotNull final AtomicReference<List<String>> mainMakeTasks) {
    super(logger);
    this.myWorkingDirectory = workingDirectory;
    this.myMainMakeTasks = mainMakeTasks;
  }

  private boolean hasTargets() {
    return !myTargetsStack.empty();
  }

  private boolean isLastTargetDirectory(@NotNull final String dir) {
    return hasTargets() && new File(myTargetsStack.peek().getDirectory()).equals(new File(dir));
  }

  private boolean isLastTargetLevel(final int level) {
    return hasTargets() && myTargetsStack.peek().getLevel() == level;
  }

  boolean isWorkingDirectory(@Nullable final String dir) {
    return myWorkingDirectory.get().equals(new File(dir));
  }

  void directoryStart(final String directory, int level) {
    if (!hasTargets()) {
      // Starting new Main task
      final String targetName = getNextMainTarget();
      final String targetDescription = targetName != null ? "Making " + targetName + " in ." : "Making unknown target in .";
      myTargetsStack.push(new Target(myWorkingDirectory.get().getAbsolutePath(), targetDescription, 0));
      getLogger().blockStart(targetDescription);

      if (isWorkingDirectory(directory)) {
        for (final String line : toPrintAfterDirectoryStart) {
          getLogger().message(line);
        }
        toPrintAfterDirectoryStart.clear();
        return;
      }
    }
    final String relativePath = jetbrains.buildServer.util.FileUtil.getRelativePath(new File(getPrevTargetDirectory()), new File(directory));
    if (!isWorkingDirectory(directory) && !".".equals(relativePath)) {
      if (level == -1) level = getPrevTargetLevel() + 1;
      final String description = toPrintAfterDirectoryStart.isEmpty() ? relativePath : toPrintAfterDirectoryStart.peek();
      @SuppressWarnings({"ConstantConditions"})
      final Target mt = new Target(directory, description, level);
      myTargetsStack.push(mt);
      getLogger().blockStart(description);
      for (final String line : toPrintAfterDirectoryStart) {
        getLogger().message(line);
      }
      toPrintAfterDirectoryStart.clear();
    }
  }

  void directoryFinish(final String directory, final int level) {
    if (!isLastTargetDirectory(directory)) return;
    if (isLastTargetLevel(level)) {
      final Target mt = myTargetsStack.pop();
      getLogger().blockFinish(mt.getDescription());
    } else {
      checkMainTaskFinished(directory);
    }
  }

  int getPrevTargetLevel() {
    return hasTargets() ? myTargetsStack.peek().getLevel() : -1;
  }

  private void checkMainTaskFinished(@Nullable final String dirName) {
    if (isWorkingDirectory(dirName) && isLastTargetDirectory(myWorkingDirectory.get().getAbsolutePath())) {
      getLogger().blockFinish(myTargetsStack.pop().getDescription());
    }
  }

  private boolean initMainTasksIterator() {
    if (myMainMakeTasksIterator != null) return true;
    if (myMainMakeTasks.get() == null) return false;
    myMainMakeTasksIterator = myMainMakeTasks.get().listIterator();
    return true;
  }

  private final Queue<String> toPrintAfterDirectoryStart = new ArrayDeque<String>();

  public void finishAllTargets() {
    // Closing all opened targets in exit.
    while (!myTargetsStack.isEmpty()) {
      final Target mt = myTargetsStack.pop();
      getLogger().blockFinish(mt.getDescription());
//      checkMainTaskFinished(mt.getDirectory());
    }
  }

  @NonNls
  private static final String MAKING_IN = ".*[Mm]aking (\\S+) in (\\S+)";
  @NonNls
  private static final String DIRECTORY_LEAVE = ".*[Mm]ake[^\\[\\]]*(?:\\[(\\d+)\\])?: Leaving directory `(.*)'";
  @NonNls
  private static final String DIRECTORY_ENTER = ".*[Mm]ake[^\\[\\]]*(?:\\[(\\d+)\\])?: Entering directory `(.*)'";

  public static final Pattern MAKING_IN_PATTERN = Pattern.compile(MAKING_IN);
  public static final Pattern DIRECTORY_LEAVE_PATTERN = Pattern.compile(DIRECTORY_LEAVE);
  public static final Pattern DIRECTORY_ENTER_PATTERN = Pattern.compile(DIRECTORY_ENTER);

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
      getLogger().message(text);
      return true;
    }
    m = DIRECTORY_LEAVE_PATTERN.matcher(text);
    if (m.find()) {
      getLogger().message(text);
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
  public String getPrevTargetDirectory() {
    return hasTargets() ? myTargetsStack.peek().getDirectory() : myWorkingDirectory.get().getAbsolutePath();
  }

  @Nullable
  private String getNextMainTarget() {
    return initMainTasksIterator() && myMainMakeTasksIterator.hasNext() ? myMainMakeTasksIterator.next() : null;
  }
}
