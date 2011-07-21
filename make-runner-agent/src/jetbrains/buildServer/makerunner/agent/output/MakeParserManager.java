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
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
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
    return hasTargets() && myTargetsStack.peek().getDirectory().equals(dir);
  }

  private boolean isLastTargetLevel(final int level) {
    return hasTargets() && myTargetsStack.peek().getLevel() == level;
  }

  boolean isWorkingDirectory(@Nullable final String dir) {
    return myWorkingDirectory.get().getName().equals(dir);
  }

  private void checkMainTaskFinished(@Nullable final String dirName) {
    checkMainTaskExist();
    if (isWorkingDirectory(dirName) && isLastTargetDirectory(".")) {
      getLogger().blockFinish(myTargetsStack.pop().getDescription());
    }
  }

  void startNextMainTask() {
    if (myMainMakeTasks.get() == null) return;
    if (myMainMakeTasksIterator == null) {
      myMainMakeTasksIterator = myMainMakeTasks.get().listIterator();
    }
    if (myMainMakeTasksIterator.hasNext()) {
      final String targetName = myMainMakeTasksIterator.next();
      final String targetDescription = "Making " + targetName + " in /";
      myTargetsStack.push(new Target(targetName, ".", targetDescription, 0));
      getLogger().blockStart(targetDescription);
    }
  }

  void targetStart(final String name, final String directory, final String description, int level) {
    checkMainTaskExist();
    if (level == -1) level = getPrevTargetLevel() + 1;
    final Target mt = new Target(name, directory, description, level);
    myTargetsStack.push(mt);
    getLogger().blockStart(description);
  }

  int getPrevTargetLevel() {
    checkMainTaskExist();
    return myTargetsStack.peek().getLevel();
  }

  void targetFinish() {
    final Target mt = myTargetsStack.pop();
    getLogger().blockFinish(mt.getDescription());
    checkMainTaskFinished(mt.getDirectory());
  }

  void targetFinish(final int level) {
    if (!isLastTargetLevel(level)) return;
    final Target mt = myTargetsStack.pop();
    getLogger().blockFinish(mt.getDescription());
    checkMainTaskFinished(mt.getDirectory());
  }

  private void checkMainTaskExist() {
    // Checks that we have at least one main target
    if (!hasTargets()) {
      startNextMainTask();
    }
  }

  public void finishAllTargets() {
    // Closing all opened targets in exit.
    while (hasTargets()) {
      targetFinish();
    }
  }

  @NonNls
  private static final String MAKING_IN = ".*[Mm]aking (\\S+) in (\\S+)";
  @NonNls
  private static final String DIRECTORY_LEAVE = ".*[Mm]ake[^\\[\\]]*(?:\\[(\\d+)\\])?: Leaving directory `(.*)'";

  public static final Pattern MAKING_IN_PATTERN = Pattern.compile(MAKING_IN);
  public static final Pattern DIRECTORY_LEAVE_PATTERN = Pattern.compile(DIRECTORY_LEAVE);

  @Override
  protected boolean specialParse(@NotNull final String text) {

    Matcher m = MAKING_IN_PATTERN.matcher(text);
    if (m.find()) {
      final String directory = m.group(2);
      if (!directory.equals(".")) {
        targetStart(text, directory, text, -1);
      }
      getLogger().message(text);
      return true;
    }

    m = DIRECTORY_LEAVE_PATTERN.matcher(text);
    if (m.find()) {
      getLogger().message(text);
      final String dirShortName = new File(m.group(2)).getName();
      final String levelStr = m.group(1);
      int level = -1;
      if (levelStr != null) {
        level = Integer.parseInt(levelStr);
      }
      if (isLastTargetDirectory(dirShortName)) {
        targetFinish(level);
      }
      checkMainTaskFinished(dirShortName);
      return true;
    }

    return super.specialParse(text);
  }
}
