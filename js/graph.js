// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(chargeFrames);

function chargeFrames(){
	var runways = document.getElementsByClassName("runway");
	var i;
	for (i=0; i<runways.length; i++){
		ajaxRequest("GET","/graph/"+i, "",function() {        
	        return function(response){
	                if(!response){
	                        console.log("fail");
	                        return;
	                }
	                console.log(response);
	                var schedule = JSON.parse(response);
	                //var data = parseData(schedule);
	                //var chart = drawChart(runways[i], 'Piste d\'atterissage', data);
	        }
		}());
	}
	
}

function parseData(json){
	drawFrame(json.chart_id,json.id_column, json.priority);
	var all_data=json.data;
	var rows= new Array();
	$.each(all_data, function(key,val){	
	        var row = new Array();
	        $.each(val, function(key2,val2){
	                row.push(val2);
	        });
	        rows.push(row);
	});
	var data = formatData(json.columns, rows);
}

function formatData(columnHeader, data) {
        for (var i=0;i<columnHeader.length;i++) {
                for (var j=0;j<data.length;j++) {
                        if (columnHeader[i] == "date") {
                                data[j][i] = new Date(data[j][i]);
                        } else {
                                var value = parseFloat(data[j][i]);
                                if(!isNaN(value)) {
                                        data[j][i] = value;
                                }
                        }
                }
        }
        data.unshift(columnHeader);
        return data;
}

function drawChart(container,title,data) {
    var options = {'title': title, 
                    legend: {position : 'in'}, 
                    'backgroundColor': { 'fill':'transparent' },
                    'chartArea':{width:"80%",height:"80%"}};        
    var chart = new google.visualization.TimeLine(container);
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

function resize(){
  chart.draw(data, options);        
}