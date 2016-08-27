var LOI1Data = [{ratio:"[1:0:0:0]", reading:"0.173035538768644"},	{ratio:"[1:0:0:0]", reading:"0.033928537013459"},	{ratio:"[1:0:0:0]", reading:"0.105178464741725"},	{ratio:"[1:0:0:0]", reading:"0.359642492342673"},	{ratio:"[1:0:0:0]", reading:"0.094999903637687"},	{ratio:"[1:0:0:0]", reading:"0"},
				{ratio:"[5:5:6:0]", reading:"0.731159972640057"},	{ratio:"[5:5:6:0]", reading:"0.649731483807753"},	{ratio:"[5:5:6:0]", reading:"0.695535008775924"},	{ratio:"[5:5:6:0]", reading:"0.60223153198891"},	{ratio:"[5:5:6:0]", reading:"0.566606568124776"},	{ratio:"[5:5:6:0]", reading:"0.576785129228815"},
				{ratio:"[7:6:7:0]", reading:"0.649731483807753"},	{ratio:"[7:6:7:0]", reading:"0.653124337509099"},	{ratio:"[7:6:7:0]", reading:"0.780356351309573"},	{ratio:"[7:6:7:0]", reading:"0.683660020821213"},	{ratio:"[7:6:7:0]", reading:"0.722677838386691"},	{ratio:"[7:6:7:0]", reading:"0.651427910658426"},
				{ratio:"[3:1:6:0]", reading:"0.666695752314483"},	{ratio:"[3:1:6:0]", reading:"0.639552922703715"},	{ratio:"[3:1:6:0]", reading:"0.739642106893421"},	{ratio:"[3:1:6:0]", reading:"0.695535008775924"},	{ratio:"[3:1:6:0]", reading:"0.676874313418521"},	{ratio:"[3:1:6:0]", reading:"0.659910044911791"},
				{ratio:"[1:2:1:0]", reading:"0.817677742024379"},	{ratio:"[1:2:1:0]", reading:"0.839731291083128"},	{ratio:"[1:2:1:0]", reading:"0.902499084558028"},	{ratio:"[1:2:1:0]", reading:"0.76169565595217"},	{ratio:"[1:2:1:0]", reading:"0.839731291083128"},	{ratio:"[1:2:1:0]", reading:"0.868570547544569"},
				{ratio:"[0:0:0:1]", reading:"3.65240700949894"},	{ratio:"[0:0:0:1]", reading:"3.74401405943528"},	{ratio:"[0:0:0:1]", reading:"2.58874737412698"},	{ratio:"[0:0:0:1]", reading:"2.43946181126775"},	{ratio:"[0:0:0:1]", reading:"2.30714051691526"},	{ratio:"[0:0:0:1]", reading:"3.61678204563481"}
			];

var elementInfo = [
	{
		elementName: "Copper",
		atomicSymbol: "Cu",
		saltUsed: "Cu(NO3)2",
		concentration: 0.04
	},

	{
		elementName: "Zinc",
		atomicSymbol: "Zn",
		saltUsed: "Zn(NO3)2",
		concentration: 0.04
	},

	{
		elementName: "Aluminium",
		atomicSymbol: "Al",
		saltUsed: "Al(NO3)3",
		concentration: 0.04
	},

	{
		elementName: "Iron",
		atomicSymbol: "Fe",
		saltUsed: "Fe(NO3)3",
		concentration: 0.04
	}
]

var tableRow = document.createElement("tr");
var tableData = document.createTextNode("");

for(var i = 0; i < 6; i++){

	$("#energyReadings").find($('tbody')).append($('<tr>'));

	for(var j = 0; j < 6; j++){

		$("#energyReadings").find($('tr')).append($('<td>'));
		$("#energyReadings").find($('td')).text(function(){return LOI1Data[i * 6 + j].reading;});

		console.log("this happens" + LOI1Data[i * j].reading);
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
							.data(LOI1Data)
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