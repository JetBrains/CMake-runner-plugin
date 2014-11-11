<%--
  ~ Copyright 2000-2014 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.cmakerunner.server.MakeRunnerBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

  <tr>
    <th>
      <props:radioButtonProperty name="use-custom-build-file" value="" id="customMakefile1"
                                 onclick="BS.MakeRunner.updateCustomBuildFile()"
                                 checked="${empty propertiesBean.properties['use-custom-build-file']}"/>
      <label for="customMakefile1">Path to a Makefile:</label>
    </th>
    <td>
      <props:textProperty name="build-file-path" className="longField" maxlength="256"/>
      <span class="error" id="error_build-file-path"></span>
      <span class="smallNote">Enter Makefile path if you don't want to use a default one. Specified path should be relative to the working directory.</span>
    </td>
  </tr>
  <tr>
    <th>
      <props:radioButtonProperty name="use-custom-build-file" value="true" id="customMakefile2"
                                 onclick="BS.MakeRunner.updateCustomBuildFile()"/>
      <label for="customMakefile2">Makefile content:</label>
    </th>
    <td>
      <props:multilineProperty expanded="${propertiesBean.properties['use-custom-build-file'] == true}"
                               name="custom-build-file-content" rows="10" cols="58"
                               linkTitle="Type the Makefile content"
                               onkeydown="$('custom2').checked = true;" className="longField"/>
      <span class="error" id="error_build-file"></span>
    </td>
  </tr>
  <forms:workingDirectory/>
  <tr>
    <th><label for="${bean.tasksKey}">Make tasks: </label></th>
    <td><props:textProperty name="${bean.tasksKey}" className="longField" maxlength="256"/>
      <span class="smallNote">Enter tasks names separated by space character if you don't want to use default (first non-prune) task.<br/>E.g. 'test' or 'clean test'.</span>
    </td>
  </tr>
  <tr class="advancedSetting">
    <th><label for="${bean.makeProgramPathKey}">Make program path: </label></th>
    <td><props:textProperty name="${bean.makeProgramPathKey}" className="longField" maxlength="256"/>
      <span class="smallNote">Enter path to make program or leave blank for using default 'make'.</span>
    </td>
  </tr>
  <tr class="advancedSetting">
    <th>
      <label>Other options: </label>
    </th>
    <td>
      <props:checkboxProperty name="${bean.keepGoingKey}"/>
      <label for="${bean.keepGoingKey}">Keep-going after first error</label>
      <br/>
      <props:checkboxProperty name="${bean.redirectStderrKey}"/>
      <label for="${bean.redirectStderrKey}">Synchronize stderr and stdout messages (redirect stderr into stdout)</label>
      <br/>
    </td>
  </tr>
  <tr class="advancedSetting">
    <th><label for="${bean.additionalCmdParamsKey}">Additional Make command line parameters: </label></th>
    <td><props:textProperty name="${bean.additionalCmdParamsKey}" className="longField" maxlength="256"/>
      <span class="smallNote">If isn't empty these parameters will be added to 'make' command line.</span>
    </td>
  </tr>


<script type="text/javascript">
  BS.MakeRunner = {
    updateCustomBuildFile : function() {
      var val = $('use-custom-build-file').value;
      if (val) {
        try {
          BS.MultilineProperties.show('build-file', true);
          $('build-file').focus();
        } catch(e) {
        }
      } else {
        $('build-file-path').focus();
      }
      BS.MultilineProperties.updateVisible();
    }
  };
</script>