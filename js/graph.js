// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(chargeFrames);

function chargeFrames(){
	console.log("test");
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
        drawChart(json.chart_id,
                          json.title, 
                          json.columns, 
                          data, 
                          json.typeofchart
                  );
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

function drawFrame(id_chart, id_column){
        if(id_column=='1'){
                console.log(id_chart)
                $("#now").append("<div class='frame'><button class='close' title='retirer des favoris' onclick='remove_(this);'></button><div class='box'><div class='chart' id='"+id_chart+"'></div></div></div>");
        }
        else if (id_column=='2'){
                $('#asap').append("<div class='frame'><button class='close' title='retirer des favoris' onclick='remove_(this);'></button><div class='box'><div class='chart' id='"+id_chart+"'></div></div></div>");                
        }
        else if (id_column=='3'){
                $('#shot').append("<div class='frame'><button class='close' title='retirer des favoris' onclick='remove_(this);'></button><div class='box'><div class='chart' id='"+id_chart+"'></div></div></div>");
        }
}

function drawChart(id_chart,title,columns,data,type_of_chart) {
    var options;
        var chart;
        switch(type_of_chart) {
                case "LineChart":
                        options = {'title': title, 
                                                legend: {position : 'in'}, 
                                                'backgroundColor': { 'fill':'transparent' },
                                                'trendlines': {0:{},1:{}},
                                                'chartArea':{width:"80%",height:"80%"}};        
                        chart = new google.visualization.LineChart(document.getElementById(id_chart));
                        break;
                case "BarChart":
                    options = {'title': title,
	                            legend : {position : 'in'},
	                            'isStacked':'true',
	                            colors:['blue','red','#AAAAAA'],
	                            legend: 'none', 
	                            'backgroundColor': { 'fill':'transparent' },
	                            'chartArea':{width:"80%",height:"80%"}};        
                        chart = new google.visualization.BarChart(document.getElementById(id_chart));
                        break;
                case "BubbleChart":
                        options={'title': title, 
                                                legend: {position : 'in'}, 
                                                colorAxis: {colors: ['green', 'yellow', 'red'], minValue : 0, maxValue : 1000},
                                                hAxis : {maxValue : 1000},
                                                vAxis : {maxValue : 1000},
                                                sizeAxis : {minSize :6, maxSize : 6},
                                                'backgroundColor': { 'fill':'transparent' },
                                                'chartArea':{width:"80%",height:"80%"}};
                        chart = new google.visualization.BubbleChart(document.getElementById(id_chart));
                        break;
        }
    var dataTable = new google.visualization.arrayToDataTable(data);
           chart.draw(dataTable, options);
}

function resize(){
  chart.draw(data, options);        
}