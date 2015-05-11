<%@ include file="/include.jsp" %>


<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="intprop" uri="/WEB-INF/functions/intprop" %>

<jsp:useBean id="webCons" class="io.cyberstock.tcdop.server.web.DOWebConstants"/>

<h2 class="noBorder section-header">Cloud Access Information</h2>
<script type="text/javascript">
    BS.LoadStyleSheetDynamically("<c:url value='${resPath}do-settings.css'/>");
</script>

<table>
    <tr>
        <th>API KEY</th>
        <td><props:textProperty name="${webCons.API_KEY}" className="settings longField"/></td>
    </tr>
</table>