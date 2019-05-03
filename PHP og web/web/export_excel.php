<?php
include_once("../itfag/config.php");
$sql = "SELECT * from registrations";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result);
if(mysqli_num_rows($result) > 0){
	$output .='<table class="table" border="1"
				<tr>
					<th> id </th>
					<th> user_id </th>
					<th> path </th>
					<th> type </th>
					<th> størrelse </th>
					<th> fjell </th>
					<th> skog </th>
					<th> elv </th>
					<th> kyst </th>
					<th> innsjø </th>
                    <th> vei </th>
                    <th> industri </th>
                    <th> skole </th>
                    <th> jordbruk </th>
                    <th> bolig </th>
					<th> Latitude </th>
					<th> Longitude </th>
					<th> Dato </th>
					<th> Tid </th>
				</tr>
	';
	while ($row = mysqli_fetch_array($result)){
		$output .= '
					<tr>
						<td> ' . $row["id"] . '</td>
						<td> ' . $row["user_id"] . '</td>
						<td> ' . $row["path"] . '</td>
						<td> ' . $row["type"] . '</td>
                        <td> ' . $row["size"] . '</td>
						<td> ' . $row["Mountain"] . '</td>
						<td> ' . $row["Forest"] . '</td>
						<td> ' . $row["River"] . '</td>
						<td> ' . $row["Coast"] . '</td>
						<td> ' . $row["Lake"] . '</td>
						<td> ' . $row["Road"] . '</td>
                        <td> ' . $row["Industry_Towns"] . '</td>
                        <td> ' . $row["School_Recreational_area"] . '</td>
                        <td> ' . $row["Acre_Agriculture"] . '</td>
                        <td> ' . $row["Residential_area"] . '</td>
						<td> ' . $row["latitude"] . '</td>
						<td> ' . $row["longitude"] . '</td>
						<td> ' . $row["Date"] . '</td>
						<td> ' . $row["Time"] . '</td>
					</tr>
		';
	}
	$output .= '</table>';
	header('Content-Type: application/xls');
	header('Content-Disposition: attachment; filename=registreringer.xls');
	echo $output;
}

?>
