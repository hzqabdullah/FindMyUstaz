<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('../admin/connection.php');
	
	$result = array();

	$location = $_POST['location'];
	$latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
	
	$Lat = $latitude / 180 * M_PI;
	$Long = $longitude / 180 * M_PI;
	
	$Distance = 20;
	
	$sql = "SELECT DISTINCT event.eid, event.eimage, event.evenue, event.edate, event.edateend, event.eday, event.etime, event.etitle, ustaz.uprofilename, 
	(6367.41 * SQRT(2 * (1-Cos(RADIANS(event.elatitude)) * Cos(".$Lat.") * (Sin(RADIANS(event.elongitude))*Sin(".$Long.") + Cos(RADIANS(event.elongitude)) * Cos(".$Long.")) - Sin(RADIANS(event.elatitude)) * Sin(".$Lat.")))) AS distance FROM event INNER JOIN ustaz ON event.uid = ustaz.uid WHERE  event.estatus = 0 AND (6367.41 * SQRT(2 * (1 - Cos(RADIANS(event.elatitude)) * Cos(".$Lat.") * (Sin(RADIANS(event.elongitude)) * Sin(".$Long.") + cos(RADIANS(event.elongitude)) * Cos(".$Long.")) - Sin(RADIANS(event.elatitude)) * Sin(".$Lat."))) <= ".$Distance.") ORDER BY event.edate ASC";
	
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
			"image"=>$row['eimage'],
			"distance"=>round($row['distance'], 2),
			"profilename"=>$row['uprofilename'],

		));
	}

	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
}
?>
