<?php

	require_once('../admin/connection.php');
	
	
	$sql = "SELECT * FROM ustaz ORDER BY uprofilename"; 

	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){


		array_push($result,array(
			"uid"=>$row['uid'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"profilename"=>$row['uprofilename'],
			"place"=>$row['uplace'],
			"photo"=>$row['uphoto'],

			
		));
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
