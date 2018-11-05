<?php

	require_once('../connection.php');

	$uid = $_GET['uid'];
	
	$sql = "SELECT orderdetails.orderid, orderdetails.ordername, orderdetails.orderaddress, orderdetails.ordercontact, orderdetails.orderemail, orderdetails.paydate, orderproduct.opid, orderproduct.pid, orderproduct.pname, orderproduct.pprice, orderproduct.pqty, orderproduct.pimage, ustaz.ufirstname, ustaz.ulastname, ustaz.uemail, ustaz.ucontact FROM orderdetails JOIN orderproduct ON orderdetails.orderid = orderproduct.orderid JOIN shop ON orderproduct.pid = shop.pid JOIN ustaz ON shop.uid = ustaz.uid WHERE ustaz.uid = '$uid' AND orderdetails.orderstatus = 1 AND orderproduct.deliverystatus = 1";

	
	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r))
	{

		array_push($result,array(
			"orderid"=>$row['orderid'],
			"ordername"=>$row['ordername'],
			"orderaddress"=>$row['orderaddress'],
			"ordercontact"=>$row['ordercontact'],
			"orderemail"=>$row['orderemail'],
			"paydate"=>$row['paydate'],
			"opid"=>$row['opid'],
			"pid"=>$row['pid'],
			"pname"=>$row['pname'],
			"pprice"=>$row['pprice'],
			"pqty"=>$row['pqty'],
			"pimage"=>$row['pimage'],
			"ufirstname"=>$row['ufirstname'],
			"ulastname"=>$row['ulastname'],
			"uemail"=>$row['uemail'],
			"ucontact"=>$row['ucontact'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
