<%@ include file="/include.jsp" %>


<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<%@ page import="io.cyberstock.tcdop.api.DOConfigConstants" %>
<c:set var="apiKey" value="<%=DOConfigConstants.API_KEY%>"/>


<h2 class="noBorder section-header">Cloud Access Information</h2>
<script type="text/javascript">
    BS.LoadStyleSheetDynamically("<c:url value='${resPath}do-settings.css'/>");
</script>

<table>
    <tr>
        <th>API KEY</th>
        <td><props:textProperty name="${apiKey}" className="textProperty longField"/></td>
    </tr>
</table>