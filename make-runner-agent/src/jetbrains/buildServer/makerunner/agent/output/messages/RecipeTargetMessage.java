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

package jetbrains.buildServer.makerunner.agent.output.messages;

import jetbrains.buildServer.makerunner.agent.util.parser.Context;
import org.jetbrains.annotations.NotNull;

/**
 * @author Vladislav.Rassokhin
 */
public class RecipeTargetMessage extends PrintMessage {
  public RecipeTargetMessage(@NotNull final String description, @NotNull final PrintType printType, @NotNull final String targetName) {
    super(description, printType);
    this.myTargetName = targetName;
  }

  private final String myTargetName;

  public String getTargetName() {
    return myTargetName;
  }

  @Override
  public void apply(@NotNull final Context context) {
    super.apply(context);
//    if (getPrintType() == PrintType.ERROR) {
//      if (!(context instanceof MakeContext)) return;
//      final MakeContext mc = (MakeContext) context;
//      if (!StringUtil.isEmptyOrSpaces(myTargetName) && mc.isLastTargetName(myTargetName)) {
//        mc.targetFinish();
//      }
//    }
  }
}
