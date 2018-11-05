<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT usercart.pprice, usercart.pqty, usercart.cadded, shop.pname, shop.pdesc, shop.pimage, ustaz.uprofilename FROM usercart JOIN shop ON usercart.pid = shop.pid JOIN ustaz ON shop.uid = ustaz.uid WHERE usercart.cid = '".$id."'";

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		$totalprice = $row['pprice'] * $row['pqty'];

		array_push($result,array(
			"price"=>$row['pprice'],
			"quantity"=>$row['pqty'],
			"added"=>$row['cadded'],
			"name"=>$row['pname'],
			"profilename"=>$row['uprofilename'],
			"totalprice"=>round($totalprice, 2),
			"desc"=>$row['pdesc'],
			"image"=>$row['pimage'],
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
