<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
hello ${hello?default("")}
<br/>
111
<#assign answer=42111000/>
${answer}<br/>
${answer?string} <#-- the same as ${answer} --><br/>
${answer?string.number}<br/>
${answer?string.currency}<br/>
${answer?string.percent}<br/>
${answer}<br/>
<h1></h1>
<table>
    <tr>
        <td>1</td>
        <td>2</td>
        <td>3</td>
        <td>4</td>
        <td>5</td>
    </tr>
    <#list list as map>
    <tr>
        <td>${map.a}</td>
        <td>${map.b}</td>
        <td>${map.c}</td>
        <td>${map.d}</td>
        <td>${map.e}</td>
    </tr>
    </#list>
</table>
</body>
</html>