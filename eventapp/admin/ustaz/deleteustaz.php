<?php

 $uid = $_GET['uid'];

 //MAJOR TERMINATION
 include "../connection.php";

 //Membuat SQL Query
 $sql1 = "DELETE FROM ustaz WHERE uid = '$uid'";
 $sql2 = "DELETE event, attend, rate FROM event LEFT JOIN attend ON event.eid = attend.eid LEFT JOIN rate ON attend.eid = rate.eid WHERE event.uid = '$uid'";
 $sql3 = "DELETE shop, usercart FROM shop LEFT JOIN usercart ON shop.pid = usercart.pid WHERE shop.uid = '$uid'";
 $sql4 = "DELETE FROM favorite WHERE uid = '$uid'";
 $sql5 = "DELETE FROM tmpustaz WHERE uid = '$uid'";


 //Menghapus Nilai pada Database
 if(mysqli_query($connection,$sql1))
 {
	 if(mysqli_query($connection,$sql2))
	 if(mysqli_query($connection,$sql3))
	 if(mysqli_query($connection,$sql4))
	 if(mysqli_query($connection,$sql5))
	 
	echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Ustaz account has been terminated')
				window.location.href='displayustaz.php';
				</SCRIPT>");
 }
 else
 {
 	echo 'Please try again';
 }

 mysqli_close($connection);
?>