<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT count(*) FROM orderdetails JOIN orderproduct ON orderdetails.orderid = orderproduct.orderid JOIN shop ON orderproduct.pid = shop.pid JOIN ustaz ON shop.uid = ustaz.uid WHERE ustaz.uid = '$uid' AND orderdetails.orderstatus = 1 AND orderproduct.deliverystatus = 0"; 
	
	$r = mysqli_query($connection, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($r)){
		
		$s = $row[0];
		
		array_push($result,array(
			"orderCount"=>$s,
		));
	}

	echo json_encode(array('ocount'=>$result));

	mysqli_close($connection);
?>
