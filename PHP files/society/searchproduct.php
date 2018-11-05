<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('../admin/connection.php');

	$product = $_POST['product'];
	
	$sql = "SELECT * FROM shop JOIN ustaz ON shop.uid = ustaz.uid WHERE shop.pname LIKE '%".$product."%' OR ustaz.uprofilename LIKE '%".$product."%' OR ustaz.ufirstname LIKE '%".$product."%' OR ustaz.ulastname LIKE '%".$product."%' AND shop.pstatus = 'Available' ORDER BY shop.pname";

	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
				"pid"=>$row['pid'],
				"name"=>$row['pname'],
				"price"=>$row['pprice'],
				"quantity"=>$row['pquantity'],
				"image"=>$row['pimage'],
				"uid"=>$row['uid'],
				"profilename"=>$row['uprofilename'],

		));
		
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
}
?>
