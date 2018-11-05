<?php

	require_once('connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT count(*) FROM rate WHERE eid = '$id'"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"joinuserCount"=>$s,
		));
	}

	echo json_encode(array('jucount'=>$result));

	mysqli_close($connection);
?>
