<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	
	$sql = "SELECT count(*) FROM shop WHERE uid = '$uid'";
	$r = mysqli_query($connection, $sql);
	
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"productCount"=>$s,
		));
	}

	echo json_encode(array('pcount'=>$result));

	mysqli_close($connection);
?>
