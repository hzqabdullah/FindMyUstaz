<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT * FROM shop WHERE uid = '$uid' ORDER BY pstatus, pname ASC"; 
	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"id"=>$row['pid'],
			"name"=>$row['pname'],
			"price"=>$row['pprice'],
			"quantity"=>$row['pquantity'],
			"status"=>$row['pstatus'],
			"update"=>$row['pupdate'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
