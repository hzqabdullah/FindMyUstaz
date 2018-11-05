<?php

	require_once('../admin/connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT * FROM shop WHERE uid = '$uid' AND pstatus = 'Available' ORDER BY pname ASC"; //where uid = uid

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"pid"=>$row['pid'],
			"name"=>$row['pname'],
			"price"=>$row['pprice'],
			"quantity"=>$row['pquantity'],
			"description"=>$row['pdesc'],
			"image"=>$row['pimage'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
