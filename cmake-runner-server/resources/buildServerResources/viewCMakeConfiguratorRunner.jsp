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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.cmakerunner.server.CMakeConfigureBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>


<div class="parameter">
  CMake program path (CMAKE_COMMAND):
  <strong><props:displayValue name="${bean.cmakeCommandKey}" emptyValue="<default>"/></strong>
</div>
<props:viewWorkingDirectory/>

<div class="parameter">
  Path to source (CMakeList.txt):
  <strong><props:displayValue name="${bean.sourcePathKey}" emptyValue="<not specified>"/></strong>
</div>

<div class="parameter">
  Additional CMake command line parameters:
  <strong><props:displayValue name="${bean.additionalParamsKey}" emptyValue="<not specified>"/></strong>
</div>

<div class="parameter">
  Generator:
  <strong><props:displayValue name="${bean.generatorKey}" emptyValue="Default"/></strong>
</div>

<div class="parameter">
  Build type:
  <strong><props:displayValue name="${bean.buildTypeKey}" emptyValue="DEFAULT"/></strong>
</div>

<div class="parameter">
  Launching Parameters:
  <div class="nestedParameter">
    <ul style="list-style: none; padding-left: 0; margin-left: 0; margin-top: 0.1em; margin-bottom: 0.1em;">
      <li>Redirect stderr into stdout (output will be synchronized): <strong><props:displayCheckboxValue
              name="${bean.redirectStderrKey}"/></strong></li>
    </ul>
  </div>
</div>
<div class="parameter">
  Debugging Parameters:
  <div class="nestedParameter">
    <ul style="list-style: none; padding-left: 0; margin-left: 0; margin-top: 0.1em; margin-bottom: 0.1em;">
      <li>Print developer debugging information:
        <strong><props:displayCheckboxValue name="${bean.developerWarningsKey}"/></strong></li>

      <li>Print a warning when an uninitialized variable is used:
        <strong><props:displayCheckboxValue name="${bean.warnUninitializedKey}"/></strong></li>

      <li>Find variables that are declared or set, but not used:
        <strong><props:displayCheckboxValue name="${bean.warnUnusedVarsKey}"/></strong></li>

      <li>Don't find variables that are declared on the command line, but not used:
        <strong><props:displayCheckboxValue name="${bean.noWarnUnusedCliKey}"/></strong></li>

      <li>Print a trace of all calls made and from where with message(send_error ) calls:
        <strong><props:displayCheckboxValue name="${bean.printTraceKey}"/></strong></li>

      <li>Print extra stuff during the cmake run like stack traces with message(send_error ) calls:
        <strong><props:displayCheckboxValue name="${bean.debugModeKey}"/></strong></li>
    </ul>
  </div>
</div>
