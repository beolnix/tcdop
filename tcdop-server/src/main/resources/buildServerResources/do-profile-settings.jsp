<%@ include file="/include.jsp" %>

<%--@elvariable id="resPath" type="java.lang.String"--%>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<%@ page import="io.cyberstock.tcdop.model.DOConfigConstants" %>
<%@ page import="io.cyberstock.tcdop.model.WebConstants" %>
<c:set var="API_KEY" value="<%=WebConstants.TOKEN%>"/>
<c:set var="IMAGE_ID" value="<%=WebConstants.IMAGE_NAME%>"/>
<c:set var="INSTANCES_LIMIT" value="<%=WebConstants.INSTANCES_COUNT_LIMIT%>"/>
<c:set var="AGENT_CONFIG_TYPE" value="<%=WebConstants.DO_INTEGRATION_MODE%>"/>
<c:set var="SINGLE_INSTANCE_TYPE" value="<%=DOConfigConstants.PREPARED_IMAGE_MODE_CODE%>"/>
<c:set var="DROPLET_SIZE" value="<%=WebConstants.DROPLET_SIZE%>"/>
<c:set var="DROPLET_NAME_PREFIX" value="<%=WebConstants.DROPLET_NAME_PREFIX%>"/>

<c:set var="MULTIPLE_DOCKER_BASED_INSTANCE_TYPE" value="<%=DOConfigConstants.DOCKER_BASED_MODE_CODE%>"/>

<c:set var="STYLES_PATH" value="<%=WebConstants.STYLES_PATH%>"/>

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
            <props:textProperty name="${API_KEY}" id="${API_KEY}" className="longField"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${API_KEY}"></span>
        </td>
    </tr>
    <tr>
        <th class="tcdop-property-label-container">Instances number limit</th>
        <td>
            <props:textProperty name="${INSTANCES_LIMIT}" id="${INSTANCES_LIMIT}"
                                className="longField" value="4"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${INSTANCES_LIMIT}"></span>
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
                              value="${MULTIPLE_DOCKER_BASED_INSTANCE_TYPE}">
                    <c:out value="docker based instances"/>
                </props:option>
            </props:selectProperty>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${AGENT_CONFIG_TYPE}"></span>
        </td>
    </tr>

    <tr id="${SINGLE_INSTANCE_TYPE}_section">
        <th class="tcdop-property-label-container">Image Name</th>
        <td>
            <props:textProperty name="${IMAGE_ID}" className="longField"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${IMAGE_ID}"></span>
        </td>
    </tr>
    <tr>
        <th class="tcdop-property-label-container">Size</th>
        <td>
            <props:selectProperty name="${DROPLET_SIZE}" className="longField">
                <props:option id="DROPLET_512MB" value="512MB">
                    <c:out value="512MB"/>
                </props:option>
                <props:option id="DROPLET_1GB" value="1GB">
                    <c:out value="1GB"/>
                </props:option>
                <props:option id="DROPLET_2GB" value="2GB">
                    <c:out value="2GB"/>
                </props:option>
                <props:option id="DROPLET_4GB" value="4GB">
                    <c:out value="4GB"/>
                </props:option>
                <props:option id="DROPLET_8GB" value="8GB">
                    <c:out value="8GB"/>
                </props:option>
                <props:option id="DROPLET_16GB" value="16GB">
                    <c:out value="16GB"/>
                </props:option>
                <props:option id="DROPLET_32GB" value="32GB">
                    <c:out value="32GB"/>
                </props:option>
                <props:option id="DROPLET_64GB" value="64GB">
                    <c:out value="64GB"/>
                </props:option>
            </props:selectProperty>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${DROPLET_SIZE}"></span>
        </td>
    </tr>
    <tr>
        <th class="tcdop-property-label-container">Droplet instance name prefix</th>
        <td>
            <props:textProperty name="${DROPLET_NAME_PREFIX}" id="${DROPLET_NAME_PREFIX}" className="longField"
                                value="TCDOP"/>
        </td>
        <td class="tcdop-error-container">
            <span class="error tcdop-error-msg" id="error_${DROPLET_NAME_PREFIX}"></span>
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

