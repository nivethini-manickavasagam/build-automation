console.log("Loading application..");
var self = this;

var gatewayUI = null;
var gatewayCore = null;
var selectedDevices = [];
var allDevices = [];

var getCount = function(data) {
	var retVal = "";
	data["bySeverity"].forEach(function(severity){
		console.log("Validating severity:", severity);
		if (severity.id == 'Critical' || severity.id=='High') {
			if (retVal) {
				retVal = retVal + " / ";	
			}
			retVal = retVal + severity.totalCount + " - " + severity.id;
		}
	});
	console.log("Returning retVal:", retVal);
	return retVal;
}

var getCategory = function(data) {
	var retVal = "";
	data["byCategory"].forEach(function(category){
		retVal = retVal + category.name + '<br/>';
	});
	
	return retVal;
}

var loadPackageDetails = function(){
	$.get("/security/packages", function(data){
		console.log("Security packages responses received:", data);
		gatewayUI = data["42"];
		gatewayCore = data["30"];
		console.log("Values:", gatewayUI, gatewayCore);
		$('#GatewayUI').text(getCount(gatewayUI));
		$('#GatewayCore').text(getCount(gatewayCore));
		$("#GatewayUI").attr("title", getCategory(gatewayUI)).attr("data-toggle","tooltip").attr("data-placement","right");
		$("#GatewayCore").attr("title", getCategory(gatewayCore)).attr("data-toggle","tooltip").attr("data-placement","right");
		$('[data-toggle="tooltip"]').tooltip();
	});
}

var loadDeviceDetails = function(){
	$.get("/devices", function(devices) {
		console.log("Devices:", devices);
		allDevices.length=0;
		var table = $('#deviceData').DataTable();
		table.clear().draw();
		//table.row.clear();
		for(var i = 0; i < devices.length; i++) {
			allDevices.push(devices[i].name);
			table.row.add( [
				devices[i].isCompliant?'<input type="checkbox" style="height:20px;width:20px" disabled></input>':'<input type="checkbox" style="height:20px;width:20px" onclick="selectDevice(\''+devices[i].name+'\')"></input>',
				devices[i].name,
				devices[i].version?devices[i].version:"",
				devices[i].isCompliant?"<span style='font-size:24px;'>&#10004;</span>":"<span style='font-size:24px;'>&#10008;</span>",
				'<div id="status_'+i+'">N/A</div>'	
			] ).draw( false );
		}
	});
}

var updateStatus = function(){
	for (var i=0; i< selectedDevices.length;i++) {
		var statusIndex = allDevices.indexOf(selectedDevices[i]);
		$('#status_'+statusIndex).html('<div class="spinner-border text-info"></div>');
		$.get('/devices/'+selectedDevices[i]+"/status", function(data){
			var index = allDevices.indexOf(data.id);
			console.log("Received response:"+JSON.stringify(data)+ ": Index:"+ index);
			$('#status_'+index).html("<div>"+data.status+"</div>");
		});
	}
}

var selectComponent = '<select name="#id"><option value="Audio">Audio</option><option value="Core">Core</option><option value="Crypto">Crypto</option><option value="BluetoothManager">Bluetooth Manager</option><option value="Certificates">Certificates</option></select>';
var classificationComponent = '<select name="#id"><option value="SecurityPatch">Security Patch</option><option value="UpdateRollout">Update Rollout</option></select>';

function getFormattedDate() {
	var date = new Date();
    return (((date.getMonth() > 8) ? (date.getMonth() + 1) : ('0' + (date.getMonth() + 1))) + '/' + ((date.getDate() > 9) ? date.getDate() : ('0' + date.getDate())) + '/' + date.getFullYear());
}

var addNewVersion = function() {
	var table = $('#releaseVersion').DataTable();
	var currentCount = table.row.length;
	var softwareType = selectComponent.replace("#id", "softwareType");
	var classification = classificationComponent.replace("#id", "classification");
	table.row.add( [
		'<input type="text" name="title" id="title"></input>',
		softwareType,
		classification,
		'<input type="text" name="releaseDate" value="'+getFormattedDate()+'"></input>',
		'<input type="text" style="width:25px" name="majorVersion"></input>.'+'<input type="text" style="width:25px" name="minorVersion"></input>.'+'<input type="text" style="width:25px" name="patchVersion"></input>',
		'<input type="file" name="file" id="file"></input>',
		'<span style="font-size:20px;">&#10008;</span>'
	] ).draw( false );
	$('#addVersion').prop( "disabled", true );
}

var loadSoftwareVersion= function(){
	$.get("/software", function(softwareVersions){
		var table = $('#releaseVersion').DataTable();
		table.clear().draw();
		//table.row.clear();
		softwareVersions.forEach(function(softwareVersion){
			var softwareVersion = 
				table.row.add( [
					softwareVersion.title,
					softwareVersion.softwareType,
					softwareVersion.classification,
					softwareVersion.releaseDate,
					softwareVersion.majorVersion+"."+softwareVersion.minorVersion+"."+softwareVersion.patchVersion,
					'<a href="/download/'+softwareVersion.id+'">'+softwareVersion.fileName+'</a>'	,
					softwareVersion.scmPackageId?'<span style="font-size:20px;">&#10004;</span>':'<span style="font-size:20px;">&#10008;</span>'
				] ).draw( false );
		});
	})
}

var updateDevices = function(){
	var selectedSoftwareType = $('#selectedType').val();
	var updateToVersion = $('#updateToVersion').val();
	$.ajax(
			{
				url: "/devices/update",
				data: JSON.stringify({devices:selectedDevices, softwareType:selectedSoftwareType, updateToVersion:updateToVersion }),
				contentType: "application/json",
				success : function(result,status,xhr) {alert ("Update request submitted for devices: " + selectedDevices)},
				type : "POST"
			}
	);
	$('#versionModal').modal('hide');
}

var selectDevice = function(device) {
	if (selectedDevices.indexOf(device) != -1){
		selectedDevices.splice(selectedDevices.indexOf(device), 1);
	} else {
		selectedDevices.push(device);
	}
}


var initialize = function() {	
	self.loadPackageDetails();
	self.loadDeviceDetails();
	self.loadSoftwareVersion();
}



self.initialize();