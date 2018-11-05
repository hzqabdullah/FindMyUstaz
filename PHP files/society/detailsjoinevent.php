<?php

	require_once('../admin/connection.php');
	
	$result = array();

	$id = $_GET['id'];
	
	$sql = "SELECT event.eid, event.etitle, event.edate, event.edateend, event.eday, event.etime, event.evenue, event.eplace, event.elatitude, event.elongitude, event.eimage, ustaz.uprofilename , ustaz.ufirstname, ustaz.ulastname, ustaz.uplace, ustaz.uphoto FROM attend JOIN event ON attend.eid = event.eid JOIN ustaz ON event.uid = ustaz.uid WHERE attend.aid = '$id'";
	
	$r = mysqli_query($connection, $sql);
	
	if($r)
	{
		while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"eid"=>$row['eid'],
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
			"profilename"=>$row['uprofilename'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"place"=>$row['uplace'],
			"photo"=>$row['uphoto'],

		));
	}

}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);

?>
