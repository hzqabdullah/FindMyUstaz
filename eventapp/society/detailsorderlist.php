<?php

	require_once('../admin/connection.php');

	$id = $_GET['id'];
	
	$sql = "SELECT * FROM sales WHERE payid = '$id'";

	$r = mysqli_query($connection, $sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){

		$totalprice = $row['pprice'] * $row['pqty'];
		
		array_push($result,array(
		
			"price"=>$row['pprice'],
			"quantity"=>$row['pqty'],
			"name"=>$row['pname'],
			"profilename"=>$row['ustazshop'],
			"totalprice"=>round($totalprice, 2),

		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
