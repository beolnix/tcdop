<%@ include file="/include.jsp" %>


<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<%@ page import="io.cyberstock.tcdop.api.DOConfigConstants" %>
<c:set var="apiKey" value="<%=DOConfigConstants.API_KEY%>"/>
<c:set var="instanceId" value="<%=DOConfigConstants.INSTANCE_ID%>"/>
<c:set var="sshPrivateKey" value="<%=DOConfigConstants.SSH_PRIVATE_KEY%>"/>
<c:set var="sshPublicKey" value="<%=DOConfigConstants.SSH_PUBLIC_KEY%>"/>
<c:set var="agentConfigType" value="<%=DOConfigConstants.AGENT_CONFIG_TYPE%>"/>

<c:set var="AGENT_CONFIG_TYPE" value="<%=DOConfigConstants.AGENT_CONFIG_TYPE%>"/>
<c:set var="SINGLE_INSTANCE_TYPE" value="<%=DOConfigConstants.SINGLE_INSTANCE_TYPE%>"/>
<c:set var="MULTIPLE_DOCKER_BASED_INSTANCE_TYPE" value="<%=DOConfigConstants.MULTIPLE_DOCKER_BASED_INSTANCE_TYPE%>"/>

<script type="text/javascript">
    BS.LoadStyleSheetDynamically("<c:url value='${resPath}do-settings.css'/>");
</script>

<h2 class="noBorder section-header">Cloud Access Information</h2>
<table>
    <tr>
        <th>API KEY</th>
        <td><props:textProperty name="${apiKey}" className="longField"/></td>
    </tr>

    <tr>
        <td colspan="2">
            <props:multilineProperty name="${sshPrivateKey}" linkTitle="Place SSH Private key here" cols="55" rows="7" />
        </td>
    </tr>

    <tr>
        <td colspan="2">
            <props:multilineProperty name="${sshPublicKey}" linkTitle="Place SSH Public key here" cols="55" rows="7" />
        </td>
    </tr>
</table>

<l:settingsGroup title="Agent Instance settings" className="advancedSetting">
    <table>
        <tr>
            <th>Agent type</th>
            <td>
                <props:selectProperty name="${AGENT_CONFIG_TYPE}" className="longField" onchange="syncCloudSelectionControlState(); return true;">
                    <props:option id="${SINGLE_INSTANCE_TYPE}" value="${SINGLE_INSTANCE_TYPE}"><c:out value="single instance"/></props:option>
                    <props:option id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}" value="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}"><c:out value="docker based instances"/></props:option>
                </props:selectProperty>
            </td>
        </tr>
        <tr id="${SINGLE_INSTANCE_TYPE}_section" class="advancedSetting">
            <td>test single</td>
            <td>single</td>
        </tr>
        <tr id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section" class="advancedSetting">
            <td>test multiple docker based</td>
            <td>docker</td>
        </tr>
    </table>

</l:settingsGroup>


<script type="text/javascript">

    window.syncCloudSelectionControlState = function() {
        if($("${SINGLE_INSTANCE_TYPE}").selected) {
            BS.Util.show("${SINGLE_INSTANCE_TYPE}_section");
            BS.Util.hide("${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section");

        }
        else {
            BS.Util.hide("${SINGLE_INSTANCE_TYPE}_section");
            BS.Util.show("${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section");
        }

        BS.MultilineProperties.updateVisible();
    };

    window.syncControlState = function() {
        syncCloudSelectionControlState();
    };

    window.syncControlState();
</script>

