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
<jsp:useBean id="bean" class="jetbrains.buildServer.cmakerunner.server.CMakeRunnerBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
  <th><label for="${bean.cmakeCommandKey}">CMake program path: </label></th>
  <td>
    <props:textProperty name="${bean.cmakeCommandKey}" className="longField" maxlength="256"/>
    <span class="smallNote">Enter path to cmake program or leave blank for using default.</span>
  </td>
</tr>

<forms:workingDirectory/>

<%--<tr>--%>
<%--<th>--%>
<%--<props:radioButtonProperty name="use-custom-build-file" value="" id="customMakefile1"--%>
<%--checked="${empty propertiesBean.properties['use-custom-build-file']}"/>--%>
<%--<label for="customMakefile1">Path to a CMakeList.txt:</label>--%>
<%--</th>--%>
<%--<td>--%>
<%--<props:textProperty name="build-file-path" className="longField" maxlength="256"/>--%>
<%--<span class="error" id="error_build-file-path"></span>--%>
<%--<span class="smallNote">Enter CMakeList.txt path if you don't want to use a default one. Specified path should be relative to the checkout directory.</span>--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<th>--%>
<%--<props:radioButtonProperty name="use-custom-build-file" value="true" id="customMakefile2"--%>
<%--/>--%>
<%--<label for="customMakefile2">Makefile content:</label>--%>
<%--</th>--%>
<%--<td>--%>
<%--<props:multilineProperty expanded="${propertiesBean.properties['use-custom-build-file'] == true}"--%>
<%--name="custom-build-file-content" rows="10" cols="58" linkTitle="Type the Makefile content"--%>
<%--onkeydown="$('custom2').checked = true;" className="longField"/>--%>
<%--<span class="error" id="error_build-file"></span>--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<th><label for="${bean.tasksKey}">Make tasks: </label></th>--%>
<%--<td><props:textProperty name="${bean.tasksKey}" className="longField" maxlength="256"/>--%>
<%--<span class="smallNote">Enter tasks names separated by space character if you don't want to use default (first non-prune) task.<br/>E.g. 'test' or 'clean test'.</span>--%>
<%--</td>--%>
<%--</tr>--%>
<tr>
  <th><label for="${bean.additionalParamsKey}">Additional parameters: </label></th>
  <td><props:textProperty name="${bean.additionalParamsKey}" className="longField" maxlength="256"/>
    <span class="smallNote">If isn't empty these parameters will be added to 'cmake' command line.</span>
  </td>
</tr>

<tr>
  <th><label for="${bean.makefileGeneratorKey}">Generator: </label></th>
  <td id="generator">
    <props:selectProperty name="${bean.makefileGeneratorKey}">
      <c:forEach items="${bean.generatorsMap}" var="item">
        <props:option value="${item.value}">${item.key}</props:option>
      </c:forEach>
    </props:selectProperty>

    <c:forEach items="${bean.generatorsMap}" var="item">
      <span class="smallNote" id="${item.value}.description">${item.value.description}</span>
    </c:forEach>
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
  jQuery("input[name='prop:${bean.developerWarningsKey}']").click(function() {
    if (jQuery(this).val() != 'true') {
      jQuery('${bean.additionalDebugOptions} > input').attr("disabled", true);
    } else {
      jQuery('${bean.additionalDebugOptions} > input').removeAttr("disabled");
    }
  });
  jQuery("input[name='prop:${bean.makefileGeneratorKey}']").change(function () {
    var v = jQuery(this).val();
    alert(v);
    jQuery("#generator > .smallNote").hide();
    jQuery("#generator > .smallNote[id='" + v + ".description']").show();
  });

  jQuery("#generator > .smallNote").hide();

</script>