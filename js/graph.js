// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['timeline','corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(chargeFrames);

function getURLParameter(name) {
	  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}

function setSelectValue(name) {
	if(getURLParameter(name) != null) {
		document.getElementById(name).value = getURLParameter(name);
	}
}

function chargeFrames(){
	setSelectValue('generatorDifficulty');
	setSelectValue('generatorNbPlanes');
	setSelectValue('generatorType');
	setSelectValue('generatorTimeOut');
	
	var runways = document.getElementsByClassName("runwayGraph");
	var weight = document.getElementsByClassName("runwayWeight");
	var i = 0;
	for (i=0; i<runways.length; i++){
		ajaxRequest("GET","/graph/" + i, "",function(j) {        
	        return function(response){
	                if(!response){
	                        console.log("fail");
	                        return;
	                }
	                var graphs = JSON.parse(response)['graphs'];
					formatDates(graphs[0]['Timeline']);
	                drawChart(runways[j], 'Piste d\'atterissage', graphs[0]['Timeline'],'Timeline', 0);
					drawChart(weight[j], 'Charge sur la piste', graphs[1]['LineChart'],'LineChart', graphs[1]['vAxisHeight']);
	        }
		}(i));
	}
}

function formatDates(array) {
	array[1][2] = new Date(0,0,0,6,0,0);
	array[1][3] = new Date(0,0,0,6,0,0);
	for (i=2;i<array.length - 1;i++) {
		landingHour = 6 + Math.floor(array[i][2] / 60);
		landingMinute = array[i][2] % 60;
		array[i][2] = new Date(0,0,0,landingHour,landingMinute,0);
		takeOffHour = 6 + Math.floor(array[i][3] / 60);
		takeOffMinute = array[i][3] % 60;
		array[i][3] = new Date(0,0,0,takeOffHour,takeOffMinute,0);
	}
	array[array.length - 1][2] = new Date(0,0,1,0,0,0);
	array[array.length - 1][3] = new Date(0,0,1,0,0,0);
}

function drawChart(container, title, data, typeOfChart, vAxisHeight) {
    var options = {'title': title, 
                    legend: {position : 'in'}, 
                    'backgroundColor': { 'fill':'transparent' },
                    'chartArea':{width:"80%",height:"80%"},
					'vAxis':{maxValue:vAxisHeight}};
    var chart;
	switch(typeOfChart) {
	case 'Timeline':
		chart = new google.visualization.Timeline(container);
		break;
	case 'LineChart':
		chart = new google.visualization.LineChart(container);
		break;
	} 
    var dataTable = new google.visualization.arrayToDataTable(data);
    chart.draw(dataTable, options);
}

function ajaxRequest(method, url, data, onSuccess) {
        var xhr_object;
        if(window.XMLHttpRequest) // FIREFOX
                xhr_object = new XMLHttpRequest();
        else if(window.ActiveXObject) // IE
                xhr_object = new ActiveXObject("Microsoft.XMLHTTP");
        else
                return(false);
        xhr_object.open(method, url, true);
        if(method=="POST"){
                xhr_object.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xhr_object.send(data);        
        }
        else{
                xhr_object.send();
        }
        xhr_object.onreadystatechange = function() {
                if(xhr_object.readyState == 4) {
                        if (xhr_object.status == 200) {
                                onSuccess(xhr_object.responseText);
                        }
                }
        }
        
}