<?php

	require_once('../../admin/connection.php');
	
	if($_SERVER['REQUEST_METHOD']=='POST'){

		$target_dir = "product/";
		$path = $target_dir."".rand()."_".time().".jpeg";
		$actualpath = "http://fmu.000webhostapp.com/ustaz/shop/$path";


		$name = $_POST['name'];
		$price = $_POST['price'];
		$desc = $_POST['desc'];
		$quantity = $_POST['quantity'];
		$image = $_POST['image'];
		$status = $_POST['status'];
		$insert = $_POST['insert'];
		$update = $_POST['update'];
		$uid = $_POST['uid'];


 		if(!empty($image))
		 {
			$sql1 = "INSERT INTO shop (pname, pprice, pimage, pdesc, pquantity, pstatus, pinsert, pupdate, uid) 
			VALUES('$name', '$price', '$actualpath', '$desc', '$quantity', '$status', '$insert', '$update', '$uid')";
		
	
			if(mysqli_query($connection,$sql1))
			{
				file_put_contents($path, base64_decode($image));
				echo 'New product has been added into the shop';
			}
			else
			{
				echo 'Please try again';
			}
		 }
		 
		 if(empty($image))
		 {
			$sql2 = "INSERT INTO shop (pname, pprice, pdesc, pquantity, pstatus, pinsert, pupdate, uid) 
			VALUES('$name', '$price', '$desc', '$quantity', '$status', '$insert', '$update', '$uid')";
		
	
			if(mysqli_query($connection,$sql2))
			{
				echo 'New product has been added into the shop';
			}
			else
			{
				echo 'Please try again';
			}
		 }
		 
		mysqli_close($connection);
	}
?>