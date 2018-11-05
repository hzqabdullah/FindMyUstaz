<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('../admin/connection.php');

	$search = $_POST['search'];
	
	$sql = "SELECT * FROM ustaz WHERE uprofilename LIKE '%".$search."%' OR uplace LIKE '%".$search."%' OR ufirstname LIKE '%".$search."%' OR ulastname LIKE '%".$search."%' 
	ORDER BY uprofilename";

	$r = mysqli_query($connection, $sql);

	
	$result = array();

	while($row = mysqli_fetch_array($r)){

		
		array_push($result,array(
			"uid"=>$row['uid'],
			"firstname"=>$row['ufirstname'],
			"lastname"=>$row['ulastname'],
			"profilename"=>$row['uprofilename'],
			"icno"=>$row['uicno'],
			"dob"=>$row['udob'],
			"place"=>$row['uplace'],
			"contact"=>$row['ucontact'],
			"email"=>$row['uemail'],

		));
		
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
}
?>
