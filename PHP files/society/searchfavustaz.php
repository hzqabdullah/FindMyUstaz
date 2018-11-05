<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('../admin/connection.php');

	$id = $_POST['id'];
	$search = $_POST['search'];
	
	$sql = "SELECT * FROM favorite JOIN ustaz ON favorite.uid = ustaz.uid WHERE favorite.sid = '".$id."' AND ustaz.uprofilename LIKE '%".$search."%'";

	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"uid"=>$row['uid'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"profilename"=>$row['uprofilename'],
			"place"=>$row['uplace'],
	
		));
		
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
}
?>
