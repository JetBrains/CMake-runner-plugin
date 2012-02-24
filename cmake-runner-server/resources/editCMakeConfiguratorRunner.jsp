<%--
  ~ Copyright 2000-2011 JetBrains s.r.o.
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
<jsp:useBean id="bean" class="jetbrains.buildServer.cmakerunner.server.CMakeConfigureBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
  <th><label for="${bean.cmakeCommandKey}">CMake program path: </label></th>
  <td>
    <props:textProperty name="${bean.cmakeCommandKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter path to cmake program or leave blank for using default.</span>
  </td>
</tr>

<forms:workingDirectory/>

<tr>
  <th><label for="${bean.sourcePathKey}">Path to source (CMakeList.txt): </label></th>
  <td>
    <props:textProperty name="${bean.sourcePathKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter path to source relative to working directory or leave blank for use '.' .</span>
  </td>
</tr>

<tr>
  <th><label for="${bean.additionalParamsKey}">Additional parameters: </label></th>
  <td>
    <props:multilineProperty name="${bean.additionalParamsKey}"
                             expanded="${not empty propertiesBean.properties[bean.additionalParamsKey]}"
                             className="longField" rows="10" cols="58" linkTitle="Type additional parameters"/>
    <span class="error" id="error_${bean.additionalParamsKey}"></span>
  </td>
</tr>

<tr>
  <th><label for="${bean.generatorKey}">Generator: </label></th>
  <td id="generator">
    <c:set var="generatorValue"><c:out value="${propertiesBean.properties[bean.generatorKey]}"/></c:set>
    <input type="text" id="${bean.generatorKey}" name="prop:${bean.generatorKey}" value="${generatorValue}"/>
    <span class="smallNote" id="${bean.generatorsNames}.description">You can choose generator from list or enter some other name. Use 'Default' or leave blank for using system default generator.</span>
    <script type="text/javascript">
      jQuery(document).ready(function() {
        jQuery("#${bean.generatorKey}").autocomplete({source: ${bean.generatorsNames}});
        jQuery("#${bean.generatorKey}").placeholder();
      });
    </script>
  </td>
</tr>

<tr>
  <th><label for="${bean.buildTypeKey}">Build type: </label></th>
  <td id="buildType">
    <props:selectProperty name="${bean.buildTypeKey}">
      <c:forEach items="${bean.buildTypesSet}" var="item">
        <props:option value="${item}">${item.normalName}</props:option>
      </c:forEach>
    </props:selectProperty>
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

<l:settingsGroup title="Debuging Parameters">
  <tr>
    <th>
      <props:radioButtonProperty name="${bean.developerWarningsKey}" value="false" id="customDevWarn1"
                                 checked="${empty propertiesBean.properties[bean.developerWarningsKey] or propertiesBean.properties[bean.developerWarningsKey]=='false'}"/>
      <label for="customDevWarn1">No developer debugging information (-Wno-dev)</label>
    </th>
    <td>
    </td>
  </tr>
  <tr>
    <th>
      <props:radioButtonProperty name="${bean.developerWarningsKey}" value="true" id="customDevWarn2"/>
      <label for="customDevWarn2">Print developer debugging information (-Wdev)</label>
    </th>
    <td id="${bean.additionalDebugOptions}">
      <props:checkboxProperty name="${bean.warnUninitializedKey}"/>
      <label for="${bean.warnUninitializedKey}">Print a warning when an uninitialized variable is used.</label>
      <br/>
      <props:checkboxProperty name="${bean.warnUnusedVarsKey}"/>
      <label for="${bean.warnUnusedVarsKey}">Find variables that are declared or set, but not used.</label>
      <br/>
      <props:checkboxProperty name="${bean.noWarnUnusedCliKey}"/>
      <label for="${bean.noWarnUnusedCliKey}">Don't find variables that are declared on the command line, but not
        used.</label>
      <br/>
      <props:checkboxProperty name="${bean.printTraceKey}"/>
      <label for="${bean.printTraceKey}">Print a trace of all calls made and from where with message(send_error )
        calls.</label>
      <br/>
      <props:checkboxProperty name="${bean.debugModeKey}"/>
      <label for="${bean.debugModeKey}">Print extra stuff during the cmake run like stack traces with message(send_error
        ) calls.</label>
    </td>
  </tr>
</l:settingsGroup>


<script type="text/javascript">
  jQuery("input[name=\"prop\\:${bean.developerWarningsKey}\"]").click(function() {
    if (jQuery(this).val() != 'true') {
      jQuery('#${bean.additionalDebugOptions} > input').attr("disabled", true);
    } else {
      jQuery('#${bean.additionalDebugOptions} > input').removeAttr("disabled");
    }
  })

</script>