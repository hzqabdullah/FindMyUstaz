<?php


	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT * FROM payment WHERE payid = '$id'";

	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"payid"=>$row['payid'],
			"payfullname"=>$row['payfullname'],
			"payemail"=>$row['payemail'],
			"payqty"=>$row['payqty'],
			"payprice"=>$row['payprice'],
			"paycreated"=>$row['paycreated'],
			"paycontact"=>$row['paycontact'],
			"payaddress"=>$row['payaddress'],

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
