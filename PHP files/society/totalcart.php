<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT SUM(pprice*pqty) FROM usercart WHERE sid= '$id'"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"carttotal"=>round($s, 2),
		));
	}

	echo json_encode(array('cartcount'=>$result));

	mysqli_close($connection);
?>
