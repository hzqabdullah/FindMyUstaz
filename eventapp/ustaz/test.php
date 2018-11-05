<?php

	require_once('connection.php');
	
	$sql = "SELECT * FROM test"; 
	
	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		array_push($result,array(
			"id"=>$row['id'],
			"name"=>$row['name'],
			"details"=>$row['details'],
			"age"=>$row['age'],
			"email"=>$row['email']
		));
	}

	echo json_encode($result);

	mysqli_close($connection);
?>
