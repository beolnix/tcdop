<%@ include file="/include.jsp" %>

<%@ page import="io.cyberstock.tcdop.api.DOConfigConstants" %>

<c:set var="instanceId" value="<%=DOConfigConstants.INSTANCE_ID%>"/>

<h2 class="noBorder section-header">Single Instance configuration</h2>
<table>

    <tr>
        <th>Instance ID</th>
        <td><props:textProperty name="${instanceId}" className="longField"/></td>
    </tr>


</table>