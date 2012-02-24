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

package jetbrains.buildServer.cmakerunner.tests.regexparser;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.cmakerunner.regexparser.RegexPattern;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexPatternTest extends BaseTestCase {


  @Test
  public void testPatternConverter() throws Exception {
    final RegexPattern.PatternConverter pc = new RegexPattern.PatternConverter();

    Assert.assertTrue(pc.canConvert(Pattern.class));
    Assert.assertFalse(pc.canConvert(String.class));

    final String patternString = "(aba)+";
    final Pattern pattern = Pattern.compile(patternString);

    final String serialized = pc.toString(pattern);
    Assert.assertEquals(serialized, patternString);

    final Object parsedObject = pc.fromString(serialized);
    Assert.assertSame(Pattern.class, parsedObject.getClass());

    Assert.assertEquals(((Pattern) parsedObject).pattern(), patternString);
  }
}
