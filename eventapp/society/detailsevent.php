<?php


	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT * FROM event 
	INNER JOIN ustaz ON event.uid = ustaz.uid WHERE event.eid = '".$id."'";

	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"eid"=>$row['eid'],
			"category"=>$row['ecategory'],
			"title"=>$row['etitle'],
			"date"=>$row['edate'],
			"dateend"=>$row['edateend'],
			"day"=>$row['eday'],
			"time"=>$row['etime'],
			"venue"=>$row['evenue'],
			"eplace"=>$row['eplace'],
			"latitude"=>$row['elatitude'],
			"longitude"=>$row['elongitude'],
			"image"=>$row['eimage'],
			"uid"=>$row['uid'],
			"profilename"=>$row['uprofilename'],

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);

?>
