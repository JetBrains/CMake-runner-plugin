<%--
  ~ Copyright 2000-2013 JetBrains s.r.o.
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.cmakerunner.server.CMakeBuildBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
  <th><label for="${bean.cmakeCommandKey}">CMake program path: </label></th>
  <td>
    <props:textProperty name="${bean.cmakeCommandKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter path to cmake program or leave blank for using default.</span>
  </td>
</tr>

<forms:workingDirectory/>

<%-- Buil directory == working directory... isn't it?--%>
<%--<tr>--%>
<%--<th><label for="${bean.buildPathKey}">Path to build directory (contain CMakeCache.txt): </label></th>--%>
<%--<td>--%>
<%--<props:textProperty name="${bean.buildPathKey}" className="longField" maxlength="256"/>--%>
<%--<span class="smallNote">Enter path to source relative to working directory or leave blank for use '.' .</span>--%>
<%--</td>--%>
<%--</tr>--%>

<tr>
  <th><label for="${bean.buildTargetKey}">Build target: </label></th>
  <td><props:textProperty name="${bean.buildTargetKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter target name if you don't want to use 'default' task. (equals '--target &lt;tgt&gt;' cmd param)
      <br/>E.g. 'test' or 'clean'.</span>
  </td>
</tr>
<tr>
  <th>
    <label>Additional: </label>
  </th>
  <td>
    <props:checkboxProperty name="${bean.cleanBeforeBuildKey}"/>
    <label for="${bean.cleanBeforeBuildKey}">Clean first (Build target 'clean' first, then build.) (equals
      '--clean-first' cmd param)</label>
    <br/>
  </td>
</tr>

<tr>
  <th><label for="${bean.buildConfigurationKey}">Configuration: </label></th>
  <td><props:textProperty name="${bean.buildConfigurationKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter configuration for multi-configuration tools. (equals '--config &lt;tgt&gt;' cmd param)
      <br/>E.g. 'Release' or 'Debug'.</span>
  </td>
</tr>

<tr>
  <th><label for="${bean.nativeToolParamsKey}">Native tool parameters: </label></th>
  <td>
    <props:multilineProperty name="${bean.nativeToolParamsKey}"
                             expanded="${not empty propertiesBean.properties[bean.nativeToolParamsKey]}"
                             className="longField" rows="10" cols="58" linkTitle="Type parameters for native tool"/>
    <span class="error" id="error_${bean.nativeToolParamsKey}"></span>
  </td>
</tr>

<l:settingsGroup title="Launching Parameters">
  <tr>
    <th>
      <label>Debug: </label>
    </th>
    <td>
      <props:checkboxProperty name="${bean.redirectStderrKey}"/>
      <label for="${bean.redirectStderrKey}">Redirect stderr into stdout (output will be synchronized)</label>
      <br/>
    </td>
  </tr>
</l:settingsGroup>
