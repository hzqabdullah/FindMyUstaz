<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT count(*) FROM attend WHERE eid = '$id'"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"joinuser"=>$s,
		));
	}

	echo json_encode(array('jucount'=>$result));

	mysqli_close($connection);
?>
