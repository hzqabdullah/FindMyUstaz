<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT count(*) FROM attend JOIN event ON attend.eid = event.eid WHERE attend.sid = '$id' AND attend.astatus = 0 AND event.estatus = 0"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"jointotal"=>$s,
		));
	}

	echo json_encode(array('jcount'=>$result));

	mysqli_close($connection);
?>
