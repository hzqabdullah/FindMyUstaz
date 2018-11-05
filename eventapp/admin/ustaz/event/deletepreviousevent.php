<?php

 $eid = $_GET['eid'];
 $uid = $_GET['uid'];

 
 include "../../connection.php";


 $sql = "DELETE event, attend, rate FROM event LEFT JOIN attend ON event.eid = attend.eid LEFT JOIN rate ON attend.eid = rate.eid WHERE event.eid = '$eid'";

 if(mysqli_query($connection,$sql))
 {
 echo ("<SCRIPT LANGUAGE='JavaScript'>
	window.alert('Ustaz event has been deleted')
	window.location.href='displaypreviousevent.php?uid=".$uid."';
	</SCRIPT>");
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
?>