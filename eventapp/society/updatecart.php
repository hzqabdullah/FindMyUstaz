<?php

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
	
		$cid = $_POST['cid'];
		$quantity = $_POST['quantity'];
		$added = $_POST['added'];
    
    
		require_once('../admin/connection.php');

		
		$sql = "UPDATE usercart SET pqty = '$quantity', cadded = '$added' WHERE cid = $cid;";

		
        if(mysqli_query($connection,$sql))
        {
			echo 'Product quantity has been updated';
		}else
		{
			echo 'Please try again';
		}

		mysqli_close($connection);
	}
?>
