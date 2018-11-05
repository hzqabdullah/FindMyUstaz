<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT * FROM shop JOIN ustaz ON shop.uid = ustaz.uid WHERE pid = '$id'";

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"pid"=>$row['pid'],
			"name"=>$row['pname'],
			"price"=>$row['pprice'],
			"quantity"=>$row['pquantity'],
			"desc"=>$row['pdesc'],
			"uid"=>$row['uid'],
			"profilename"=>$row['uprofilename'],
			"image"=>$row['pimage'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
