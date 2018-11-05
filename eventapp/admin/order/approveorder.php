<?php

 $orderid = $_GET['orderid'];
 
 $status = 1;

 include "../connection.php";

 $query = "UPDATE orderdetails SET orderstatus = '$status' WHERE orderid = '$orderid'";
 
 if(mysqli_query($connection,$query))
 {
	echo ("<SCRIPT LANGUAGE='JavaScript'>
	window.alert('Customer order has been approved. Product is on delivery')
	window.location.href='displayorder.php';
	</SCRIPT>");
 }
 else
 {
 	echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Failed to approved');
				</SCRIPT>");
 }
 mysqli_close($connection);
?>