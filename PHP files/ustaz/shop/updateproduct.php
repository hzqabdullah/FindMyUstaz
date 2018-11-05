<?php

require_once('../../admin/connection.php');


	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$target_dir = "product/";
		$path = $target_dir."".rand()."_".time().".jpeg";
		$actualpath = "http://fmu.000webhostapp.com/ustaz/shop/$path";;
	
		$id = $_POST['id'];
		$name = $_POST['name'];
		$price = $_POST['price'];
		$desc = $_POST['desc'];
		$image = $_POST['image'];
		$quantity = $_POST['quantity'];
		$status = $_POST['status'];
		$update = $_POST['update'];
		
		$sql = "SELECT pimage FROM shop WHERE pid = '$id'";
		
		$r = mysqli_query($connection, $sql);
		$row = mysqli_fetch_array($r);
		$currentphoto = $row['pimage'];

		if(!empty($image))
		{
			$sql1 = "UPDATE shop SET pname = '$name', pprice = '$price', pimage = '$actualpath', pdesc = '$desc', pquantity = '$quantity', pstatus = '$status', pupdate = '$update' WHERE pid = $id;";
	
		
			if(mysqli_query($connection,$sql1))
			{
				file_put_contents($path, base64_decode($image));
				echo 'Product information has been updated';
			}
			else
			{
				echo 'Please try again';
			}
		}
		
		if(empty($image))
		{
	
			$sql2 = "UPDATE shop SET pname = '$name', pprice = '$price', pimage = '$currentphoto', pdesc = '$desc', pquantity = '$quantity', pstatus = '$status', pupdate = '$update' WHERE pid = $id;";
	
		
			if(mysqli_query($connection,$sql2))
			{
				echo 'Product information has been updated';
			}
			else
			{
				echo 'Please try again';
			}
		}
	}
	mysqli_close($connection);
?>
