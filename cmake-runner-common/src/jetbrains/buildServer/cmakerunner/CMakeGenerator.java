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

package jetbrains.buildServer.cmakerunner;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"UnusedDeclaration"})
public enum CMakeGenerator {
  DEFAULT("System default", "System default generator"),
  // TODO: add descriptions

  BORLAND_MAKEFILES("Borland Makefiles", ""),
  MSYS_MAKEFILES("MSYS Makefiles", ""),
  MINGW_MAKEFILES("MinGW Makefiles", ""),
  NMAKE_MAKEFILES("NMake Makefiles", ""),
  NMAKE_MAKEFILES_JOM("NMake Makefiles JOM", ""),
  UNIX_MAKEFILES("Unix Makefiles", ""),
  VISUAL_STUDIO_10("Visual Studio 10", ""),
  VISUAL_STUDIO_10_WIN64("Visual Studio 10 Win64", ""),
  VISUAL_STUDIO_6("Visual Studio 6", ""),
  VISUAL_STUDIO_7("Visual Studio 7", ""),
  VISUAL_STUDIO_7_NET_2003("Visual Studio 7 .NET 2003", ""),
  VISUAL_STUDIO_8_2005("Visual Studio 8 2005", ""),
  VISUAL_STUDIO_8_2005_WIN64("Visual Studio 8 2005 Win64", ""),
  VISUAL_STUDIO_9_2008("Visual Studio 9 2008", ""),
  VISUAL_STUDIO_9_2008_WIN64("Visual Studio 9 2008 Win64", ""),
  WATCOM_WMAKE("Watcom WMake", ""),
  CODEBLOCKS_MINGW_MAKEFILES("CodeBlocks - MinGW Makefiles", ""),
  CODEBLOCKS_NMAKE_MAKEFILES("CodeBlocks - NMake Makefiles", ""),
  CODEBLOCKS_UNIX_MAKEFILES("CodeBlocks - Unix Makefiles", ""),
  ECLIPSE_CDT4_MINGW_MAKEFILES("Eclipse CDT4 - MinGW Makefiles", ""),
  ECLIPSE_CDT4_NMAKE_MAKEFILES("Eclipse CDT4 - NMake Makefiles", ""),
  ECLIPSE_CDT4_UNIX_MAKEFILES("Eclipse CDT4 - Unix Makefiles", "");


  private final String myNormalName;
  private final String myDescription;
  @NotNull
  public static final SortedMap<String, CMakeGenerator> NAME_TO_GENERATOR_MAP;

  static {
    final SortedMap<String, CMakeGenerator> map = new TreeMap<String, CMakeGenerator>();
    for (final CMakeGenerator generator : values()) {
      map.put(generator.getNormalName(), generator);
    }

    NAME_TO_GENERATOR_MAP = Collections.unmodifiableSortedMap(map);
  }

  CMakeGenerator(@NotNull final String normalName, @NotNull final String description) {
    myNormalName = normalName;
    myDescription = description;
  }

  @NotNull
  public String getDescription() {
    return myDescription;
  }

  @NotNull
  public String getNormalName() {
    return myNormalName;
  }

  @Override
  public String toString() {
    return getNormalName();
  }
}
