<?php

	require_once('connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT AVG(rate)FROM rate WHERE eid = '$id'"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0] * 5;
		
		array_push($result,array(
			"rateCount"=>$s,
		));
	}

	echo json_encode(array('rcount'=>$result));

	mysqli_close($connection);
?>
