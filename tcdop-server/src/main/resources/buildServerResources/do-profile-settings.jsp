<%@ include file="/include.jsp" %>

<%--@elvariable id="resPath" type="java.lang.String"--%>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<%@ page import="io.cyberstock.tcdop.model.DOConfigConstants" %>
<c:set var="apiKey" value="<%=DOConfigConstants.TOKEN%>"/>
<c:set var="imageId" value="<%=DOConfigConstants.IMAGE_NAME%>"/>
<c:set var="sshPrivateKey" value="<%=DOConfigConstants.SSH_PRIVATE_KEY%>"/>
<c:set var="sshPublicKey" value="<%=DOConfigConstants.SSH_PUBLIC_KEY%>"/>
<c:set var="agentConfigType" value="<%=DOConfigConstants.DO_INTEGRATION_MODE%>"/>
<c:set var="AGENT_CONFIG_TYPE" value="<%=DOConfigConstants.DO_INTEGRATION_MODE%>"/>
<c:set var="SINGLE_INSTANCE_TYPE" value="<%=DOConfigConstants.PREPARED_IMAGE_MODE_CODE%>"/>
<c:set var="MULTIPLE_DOCKER_BASED_INSTANCE_TYPE" value="<%=DOConfigConstants.DOCKER_BASED_MODE_CODE%>"/>

<c:set var="STYLES_PATH" value="<%=DOConfigConstants.STYLES_PATH%>"/>

<!-- injected by io.cyberstock.tcdop.server.integration.teamcity.web.TCDOPSettingsController -->
<%--@elvariable id="webConfig" type="java.util.Map"--%>

<script type="text/javascript">
    BS.LoadStyleSheetDynamically("<c:url value='${webConfig.get(STYLES_PATH)}'/>");
</script>

<br/>
<hr/>
<br/>
<br/>

<h2 class="noBorder section-header">Digital Ocean integration settings</h2>
<br/>
<table class="runnerFormTable">
    <tr>
        <th class="tcdop-property-label-container">Token</th>
        <td>
            <props:textProperty name="${apiKey}" id="${apiKey}" className="longField"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${apiKey}"></span>
        </td>
    </tr>
    <tr>
        <th class="tcdop-property-label-container">Agent type</th>
        <td>
            <props:selectProperty name="${AGENT_CONFIG_TYPE}" className="longField"
                                  onchange="syncCloudSelectionControlState(); return true;">
                <props:option id="${SINGLE_INSTANCE_TYPE}" value="${SINGLE_INSTANCE_TYPE}"><c:out
                        value="single instance"/></props:option>
                <props:option id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}"
                              value="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}"><c:out
                        value="docker based instances"/></props:option>
            </props:selectProperty>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${AGENT_CONFIG_TYPE}"></span>
        </td>
    </tr>

    <tr id="${SINGLE_INSTANCE_TYPE}_section">
        <th class="tcdop-property-label-container">Image Name</th>
        <td>
            <props:textProperty name="${imageId}" className="longField"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${imageId}"></span>
        </td>
    </tr>

    <tr id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section">
        <td colspan="3" class="tcdop-fatal longField tcdop-error-msg">
            Dynamic docker based configuration isn't supported yet
        </td>
    </tr>

</table>


<script type="text/javascript">

    window.syncCloudSelectionControlState = function () {
        if ($("${SINGLE_INSTANCE_TYPE}").selected) {
            BS.Util.show("${SINGLE_INSTANCE_TYPE}_section");
            BS.Util.hide("${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section");

        }
        else {
            BS.Util.hide("${SINGLE_INSTANCE_TYPE}_section");
            BS.Util.show("${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section");
        }

        BS.MultilineProperties.updateVisible();
    };

    window.syncControlState = function () {
        syncCloudSelectionControlState();
    };

    window.syncControlState();
</script>

