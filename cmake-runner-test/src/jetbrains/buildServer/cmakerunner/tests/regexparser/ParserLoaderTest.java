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
import jetbrains.buildServer.cmakerunner.regexparser.ParserLoader;
import jetbrains.buildServer.cmakerunner.regexparser.RegexParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;

/**
 * @author Vladislav.Rassokhin
 */
public class ParserLoaderTest extends BaseTestCase {

  public static final String TEST_PARSER_RESOURCE_NAME = "/sample-parser.xml";

  @Test
  public void testLoadFromResource() throws Exception {
    Assert.assertNotNull(this.getClass().getResourceAsStream(TEST_PARSER_RESOURCE_NAME));
    final RegexParser parser = ParserLoader.loadParser(TEST_PARSER_RESOURCE_NAME, this.getClass());
    Assert.assertNotNull(parser);
  }

  @Test
  public void testLoadFromStream() throws Exception {
    final InputStream resourceStream = this.getClass().getResourceAsStream(TEST_PARSER_RESOURCE_NAME);
    Assert.assertNotNull(resourceStream);
    final RegexParser parser = ParserLoader.loadParser(resourceStream);
    Assert.assertNotNull(parser);
  }
}
