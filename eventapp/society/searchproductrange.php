<?php

require_once('../admin/connection.php');

if($_SERVER['REQUEST_METHOD']=='POST')
{

    $range1 = $_POST['range1'];


		$sql = "SELECT shop.pid, shop.pname, shop.pprice, shop.pquantity, shop.pimage, ustaz.uprofilename FROM shop JOIN ustaz ON shop.uid = ustaz.uid WHERE shop.pprice BETWEEN 0 AND '$range1' AND shop.pstatus = 'Available' ORDER BY ABS(shop.pprice) ASC";
	
		$r = mysqli_query($connection, $sql);
	
		
		$result = array();
	
		while($row = mysqli_fetch_array($r)){
	
			
			array_push($result,array(
				"pid"=>$row['pid'],
				"name"=>$row['pname'],
				"price"=>$row['pprice'],
				"quantity"=>$row['pquantity'],
				"profilename"=>$row['uprofilename'],
				"image"=>$row['pimage'],
	
			));
		}
		
	}

	echo json_encode(array('result'=>$result));

	mysqli_close($connection);
?>
