<html>
<head>
<title>student></title>
</head>
<body>
学生信息：<br/>
<table border="1">
	<tr>
		<th>序号</th>
		<th>学号</th>
		<th>姓名</th>
		<th>年龄</th>
		<th>家庭住址</th>		
	</tr>
	<#list stuList as stu>
		<#if stu_index % 2 == 0>
			<tr bgcolor="red">
				<td>${stu_index}</td>
				<td>${stu.id}</td>
				<td>${stu.name}</td>
				<td>${stu.age}</td>
				<td>${stu.address}</td>		
			</tr>
		<#else>
			<tr bgcolor="green">
				<td>${stu_index}</td>
				<td>${stu.id}</td>
				<td>${stu.name}</td>
				<td>${stu.age}</td>
				<td>${stu.address}</td>		
			</tr>
		</#if>
	</#list>
	<br/>
	当前日期：${date?string("yyyy年MM月dd日 HH:mm:ss")  }
</table>

<#include "hello.ftl">

</body>
</html>