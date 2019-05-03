<?php
include_once("config.php");
$i=0;
$query = mysqli_query($conn, "SELECT latitude, longitude FROM registrations; ");
//Lager en javascript string som skal evalueres i kart.js
while($rad=mysqli_fetch_array($query)){
    echo "stedTabell[".$i."]={lat: ".$rad['latitude'].", lng: ".$rad['longitude']."}; ";
    $i++;
}
?>
