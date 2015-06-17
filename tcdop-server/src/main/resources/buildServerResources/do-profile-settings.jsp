<%@ include file="/include.jsp" %>


<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<%@ page import="io.cyberstock.tcdop.model.DOConfigConstants" %>
<c:set var="apiKey" value="<%=DOConfigConstants.TOKEN%>"/>
<c:set var="imageId" value="<%=DOConfigConstants.IMAGE_ID%>"/>
<c:set var="sshPrivateKey" value="<%=DOConfigConstants.SSH_PRIVATE_KEY%>"/>
<c:set var="sshPublicKey" value="<%=DOConfigConstants.SSH_PUBLIC_KEY%>"/>
<c:set var="agentConfigType" value="<%=DOConfigConstants.DO_INTEGRATION_MODE%>"/>

<c:set var="AGENT_CONFIG_TYPE" value="<%=DOConfigConstants.DO_INTEGRATION_MODE%>"/>
<c:set var="SINGLE_INSTANCE_TYPE" value="<%=DOConfigConstants.PREPARED_IMAGE_MODE_CODE%>"/>
<c:set var="MULTIPLE_DOCKER_BASED_INSTANCE_TYPE" value="<%=DOConfigConstants.DOCKER_BASED_MODE_CODE%>"/>

<script type="text/javascript">
    BS.LoadStyleSheetDynamically("<c:url value='${resPath}do-settings.css'/>");
</script>

<h2 class="noBorder section-header">Cloud Access Information</h2>
<table>
    <tr>
        <th>Token</th>
        <td><props:textProperty name="${apiKey}" className="longField"/></td>
    </tr>
</table>

<l:settingsGroup title="Agent Instance settings" className="advancedSetting">
    <table>
        <tr>
            <th>Agent type</th>
            <td>
                <props:selectProperty name="${AGENT_CONFIG_TYPE}" className="longField"
                                      onchange="syncCloudSelectionControlState(); return true;">
                    <props:option id="${SINGLE_INSTANCE_TYPE}" value="${SINGLE_INSTANCE_TYPE}"><c:out
                            value="single instance"/></props:option>
                    <props:option id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}"
                                  value="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}"><c:out value="docker based instances"/></props:option>
                </props:selectProperty>
            </td>
        </tr>
    </table>
    <table id="${SINGLE_INSTANCE_TYPE}_section" class="advancedSetting">
        <tr>
            <th>Image ID</th>
            <td><props:textProperty name="${imageId}" className="longField"/></td>
        </tr>
    </table>
    <table id="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}_section" class="advancedSetting">
        <tr>
            <th colspan="2">Dynamic docker based configuration isn't supported yet</th>
        </tr>
    </table>

</l:settingsGroup>


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

