<?php

 $sid = $_GET['sid'];


 include "../connection.php";

 $sql1 = "DELETE FROM society WHERE sid = '$sid'";
 $sql2 = "DELETE FROM usercart WHERE sid = '$sid'";
 $sql3 = "DELETE FROM favorite WHERE sid = '$sid'";
 $sql4 = "DELETE FROM attend WHERE sid = '$sid'";
 $sql4 = "DELETE FROM rate WHERE sid = '$sid'";
 $sql5 = "DELETE payment, sales FROM payment LEFT JOIN sales ON payment.payid = sales.payid WHERE payment.sid = '$sid'";



 if(mysqli_query($connection,$sql1))
 {
	 if(mysqli_query($connection,$sql2))
	 if(mysqli_query($connection,$sql3))
	 if(mysqli_query($connection,$sql4))
	 if(mysqli_query($connection,$sql5))
	 
	 echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('User has been successfully removed')
				</SCRIPT>");
	 header('location: displaysociety.php');
 }
 else
 {
 	echo 'Please try again';
 }

 mysqli_close($connection);
?>