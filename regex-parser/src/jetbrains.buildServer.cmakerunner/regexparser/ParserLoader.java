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

package jetbrains.buildServer.cmakerunner.regexparser;

import jetbrains.buildServer.util.StringUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Vladislav.Rassokhin
 */
public class ParserLoader {
  @NotNull
  private static final Logger LOG = Logger.getLogger(ParserLoader.class);

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
    } catch (IOException e) {
      LOG.warn("Reading configuration fail: " + StringUtil.stackTrace(e));
    }
    return parser;
  }
}
