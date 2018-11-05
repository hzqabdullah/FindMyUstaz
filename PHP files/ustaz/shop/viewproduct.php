<?php

	$id = $_GET['id'];

	//Importing database
	require_once('../connection.php');

	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM shop WHERE pid=$id";

	//Mendapatkan Hasil
	$r = mysqli_query($connection, $sql);

	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
			"id"=>$row['pid'],
			"name"=>$row['pname'],
			"price"=>$row['pprice'],
			"desc"=>$row['pdesc'],
			"quantity"=>$row['pquantity'],
			"image"=>$row['pimage'],
			"status"=>$row['pstatus'],
			"insert"=>$row['pinsert'],
			"update"=>$row['pupdate'],
		
		));

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
