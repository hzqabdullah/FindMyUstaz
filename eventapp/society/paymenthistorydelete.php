<?php

 require_once('../admin/connection.php');
 
 $id = $_GET['id'];

 $sql = "DELETE FROM payment WHERE payid='$id';";

 if(mysqli_query($connection,$sql))
 {
	 $sql2 = "DELETE FROM sales WHERE payid='$id';";

	 if(mysqli_query($connection,$sql2))
	 {
		 echo 'The previous payment has been remove from your account';
	 }
 }
 else
 {
 	echo 'Please try again';
 }

 mysqli_close($connection);
 ?>