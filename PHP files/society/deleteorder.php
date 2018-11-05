<?php

 require_once('../admin/connection.php');
 
 $id = $_GET['id'];

 $sql = "DELETE FROM payment WHERE payid='$id';";

 if(mysqli_query($connection,$sql))
 {
	 $sql2 = "DELETE FROM sales WHERE payid='$id';";

	 if(mysqli_query($connection,$sql2))
	 {
		 $sql3 = "DELETE FROM orderdetails WHERE orderid='$id';";

		 if(mysqli_query($connection,$sql3))
		 { 
			 $sql4 = "DELETE FROM orderproduct WHERE orderid='$id';";
	
			 if(mysqli_query($connection,$sql4))
			 {
				 echo 'The order has been remove from your account';
			 }
		 }
	 }
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
 ?>