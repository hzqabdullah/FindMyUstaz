<?php

require_once('../admin/connection.php');

	
	$sql = "SELECT * FROM shop JOIN ustaz ON shop.uid = ustaz.uid WHERE shop.pstatus = 'Available' ORDER BY shop.pupdate DESC";
	
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

?>
