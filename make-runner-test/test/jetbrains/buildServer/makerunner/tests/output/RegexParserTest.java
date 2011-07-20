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

package jetbrains.buildServer.makerunner.tests.output;

import jetbrains.buildServer.makerunner.agent.output.RegexParser;
import jetbrains.buildServer.makerunner.agent.output.RegexPattern;
import jetbrains.buildServer.makerunner.agent.output.Severity;
import junit.framework.TestCase;

import java.io.InputStream;
import java.util.List;

/**
 * @author Vladislav.Rassokhin
 */
public class RegexParserTest extends TestCase {

  private static final int SAMPLE_PARSER_PATTERNS_COUNT = 3;
  private static final String[] SAMPLE_PARSER_PATTERNS_REGEX = {"error: (.*)", "warning .*", ".*"};
  private static final String[] SAMPLE_PARSER_PATTERNS_DESCRIPTION_EXPR = {"$1", "$0", "$0"};
  private static final Severity[] SAMPLE_PARSER_PATTERNS_SEVERITY = {Severity.ERROR, Severity.WARN, Severity.INFO};
  private static final boolean[] SAMPLE_PARSER_PATTERNS_EAT = {true, false, true};

  public void testLoadingSample() throws Exception {
    final InputStream parserConfigStream = this.getClass().getResourceAsStream("/sample-parser.xml");
    assertNotNull(parserConfigStream);
    final RegexParser parser = RegexParser.deserialize(parserConfigStream);
    assertNotNull(parser);
    assertEquals("Sample output parser", parser.getId());
    assertEquals("jetbrains.buildServer.makerunner.agent.output.SampleRegexParser", parser.getName());

    final List<RegexPattern> patterns = parser.getPatterns();
    assertEquals(SAMPLE_PARSER_PATTERNS_COUNT, patterns.size());

    for (int i = 0; i < SAMPLE_PARSER_PATTERNS_COUNT; i++) {
      final RegexPattern pattern = patterns.get(i);
      assertEquals(SAMPLE_PARSER_PATTERNS_REGEX[i], pattern.getPattern().pattern());
      assertEquals(SAMPLE_PARSER_PATTERNS_DESCRIPTION_EXPR[i], pattern.getDescriptionExpression());
      assertEquals(SAMPLE_PARSER_PATTERNS_SEVERITY[i], pattern.getSeverity());
      assertEquals(SAMPLE_PARSER_PATTERNS_EAT[i], pattern.getEatLine());
    }
  }

  public void testLoadingOriginal() throws Exception {
    final InputStream parserConfigStream = this.getClass().getResourceAsStream("/make-parser.xml");
    assertNotNull(parserConfigStream);
    final RegexParser parser = RegexParser.deserialize(parserConfigStream);
    assertNotNull(parser);
    assertEquals("GNU Make output parser", parser.getId());
    assertFalse(parser.getPatterns().isEmpty());
  }

}
