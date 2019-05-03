<?php
include_once("../itfag/config.php");

$sql = "SELECT path from registrations";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result);
if(mysqli_num_rows($result) > 0){
	$output .= '<table class="table"><tr>';
	$teller = 0;
	while ($row = mysqli_fetch_array($result)){
		if($teller >= 5){
			$output .= '</tr><tr>';
			$teller = 0;
		}
		$output .= '<td><img src="'. $row["path"] . '" height="400"></td>';
		$teller++;
	}
	$output .= '</tr></table>';
	echo $output;
}

?>
