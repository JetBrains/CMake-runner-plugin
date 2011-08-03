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

package jetbrains.buildServer.makerunner.tests;

import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.Nullable;

/**
 * @author Vladislav.Rassokhin
 */
public class StringUtilMy {

  /**
   * Removes the right (end) spaces from the string.
   *
   * @param string string to trim; nulls and empty strings are allowed.
   * @return the string without right spaces, or null if null given.
   */
  public static
  @Nullable
  String trimRight(final @Nullable String string) {
    if (string == null)
      return null;

    int n = string.length();
    if (n == 0 || !Character.isWhitespace(string.charAt(n - 1)))
      return string;

    int left = n - 1;
    while (left >= 0 && Character.isWhitespace(string.charAt(left))) {
      --left;
    }

    if (left == 0)
      return StringUtil.EMPTY;

    return string.substring(0, left + 1);
  }
}
