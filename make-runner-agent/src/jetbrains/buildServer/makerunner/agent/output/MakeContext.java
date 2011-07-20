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
import jetbrains.buildServer.makerunner.agent.util.parser.Context;
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
public class MakeContext extends Manager implements Context {
  @NotNull
  private final Stack<MakeTarget> myTargetsStack = new Stack<MakeTarget>();
  @NotNull
  private final AtomicReference<File> myWorkingDirectory;
  @NotNull
  private final AtomicReference<List<String>> myMainMakeTasks;
  private ListIterator<String> myMainMakeTasksIterator;


  public MakeContext(@NotNull final Logger logger, @NotNull final AtomicReference<File> workingDirectory, @NotNull final AtomicReference<List<String>> mainMakeTasks) {
    super(logger);
    this.myWorkingDirectory = workingDirectory;
    this.myMainMakeTasks = mainMakeTasks;
  }

  public boolean hasTargets() {
    return !myTargetsStack.empty();
  }

  public boolean isLastTargetDirectory(@NotNull final String dir) {
    return hasTargets() && myTargetsStack.peek().getDirectory().equals(dir);
  }

  public boolean isLastTargetName(@NotNull final String name) {
    return hasTargets() && myTargetsStack.peek().getName().equals(name);
  }

  @NotNull
  public Logger getLogger() {
    return myLogger;
  }

  public boolean isWorkingDirectory(@Nullable final String dir) {
    return myWorkingDirectory.get().getName().equals(dir);
  }

  public void checkMainTaskFinished(@Nullable final String dirName) {
    if (isWorkingDirectory(dirName) && isLastTargetDirectory(".")) {
      myLogger.blockFinish(myTargetsStack.pop().getDescription());
    }
  }

  public void startNextMainTask() {
    if (myMainMakeTasks.get() == null) return;
    if (myMainMakeTasksIterator == null) {
      myMainMakeTasksIterator = myMainMakeTasks.get().listIterator();
    }
    if (myMainMakeTasksIterator.hasNext()) {
      final String targetName = myMainMakeTasksIterator.next();
      final String targetDescription = "Making " + targetName + " in /";
      myTargetsStack.push(new MakeTarget(targetName, ".", targetDescription));
      myLogger.blockStart(targetDescription);
    }
  }

  public void targetStart(final String name, final String directory, final String description) {
    checkMainTaskExist();
    final MakeTarget mt = new MakeTarget(name, directory, description);
    myTargetsStack.push(mt);
    myLogger.blockStart(description);
  }

  public void targetFinish() {
    final MakeTarget mt = myTargetsStack.pop();
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

  public static final String MAKING_IN = "Making (\\S+) in (\\S+)";
  public static final Pattern MAKING_IN_PATTERN = Pattern.compile(MAKING_IN);
  public static final String DIRECTORY_LEAVE = ".*make.*: Leaving directory `(.*)'";
  public static final Pattern DIRECTORY_LEAVE_PATTERN = Pattern.compile(DIRECTORY_LEAVE);

  @Override
  protected void specialParse(@NotNull final String line) {

    Matcher m = MAKING_IN_PATTERN.matcher(line);
    if (m.find()) {
      final String directory = m.group(2);
      final MakeTarget myTarget = new MakeTarget(line, directory, line);
      if (!directory.equals(".")) {
        targetStart(line, directory, line);
      }
      myLogger.info(line);
    } else {
      m = DIRECTORY_LEAVE_PATTERN.matcher(line);
      if (m.find()) {
        myLogger.info(line);
        final String dirShortName = new File(m.group(1)).getName();
        if (isLastTargetDirectory(dirShortName)) {
          targetFinish();
        }
        checkMainTaskFinished(dirShortName);
      } else {
        myLogger.info(line);
      }
    }

  }

}
