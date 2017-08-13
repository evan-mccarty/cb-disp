var programScheduleEnvoker = [
	{   "name":"getProgramSchedulesForMonth",
		"has_callback" : true,
	    "arg_names" : ["java.lang.Integer",
	    			   "java.lang.Integer"]
	},
	{   "name":"saveProgramSchedules",
		"has_callback" : true,
	    "arg_names" : ["java.util.ArrayList",
	    			   "java.util.HashMap",
	    			   "java.lang.Integer"]
	},
];

var  programScheduleHandler = function(instance){
	this.derp = function(a){
		console.log(a);
	}
}

var socketAPIProgramScheduleInstanceProvider = new SocketAPIScopeInstanceProvider("PROGRAM_SCHEDULE", programScheduleEnvoker, programScheduleHandler);

socketAPIScopeFactoryPrimary.addProvider(socketAPIProgramScheduleInstanceProvider);


var scopes = ['BASIC_ERROR', 'FLAG_COLOR', 'PROGRAM_SCHEDULE']
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
var programSchedules = null;
var programDays = null;
function createCalendar(start){
	var dates = getDatesInMonth(new Date(start));
	console.log("sdhjkghjksghjfghsgjkjkhfg");
	console.log(dates);
	var tableTopper = $("#programSetupTableTopper");
	createCalendarRows(tableTopper, dates);
	socket.scopes.PROGRAM_SCHEDULE.envoker.getProgramSchedulesForMonth(start.getFullYear(), start.getMonth(), (a) => {
		console.log(a);
		var schedules = a.argument.class_programs;
		var schedulesMap = getSchedules(a.argument.class_programs);
		programSchedules = schedulesMap;
		console.log(schedules);
		var realValues = createMapping(a.argument.class_dates, schedulesMap);
		programDays = realValues;
		populateProgramSelector(schedules);
		populateCalendar(dates, realValues);
	});
	console.log(start);
}
function createMapping(data, schedules){
	var map = [];
	for(var i = 0; i < data.length; i++){
		var d = data[i];
		d.program_schedule = schedules[d.program_schedule_id];
		map[new Date(d.program_date).getDate()] = d;
	}
	return map;
}
function getSchedules(date){
	var schedules = [];
	for(var i = 0; i < date.length; i++){
		var d = date[i];
		var scheduleId = d.id;
		if(schedules[scheduleId] == undefined)
			schedules[scheduleId] = d;
	}
	return schedules;
}
const calendar_program_colors = ['#00FFFF', '#0000FF', '#7FFFD4', '#8A2BE2', '#A52A2A',
								 '#7FFF00', '#006400', '#FF8C00', '#FF00FF', '#FFD700',
								 '#ADD8E6', '#FFB6C1', '#191970', '#FF4500', '#2E8B57',
								 '#FFFF00', '#9ACD32', '#DDA0DD', '#FFA500', '#000080'];
var defaultColor = '#00FFFF';
function getCalendarProgramColor(calendarProgram){
	if(calendarProgram.program_color == undefined)
		return defaultColor;
	else
		return calendarProgram.program_color;
	//return calendar_program_colors[calendarProgram.id % calendar_program_colors.length];
}
function populateCalendar(dates, calendarPrograms){
	console.log(calendarPrograms);
	console.log(dates);
	for(var i = 0; i < dates.length; i++){
		var currentDate = dates[i];
		var currentCalendarProgram = calendarPrograms[currentDate.getDate()];
		if(currentCalendarProgram != undefined){
			console.log(currentCalendarProgram);
			var calendarObject = $("#calendar-" + currentDate.getDate());
			var calendarProgramColor = getCalendarProgramColor(currentCalendarProgram.program_schedule);
			calendarObject.css("background-color", calendarProgramColor);
		}
	}
}
function populateProgramSelector(programs){
	$(".populateDelete").remove();
	var programSelector = $("#calendar-select");
	for(var i = 0; i < programs.length; i++){
		var p = programs[i];
		var option = $("<option>", {class : "populateDelete", value : p.id});
		option.text(p.program_text);
		option.css("background-color", getCalendarProgramColor(p));
		option.data("calendar-id", p.id);
		option.data("calendar-color", getCalendarProgramColor(p));
		programSelector.append(option);
	}
}
function selectCalendarById(id){
	var calendar = programSchedules[id];
	console.log(calendar);
	setCalendarInformation(calendar);
	setSelectedCalendarId(id);
}
function getSelectedCalendarId(){
	return $("#calendar-select").val();
}
function setSelectedCalendarId(id){
	$("#calendar-select").val(id);
}
function getNonGrossDate(date){
	var date = (date.getDay() - 1);
	if(date < 0)
		date = 6;
	return date;
}
function getDatesInMonth(date){
	var month = date.getMonth();
	var dates = [];
	while(date.getMonth() === month){
		dates.push(new Date(date));
		date.setDate(date.getDate() + 1);
	}
	return dates;
}
function createCalendarRows(table, values){
	$("#calendar-main").mouseup(handleUp).mouseleave(handleLeave);
	$(".calendar-delete").remove();
	var offset = getNonGrossDate(values[0]);
	console.log(offset + ":" + values[0].getDay());
	var max = Math.ceil((values.length + offset) / 7) * 7;
	var perRow = 7;
	for(var i = 0; i < max; i++){
		if(i % perRow == 0){
			var tr = $("<tr>", {});
			tr.insertAfter(table);
			table = tr;
		}
		var index = i - offset;
		var td = $("<td>", {class : "calendar-delete"});
		if(index > -1 && index < values.length){
			td.text(values[index].getDate());
			td.attr("id", "calendar-" + values[index].getDate());
			td.data("calendar-date", values[index]);
			td.mousedown(handleClick);
			td.mouseenter(handleEnter);
			td.contextmenu(function(){return false;});
			setHighlighted(td, false);
		}else{
			td.text("l");
		}
		table.append(td);
	}
}

const MOUSE_STATE_NONE = 0;
const MOUSE_STATE_HIGHLIGHT = 1;
const MOUSE_STATE_UNHIGHLIGHT = 2;
const MOUSE_STATE_CHOOSE = 3;

var mouse_state = MOUSE_STATE_NONE;

function handleClick(date){
	var value = $(date.target);
	if(mouse_state == MOUSE_STATE_CHOOSE){
		var id = value.data("calendar-date").getDate();
		console.log(id);
		var date = programDays[id];
		selectCalendarById(date.program_schedule_id);
	}else{
		if(mouse_state == MOUSE_STATE_NONE)
			if(date.which == 3)
				mouse_state = MOUSE_STATE_UNHIGHLIGHT;
			else
				mouse_state = MOUSE_STATE_HIGHLIGHT;
		setHighlighted(value, date.which != 3);
	}
}
function handleCalendarChoose(date){
	mouse_state = MOUSE_STATE_CHOOSE;
}
function handleEnter(date){
	console.log(mouse_state);
	var value = $(date.target);
	if(mouse_state == MOUSE_STATE_HIGHLIGHT)
		setHighlighted(value, true);
	if(mouse_state == MOUSE_STATE_UNHIGHLIGHT)
		setHighlighted(value, false);
}
function handleUp(date){
	mouse_state = MOUSE_STATE_NONE;
	console.log("butt");
}
function handleLeave(date){
	mouse_state = MOUSE_STATE_NONE;
}
function setHighlighted(date, highlighted){
	if(highlighted){
		date.addClass("calendar-highlighted");
		date.data("calendar-highlighted", true);
	}else{
		date.removeClass("calendar-highlighted");
		date.data("calendar-highlighted", false);
	}
}
function getHighlighted(){
	return $(".calendar-highlighted").filter(function(index) {
		return $(this).data("calendar-highlighted") == true;
	});
}
const CALENDAR_CREATE = 0;
const CALENDAR_SAVE = 1;
const CALENDAR_DELETE = 2;
function handleCalendarCreate(calendar){
	var dates = [];
	var highlighted = getHighlighted();
	highlighted.each(function (index){
		dates.push($(this).data("calendar-date").toISOString());
	});
	var calendarInformation = getCalendarInformation();
	socket.scopes.PROGRAM_SCHEDULE.envoker.saveProgramSchedules(dates, calendarInformation, CALENDAR_CREATE, () => {
		updateCalendar();
	});
	console.log(":" + dates);
	console.log(calendarInformation);
}
function handleCalendarSave(calendar){
	var selectedId = getSelectedCalendarId();
	if(selectedId == undefined || selectedId == null || selectedId == ""){
		showSelectGroupError();
		return;
	}
	console.log(programSchedules[selectedId]);
	var calendarInformation = getCalendarInformation();
	calendarInformation.calendar_id = selectedId;
	socket.scopes.PROGRAM_SCHEDULE.envoker.saveProgramSchedules([], calendarInformation, CALENDAR_SAVE, () => {
		updateCalendar();
	});
}
function handleCalendarSelect(calendar){
	var selected = getSelectedCalendarId();
	selectCalendarById(selected);
}
function handleCalendarDelete(calendar){
	var selectedId = getSelectedCalendarId();
	if(selectedId == undefined || selectedId == null || selectedId == ""){
		showSelectGroupError();
		return;
	}
	var calendarInformation = {};
	calendarInformation.calendar_id = selectedId;
	setSelectedCalendarId("");
	socket.scopes.PROGRAM_SCHEDULE.envoker.saveProgramSchedules([], calendarInformation, CALENDAR_DELETE, () => {
		updateCalendar();
	});
}
function showSelectGroupError(){
	alert("Select a calendar group first");
}
function getCalendarInformation(){
	var information = {};
	information.jp_enabled = $("#jp-enabled").is(":checked");
	information.jp_start = $("#jp-start").val();
	information.jp_end = $("#jp-end").val();
	information.ap_enabled = $("#ap-enabled").is(":checked");
	information.ap_start = $("#ap-start").val();
	information.ap_end = $("#ap-end").val();
	information.ap_sunset_end = $("#ap-sunset-end").is(":checked");
	information.group_color = $("#calendar-color").val();
	information.group_name = $("#calendar-text").val();
	return information;
}
function setCalendarInformation(information){
	$("#jp-enabled").prop("checked", information.jp_enabled);
	$("#jp-start").val(information.jp_start);
	$("#jp-end").val(information.jp_end);
	$("#ap-enabled").prop("checked", information.ap_enabled);
	$("#ap-start").val(information.ap_start);
	$("#ap-end").val(information.ap_end);
	$("#ap-sunset-end").prop("checked", information.ap_sunset_end);
	$("#calendar-color").val(information.program_color);
	$("#calendar-text").val(information.program_text);
}
function handleClassGet(classes){
	console.log(classes);
}

function attachCalendarNavigationControls(){
	$("#month-left").click(moveLeft);
	$("#month-right").click(moveRight);
	$("#calendar-save").click(handleCalendarSave);
	$("#calendar-create").click(handleCalendarCreate);
	$("#calendar-select").change(handleCalendarSelect);
	$("#calendar-choose").click(handleCalendarChoose);
	$("#calendar-delete").click(handleCalendarDelete);
}
function updateCalendar(){
	createCalendar(currentDate);
}
function moveLeft(){
	currentDate.setMonth(currentDate.getMonth() + 1);
	createCalendar(currentDate);
}
function moveRight(){
	currentDate.setMonth(currentDate.getMonth() - 1);
	createCalendar(currentDate);
}
var currentDate;
function handleOpen(error){
	socket.scopes.FLAG_COLOR.envoker.getFlagColor(updateFlagColor);
	currentDate = new Date(2017, 8, 1);
	createCalendar(currentDate);
	attachCalendarNavigationControls();
}