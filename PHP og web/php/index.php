<html>
<head>
	<meta charset='UTF-8'>
	<title> PlastPlukk</title>
	<style>
		h1{color: white}

		body{
			background-image: url("background.jpg");
			background-repeat: no-repeat;
  			background-size: cover;
		}
	</style>
	<!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css"
	integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ=="
	crossorigin=""/>
	<script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js"
	integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw=="
	crossorigin=""></script>
      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawAntallChart);
      google.charts.setOnLoadCallback(drawPerDayChart);
		google.charts.setOnLoadCallback(drawOmraadeChart);
      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
    <?php
    include_once("../itfag/config.php");
	$sql = "SELECT c.category, COUNT(*) as antall FROM registrations as r, categories as c WHERE c.type = r.type GROUP BY c.category";
	$result = mysqli_query($conn, $sql);
	$teller= 0;
	while ($row = mysqli_fetch_array($result)){
		switch ($teller) {
			case 0:
				$diverse = $row['antall'];
				break;
			case 1:
				$emballasje = $row['antall'];
				break;
			case 2:
				$flasker = $row['antall'];
				break;
			case 3:
				$pose = $row['antall'];
				break;
			case 4:
				$redskap = $row['antall'];
				break;
			case 5:
				$service = $row['antall'];
				break;
		}
		$teller++;
	};

	echo "
      function drawAntallChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Kategori');
        data.addColumn('number', 'Antall plukket');
        data.addRows([
          ['Poser', $pose],
          ['Emballasje', $emballasje],
          ['Flasker og beholdere', $flasker],
          ['Servise', $service],
          ['Redskap', $redskap],
          ['Diverse', $diverse]
        ]);

        var options = {title:'Hva er blitt plukket?',
                       width:400,
                       height:310};
        var chart = new google.visualization.PieChart(document.getElementById('chart_antall'));
        chart.draw(data, options);
      }";

	for ($i = 29; $i >= 0; $i--) {
	$sql = "SELECT count(*) as antall, DATE(NOW() - INTERVAL $i DAY) as dato FROM registrations as r WHERE r.Date = DATE(NOW()-INTERVAL $i DAY)";
	$result = mysqli_query($conn, $sql);
	$row = mysqli_fetch_array($result);
	${"antall$i"} = $row['antall'];
	${"dato$i"} = $row['dato'];
};
echo "
      function drawPerDayChart() {
        var data = new google.visualization.arrayToDataTable([
        ['Dato', 'Antall'],
          ['$dato29', $antall29],
          ['$dato28', $antall28],
          ['$dato27', $antall27],
          ['$dato26', $antall26],
          ['$dato25', $antall25],
          ['$dato24', $antall24],
          ['$dato23', $antall23],
          ['$dato22', $antall22],
          ['$dato21', $antall21],
          ['$dato20', $antall20],
          ['$dato19', $antall19],
          ['$dato18', $antall18],
          ['$dato17', $antall17],
          ['$dato16', $antall16],
          ['$dato15', $antall15],
          ['$dato14', $antall14],
          ['$dato13', $antall13],
          ['$dato12', $antall12],
          ['$dato11', $antall11],
          ['$dato10', $antall10],
          ['$dato9', $antall9],
          ['$dato8', $antall8],
          ['$dato7', $antall7],
          ['$dato6', $antall6],
          ['$dato5', $antall5],
          ['$dato4', $antall4],
          ['$dato3', $antall3],
          ['$dato2', $antall2],
          ['$dato1', $antall1],
          ['$dato0', $antall0]
        ]);

        var options = {
        	title:'Plukket siste 30 dager',
            curveType: 'function',
            legend: { position: 'bottom'},
            width:600,
            height:310
        };
        var chart = new google.visualization.LineChart(document.getElementById('chart_last_30_days'));
        chart.draw(data, options);
      }";

      $sql = "SELECT SUM(r.Mountain) as m, SUM(r.Forest) as f, SUM(r.River) as ri, SUM(r.Coast) as c, SUM(r.Lake) as l, SUM(r.Road) as ro, SUM(r.Industry_Towns) as i, SUM(r.School_Recreational_area) as s, SUM(r.Acre_Agriculture) as a, SUM(r.Residential_area) as re FROM registrations as r";
	$result = mysqli_query($conn, $sql);
	$row = mysqli_fetch_array($result);
	$mountain = $row['m'];
	$forest = $row['f'];
	$river = $row['ri'];
	$coast = $row['c'];
	$lake = $row['l'];
	$road = $row['ro'];
	$industry_towns = $row['i'];
	$school_recreational_area = $row['s'];
	$acre_agriculture = $row['a'];
	$residential_area = $row['re'];

	echo "
      function drawOmraadeChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Location');
        data.addColumn('number', 'Antall');
        data.addColumn({
            type: 'string',
            role: 'style'
        });
        data.addRows([
          ['Fjell', $mountain, 'green'],
          ['Skog', $forest, 'gold'],
          ['Elv', $river, 'blue'],
          ['Kyst', $coast, 'black'],
          ['Innsjø', $lake, 'yellow'],
          ['Vei', $road, 'silver'],
          ['Industri/Handel', $industry_towns, 'orange'],
          ['Skole/Fritidsområde', $school_recreational_area, 'pink'],
          ['Dyrket mark/Landbruk', $acre_agriculture, 'red'],
          ['Boligområde', $residential_area, 'brown']
        ]);

        var options = {title:'Hvor plukkes det mest?',
                       width:800,
                       height:400};
        var chart = new google.visualization.BarChart(document.getElementById('chart_location'));
        chart.draw(data, options);
      }";

echo "
    </script>
</head>
<body>
	<center>
		<h1> Plast plukk </h1>
		<form action='https://itfag.usn.no/grupper/v19gr2/plast/web/export_excel.php'>
			<input type='submit' value='Last ned xls-fil' />
		</form>
<!--Div that will hold the pie chart-->
    <div id='chart_antall' style='display: inline-block'></div>
    <div id='chart_last_30_days' style='display: inline-block'></div>
    <div id='chart_location' style='display: inline-block'></div>
    <div id='mapid' style='display: inline'></div>
</center>
<script src='map.js'></script>
</body>
</html>";
  ?>
