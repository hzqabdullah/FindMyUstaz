<?php

	require_once('../admin/connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT * FROM event WHERE uid = '$uid' AND estatus = 0 ORDER BY edate ASC "; //where uid = uid

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"eid"=>$row['eid'],
			"category"=>$row['ecategory'],
			"title"=>$row['etitle'],
			"date"=>$row['edate'],
			"day"=>$row['eday'],
			"dateend"=>$row['edateend'],
			"time"=>$row['etime'],
			"venue"=>$row['evenue'],
			"image"=>$row['eimage'],
		
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
