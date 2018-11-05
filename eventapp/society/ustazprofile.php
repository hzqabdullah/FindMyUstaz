<?php

	require_once('../admin/connection.php');
	
	$uid = $_GET['uid'];
	
	$sql = "SELECT * FROM ustaz WHERE uid = '$uid'";

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"uid"=>$row['uid'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"profilename"=>$row['uprofilename'],
			"icno"=>$row['uicno'],
			"dob"=>$row['udob'],
			"place"=>$row['uplace'],
			"contact"=>$row['ucontact'],
			"email"=>$row['uemail'],
			"photo"=>$row['uphoto'],

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
