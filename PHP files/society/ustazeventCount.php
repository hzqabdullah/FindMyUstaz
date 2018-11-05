<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	
	$sql = "SELECT count(*) FROM event WHERE uid = '$uid' AND estatus = 0";
	$r = mysqli_query($connection, $sql);
	
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"eventCount"=>$s,
		));
	}

	echo json_encode(array('ecount'=>$result));

	mysqli_close($connection);
?>
