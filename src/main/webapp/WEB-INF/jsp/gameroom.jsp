<!DOCTYPE html>
<!--<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>-->
<html lang="en"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>

<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />
</head>
<body>

	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Tic-Tac-Toe</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="/">Home</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
        <div id="playerInfo">
            <h1><label>Please wait while we find an Opponent....!</label></h1>
        </div>
		<div class="starter-template hide">
			<h2><label id="result"></label></h2>
			<input type="hidden" id="gameType" value="${gameType}"/>
			<input type="hidden" id="gameId" value=""/>
			<input type="hidden" id="name" value=""/>
			<table class="gamepanel">
			<c:forEach items="${gamepanel}" var="gamerow">
				<tr><td><Button class="YTF">${gamerow[0]}</Button></td><td><Button class="YTF">${gamerow[1]}</Button></td><td><Button class="YTF">${gamerow[2]}</Button></td></tr>
			</c:forEach>
			</table>
		</div>
		<div>
			<label>Steps:</label>
			<table id="steps">
			</table>
		</div>
	</div>
	<script type="text/javascript"
		src="webjars/jquery/3.1.1-1/jquery.min.js"></script>
	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="js/gameroom.js"></script>
	<script type="text/javascript"
		src="webjars/sockjs-client/sockjs.min.js"></script>

</body>

</html>
