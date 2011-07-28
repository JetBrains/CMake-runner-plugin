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

import java.util.Collection;
import java.util.TreeSet;

/**
 * @author Vladislav.Rassokhin
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CMakeGenerator {
  public static Collection<String> KNOWN_GENERATORS = new TreeSet<String>();

  static {
    KNOWN_GENERATORS.add("Default");
    KNOWN_GENERATORS.add("Borland Makefiles");
    KNOWN_GENERATORS.add("CodeBlocks - MinGW Makefiles");
    KNOWN_GENERATORS.add("CodeBlocks - NMake Makefiles");
    KNOWN_GENERATORS.add("CodeBlocks - Unix Makefiles");
    KNOWN_GENERATORS.add("Eclipse CDT4 - MinGW Makefiles");
    KNOWN_GENERATORS.add("Eclipse CDT4 - NMake Makefiles");
    KNOWN_GENERATORS.add("Eclipse CDT4 - Unix Makefiles");
    KNOWN_GENERATORS.add("KDevelop3");
    KNOWN_GENERATORS.add("KDevelop3 - Unix Makefiles");
    KNOWN_GENERATORS.add("MinGW Makefiles");
    KNOWN_GENERATORS.add("MSYS Makefiles");
    KNOWN_GENERATORS.add("NMake Makefiles");
    KNOWN_GENERATORS.add("NMake Makefiles JOM");
    KNOWN_GENERATORS.add("Unix Makefiles");
    KNOWN_GENERATORS.add("Visual Studio 10");
    KNOWN_GENERATORS.add("Visual Studio 10 Win64");
    KNOWN_GENERATORS.add("Visual Studio 6");
    KNOWN_GENERATORS.add("Visual Studio 7");
    KNOWN_GENERATORS.add("Visual Studio 7 .NET 2003");
    KNOWN_GENERATORS.add("Visual Studio 8 2005");
    KNOWN_GENERATORS.add("Visual Studio 8 2005 Win64");
    KNOWN_GENERATORS.add("Visual Studio 9 2008");
    KNOWN_GENERATORS.add("Visual Studio 9 2008 Win64");
    KNOWN_GENERATORS.add("Watcom WMake");
  }
}
