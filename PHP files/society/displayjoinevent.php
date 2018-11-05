<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('../admin/connection.php');
	
	$result = array();

	$id = $_POST['id'];
	
	$sql = "SELECT attend.aid, event.etitle, event.edate, event.edateend, event.eday, event.etime, event.evenue, event.eimage, ustaz.uprofilename FROM attend JOIN event ON attend.eid = event.eid JOIN ustaz ON event.uid = ustaz.uid WHERE attend.sid = '$id' AND attend.astatus = 0 AND event.estatus = 0 ORDER BY event.edate ASC";
	
	$r = mysqli_query($connection, $sql);
	
	if($r)
	{
		while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"aid"=>$row['aid'],
			"title"=>$row['etitle'],
			"date"=>$row['edate'],
			"dateend"=>$row['edateend'],
			"day"=>$row['eday'],
			"time"=>$row['etime'],
			"venue"=>$row['evenue'],
			"image"=>$row['eimage'],
			"profilename"=>$row['uprofilename'],

		));
	}

	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
}
?>
