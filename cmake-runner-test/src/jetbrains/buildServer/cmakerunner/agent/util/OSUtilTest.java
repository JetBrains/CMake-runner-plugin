/*
 * Copyright 2000-2015 JetBrains s.r.o.
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

package jetbrains.buildServer.cmakerunner.agent.util;

import com.intellij.openapi.util.SystemInfo;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OSUtilTest {

  @Test
  public void testIsCLIExist() throws Exception {
    Assert.assertTrue(OSUtil.isCLIExist(System.getenv()), "CLI should exist");
  }

  @Test
  public void testGetCLIFullPathDoesNotThrowsException() throws Exception {
    final String path = OSUtil.getCLIFullPath(System.getenv());
    Assert.assertNotNull(path);
    Assert.assertTrue(FileUtil2.checkIfExists(path));
  }

  @DataProvider(name = "UnixOnly")
  public static Object[][] UnixOnly() {
    if (SystemInfo.isUnix) return new Object[][]{{true}};
    return new Object[][]{};
  }

  @Test(dataProvider = "UnixOnly")
  public void testGuessUnixShell(final Boolean unix) throws Exception {
    final String shell = OSUtil.guessUnixShell(System.getenv());
    Assert.assertNotNull(shell, "Shell should be guessed on unix machines");
  }
}