var dataArray = [{ratio:"[1:0:0:0]", reading:"10"},	{ratio:"[0:2:4:6]", reading:"20"},	{ratio:"[0:7:3:4]", reading:"30"},	{ratio:"[0:5:7:3]", reading:"40"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"},
						 {ratio:"[1:0:0:0]", reading:"20"},	{ratio:"[0:2:4:6]", reading:"20"},	{ratio:"[0:7:3:4]", reading:"20"},	{ratio:"[0:5:7:3]", reading:"20"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"},
						 {ratio:"[1:0:0:0]", reading:"20"},	{ratio:"[0:2:4:6]", reading:"20"},	{ratio:"[0:7:3:4]", reading:"20"},	{ratio:"[0:5:7:3]", reading:"20"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"},
						 {ratio:"[1:0:0:0]", reading:"10"},	{ratio:"[0:2:4:6]", reading:"20"},	{ratio:"[0:7:3:4]", reading:"20"},	{ratio:"[0:5:7:3]", reading:"20"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"},
						 {ratio:"[1:0:0:0]", reading:"20"},	{ratio:"[0:2:4:6]", reading:"20"},	{ratio:"[0:7:3:4]", reading:"20"},	{ratio:"[0:5:7:3]", reading:"20"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"},
						 {ratio:"[1:0:0:0]", reading:"40"},	{ratio:"[0:2:4:6]", reading:"60"},	{ratio:"[0:7:3:4]", reading:"20"},	{ratio:"[0:5:7:3]", reading:"20"},	{ratio:"[0:3:5:1]", reading:"20"},	{ratio:"[0:0:0:1]", reading:"20"}
						 ];

var tableRow = document.createElement("tr");
var tableData = document.createTextNode("");

for(var i = 0; i < 6; i++){

	$("#energyReadings").find($('tbody')).append($('<tr>'));

	for(var j = 0; j < 6; j++){

		$("#energyReadings").find($('tr')).append($('<td>'));
		$("#energyReadings").find($('td')).text(function(){return dataArray[i * 6 + j].reading;});

		console.log("this happens" + dataArray[i * j].reading);
	}
}

		var canvas = d3.select("#visualizationDiv")
						.append("svg")
						.text("Cu,Zn,Sl,Ln,Fe")
						.attr("style", "outline: thick solid black;")
						.attr("width", 425)
						.attr("height",400);

		var colorScale = d3.scaleLinear()
								 .domain([0,60])
								 .range(["white","purple"]);

		var circles = canvas.selectAll("rect")
							.data(dataArray)
							.enter()
								.append("rect")
								.attr("height", 50)
								.attr("width", 50)
								.attr("fill",function(d){
									return colorScale(d.reading);
								})
								.attr("x", function(d,c){
									return 25 + ((c % 6) * 60);
								})
								.attr("y",function(d,c){
									return 25 + (Math.floor(c / 6) * 60);
								})
								.text(function(d){return d.ratio;})
								.on("click", function(d) {
        							alert("Ratio: " + d.ratio);});