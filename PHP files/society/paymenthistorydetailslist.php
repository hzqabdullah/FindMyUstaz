<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT * FROM orderproduct WHERE orderid = '$id'";

	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){

		$totalprice = $row['pprice'] * $row['pqty'];
		
		$status = $row['deliverystatus'];
		$newstatus = "";
		
		if($status == 0)
		{
			$newstatus = "Product is not yet shipping to the customer";
		}
		if($status == 1)
		{
			$newstatus = "Product is shipped to customer";
		}
		
		array_push($result,array(
		
			"price"=>$row['pprice'],
			"quantity"=>$row['pqty'],
			"name"=>$row['pname'],
			"profilename"=>$row['ustazshop'],
			"totalprice"=>round($totalprice, 2),
			"status"=>$newstatus,

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
