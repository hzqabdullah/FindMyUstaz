<?php

	$id = $_GET['id'];

	//Importing database
	require_once('../connection.php');

	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM event WHERE eid = $id";

	//Mendapatkan Hasil
	$r = mysqli_query($connection, $sql);

	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
			"id"=>$row['eid'],
			"category"=>$row['ecategory'],
			"title"=>$row['etitle'],
			"date"=>$row['edate'],
			"dateend"=>$row['edateend'],
			"day"=>$row['eday'],
			"time"=>$row['etime'],
			"venue"=>$row['evenue'],
			"place"=>$row['eplace'],
			"image"=>$row['eimage'],
			"latitude"=>$row['elatitude'],
			"longitude"=>$row['elongitude'],
			"insert"=>$row['einsert'],
			"update"=>$row['eupdate']
		
		));

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
