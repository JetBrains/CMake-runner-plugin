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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="bean" class="jetbrains.buildServer.makerunner.server.MakeRunnerBean"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  Makefile:
  <c:choose>
    <c:when test="${empty propertiesBean.properties['use-custom-build-file'] or propertiesBean.properties['use-custom-build-file'] == false}">
      <props:displayValue name="build-file-path" emptyValue="<Using GNU Make default makefile>"/>
    </c:when>
    <c:when test="${propertiesBean.properties['use-custom-build-file'] == true}">
      <props:displayValue name="custom-build-file-content" emptyValue="<empty>" showInPopup="true"
                          popupTitle="Makefile content" popupLinkText="view Makefile content"/>
    </c:when>
  </c:choose>
</div>

<props:viewWorkingDirectory/>

<div class="parameter">
  Make tasks: <strong><props:displayValue name="${bean.tasksKey}" emptyValue="default"/></strong>
</div>

<div class="parameter">
  Additional Make command line parameters: <strong><props:displayValue name="${bean.additionalCmdParamsKey}"
                                                                       emptyValue="not specified"/></strong>
</div>

<div class="parameter">
  Launching Parameters:
  <div class="nestedParameter">
    <ul style="list-style: none; padding-left: 0; margin-left: 0; margin-top: 0.1em; margin-bottom: 0.1em;">
      <li>Keep-going after first error (collect more errors): <strong><props:displayCheckboxValue
              name="${bean.keepGoingKey}"/></strong></li>
    </ul>
  </div>
</div>
