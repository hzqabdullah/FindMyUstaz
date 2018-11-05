<?php

	include '../admin/connection.php';
	
	$email = $_GET['email'];
	
	$sql = "SELECT * FROM society WHERE semail = '$email'"; //where uid = uid

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"id"=>$row['sid'],
			"profilename"=>$row['sprofilename'],
			"details"=>$row['sdetails'],
			"location"=>$row['slocation'],
			"latitude"=>$row['slatitude'],
			"longitude"=>$row['slongitude'],
			"contact"=>$row['scontact'],
			"email"=>$row['semail'],
			"photo"=>$row['sphoto'],
			"password"=>base64_decode($row['spassword']),
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
