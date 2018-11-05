<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT payment.payfullname, payment.payemail, payment.payqty, payment.payprice, payment.paycreated, payment.paycontact, payment.payaddress, orderdetails.orderstatus, orderdetails.paydate FROM payment JOIN orderdetails ON payment.payid = orderdetails.orderid  WHERE payment.payid = '$id'";

	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){

		$status = $row['orderstatus'];
		$newstatus = "";
		
		if($status == 0)
		{
			$newstatus = "Your products is not ready on delivery";
		}
		if($status == 1)
		{
			$newstatus = "Your products is ready on delivery";
		}
		
		array_push($result,array(
			"paystatus"=>$newstatus,
			"payfullname"=>$row['payfullname'],
			"payemail"=>$row['payemail'],
			"payqty"=>$row['payqty'],
			"payprice"=>$row['payprice'],
			"paycreated"=>$row['paycreated'],
			"paycontact"=>$row['paycontact'],
			"payaddress"=>$row['payaddress'],
			"paydate"=>$row['paydate'],

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>