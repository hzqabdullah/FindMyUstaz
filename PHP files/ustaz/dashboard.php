<?php

	require_once('connection.php');
	
	$uemail = $_GET['uemail'];
	
	$sql = "SELECT * FROM ustaz WHERE uemail = '$uemail'"; //where uid = uid

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"id"=>$row['uid'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"profilename"=>$row['uprofilename'],
			"icno"=>$row['uicno'],
			"dob"=>$row['udob'],
			"place"=>$row['uplace'],
			"contact"=>$row['ucontact'],
			"photo"=>$row['uphoto'],
			"email"=>$row['uemail'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
