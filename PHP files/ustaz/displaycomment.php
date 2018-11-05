<?php

	require_once('connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT rate.comment, society.sprofilename, society.sphoto FROM rate JOIN society ON rate.sid = society.sid WHERE rate.eid = '$id'";
	
	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r))
	{

		array_push($result,array(
			"comment"=>$row['comment'],
			"user"=>$row['sprofilename'],
			"userphoto"=>$row['sphoto'],

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
