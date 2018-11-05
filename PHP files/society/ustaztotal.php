<?php

	require_once('../admin/connection.php');

	
	
	$sql = "SELECT count(*) FROM ustaz"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"ustazCount"=>$s,
		));
	}

	echo json_encode(array('ucount'=>$result));

	mysqli_close($connection);
?>
