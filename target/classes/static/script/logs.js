var logEventEnvoker = [
	{   "name":"getLogEventsWithSearch",
		"has_callback" : true,
	    "arg_names" : ["java.lang.String",
	    			   "java.lang.String",
	    			   "java.lang.Integer",
	    			   "java.lang.Integer"]
	},
];

var logEventHandler = function(instance){
	this.derp = function(a){
		console.log(a);
	}
}

var socketAPILogEventInstanceProvider = new SocketAPIScopeInstanceProvider("LOG_EVENT", logEventEnvoker, logEventHandler);

socketAPIScopeFactoryPrimary.addProvider(socketAPILogEventInstanceProvider);

var scopes = ['BASIC_ERROR', 'FLAG_COLOR', 'LOG_EVENT']
getSocketInstance(function (json){
	handleResult(json);
	j = json;
}, scopes);
var socket;
function handleResult(session){
	var socketAPIScopeSession = new SocketAPIScopeSession();
	
	session.onerror = function(error){
		console.log(error);
	};
	session.onclose = function(error){
   		console.log(error);
	};
	session.onopen = function(error){
    	handleOpen(error);
    }
	//TODO export these functions to external js file
	//var socketAPIBasicErrorInstance = socketAPIBasicErrorInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIBasicErrorInstance);
	
	//var socketAPIFlagColorInstance = socketAPIFlagColorInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIFlagColorInstance);
	
	//var socketAPIRestrictionsInstance = socketAPIRestrictionsInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIRestrictionsInstance);
	
	socketAPIScopeFactoryPrimary.addScopesToInstance(socketAPIScopeSession, scopes);
	
	socketAPIScopeSession.acceptSession(session);
	socket = socketAPIScopeSession;
}
function isPageCountValid(pageCount){
	return $(".eventTableDelete").length == pageCount;
}
function clearEventLogPages(){
	$(".eventTableDelete").remove();
}
function generateEventLogPage(pageNumber, pageSizes){
	var table = $("#logEventTable");
	for(var i = 0; i < pageSizes; i++){
		var tr = $("<tr>", {class : "eventTableDelete"});
		table.append(tr);
		var td = $("<td>", {});
		td.attr("id", i + "_user");
		tr.append(td);
		td = $("<td>", {});
		td.attr("id", i + "_date");
		tr.append(td);
		td = $("<td>", {});
		td.attr("id", i + "_time");
		tr.append(td);
		td = $("<td>", {});
		td.attr("id", i + "_group");
		tr.append(td);
		td = $("<td>", {});
		td.attr("id", i + "_type");
		tr.append(td);
		td = $("<td>", {});
		td.attr("id", i + "_action");
		tr.append(td);
	}
}
function fillEventLogPage(values){
	for(var i = 0; i < values.length; i++){
		var value = values[i];
		console.log(value);
		$("#" + i + "_user").text(value.username);
		$("#" + i + "_date").text(value.event_date);
		$("#" + i + "_time").text(value.event_time);
		$("#" + i + "_group").text(value.action_group);
		$("#" + i + "_type").text(value.action_type);
		$("#" + i + "_action").text(value.event_action);
	}
}
function makeThrobbing(){
	$("#throbbing-icon").show();
}
function bustThrobbing(){
	$("#throbbing-icon").hide();
}
function updatePagination(page, pageCount){
	var maxShow = 5;
	var toShow = Math.min(maxShow, pageCount);
	console.log(toShow);
	var low = page;
	var high = page;
	var side = false;
	for(var i = 1; i < toShow; i++){
		if(side){
			if(low <= 1)
				high++;
			else
				low--;
		}else{
			if(high >= pageCount)
				low--;
			else
				high++;
		}
		side = !side;
	}
	
	$(".page-text-button").remove();
	
	var pageList = $("#pageList");
	
	var pageLiFirst = $("#page-next-button");
	
	for(var i = low; i <= high; i++){
		var li = $("<li>", {class : "page-item"});
		li.insertAfter(pageLiFirst);
		//pageList.append(li);
		pageLiFirst = li;
		if(i == page){
			li.addClass("active");
		}
		var a = $("<a>", {class : "page-link page-text-button", href : "#", text : i});
		a.data("page-number", i);
		a.data("page-selected", i == page);
		a.click(updatePage);
		li.append(a);
	}
	
	var pageFirst = $("#page-next-button");
	
	var pageLiLast = $("#page-back-button");
	
	if(low == 1){
		pageFirst.addClass("disabled");
		pageFirst.find("a").attr("tabindex", "-1");
	}else{
		pageFirst.removeClass("disabled");
		pageFirst.find("a").attr("tabindex", "");
	}
	
	if(high == pageCount){
		pageLiLast.addClass("disabled");
		pageLiLast.find("a").attr("tabindex", "-1");
	}else{
		pageLiLast.removeClass("disabled");
		pageLiLast.find("a").attr("tabindex", "");
	}
	
	console.log(low);
	console.log(high);
	
	
}
function doSearch(page){
	var show = $("#show").val();
	var sort = $("#sort").val();
	var text = $("#text").val();
	var perPage = parseInt(show);
	if(!page)
		page = getSelectedPage();
	makeThrobbing();
	socket.scopes.LOG_EVENT.envoker.getLogEventsWithSearch(text, sort, page - 1, perPage, (a) => {
		console.log(a);
		var pageCounts = a.argument.pageCounts;
		var maxPage = Math.ceil(pageCounts/perPage);
		console.log("jfsdklfjs" + maxPage);
		updatePagination(page, maxPage);
		var derpArgs = a.argument.respvalues;
		if(!isPageCountValid(derpArgs.length)){
			clearEventLogPages();
			generateEventLogPage(0, derpArgs.length);
		}
		fillEventLogPage(derpArgs);
		bustThrobbing();
	});
}
function getSelectedPage(){
	var value = 0;
	$(".page-text-button").each(function(a) {
		var thiss = $(this);
		if(thiss.data("page-selected"))
			value = thiss.data("page-number");
	});
	return value;
}
function attachSearchEvents(){
	$("#search_button").click(handleButtonSearchClick);
}
function handleButtonSearchClick(a){
	doSearch(getSelectedPage());
}
function updatePage(a){
	var pageNumber = $(a.target).data("page-number");
	doSearch(pageNumber);
}
function handleLogEventCreate(a){
	generateEventLogPage(0, a.argument.length);
	fillEventLogPage(a.argument);
	//updatePagination(98, 100);
}
function handleOpen(error){
	socket.scopes.FLAG_COLOR.envoker.getFlagColor(updateFlagColor);
	doSearch(1);
	attachSearchEvents();
}