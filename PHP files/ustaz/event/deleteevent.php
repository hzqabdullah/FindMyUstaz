<?php


 $id = $_GET['id']; 

 //Import File Koneksi Database
 require_once('../../admin/connection.php');

 
 $sql = "DELETE event, attend, rate FROM event LEFT JOIN attend ON event.eid = attend.eid LEFT JOIN rate ON attend.eid = rate.eid WHERE event.eid = '$id'";


 if(mysqli_query($connection,$sql))
 {
	 echo 'Event has been remove from the list';
 }
 else
 {
	 echo 'Please try again';
 }

 mysqli_close($connection);
 ?>