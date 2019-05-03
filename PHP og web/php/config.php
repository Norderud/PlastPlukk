<?php
$host="localhost";
$user="v19bau2";
$password="pw2";
$db = "v19badb2";

$conn = mysqli_connect($host,$user,$password,$db);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit;
  }else{  //echo "Connect"; 
   }
 mysqli_set_charset($conn, 'utf8');
?>