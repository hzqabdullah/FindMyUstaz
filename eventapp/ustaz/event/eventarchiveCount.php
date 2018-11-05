<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	
	$sql = "SELECT count(*) FROM event WHERE uid = '$uid' AND estatus = 1";
	$r = mysqli_query($connection, $sql);
	
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"archiveCount"=>$s,
		));
	}

	echo json_encode(array('acount'=>$result));

	mysqli_close($connection);
?>
