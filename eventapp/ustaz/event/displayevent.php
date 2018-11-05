<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT * FROM event WHERE uid = '$uid' AND estatus = 0 ORDER BY edate ASC";

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r))
	{

		array_push($result,array(
			"id"=>$row['eid'],
			"category"=>$row['ecategory'],
			"title"=>$row['etitle'],
			"date"=>$row['edate'],
			"dateend"=>$row['edateend'],
			"day"=>$row['eday'],
			"time"=>$row['etime'],
			"venue"=>$row['evenue'],
			"place"=>$row['eplace'],
			"latitude"=>$row['elatitude'],
			"longitude"=>$row['elongitude'],
			"image"=>$row['eimage'],
			"update"=>$row['eupdate'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
