<?php

 	$orderid = $_POST['orderid'];
	$ordername = $_POST['ordername'];
	$orderaddress = $_POST['orderaddress'];
	$ordercontact = $_POST['ordercontact'];
	$orderemail = $_POST['orderemail'];
	$paydate = $_POST['paydate'];
	$deliverdate = $_POST['deliverdate'];
	$opid = $_POST['opid'];
	$pid = $_POST['pid'];
	$pname = $_POST['pname'];
	$pprice = $_POST['pprice'];
	$pqty = $_POST['pqty'];
	$pimage = $_POST['pimage'];
	$ufirstname = $_POST['ufirstname'];
	$ulastname = $_POST['ulastname'];
	$uemail = $_POST['uemail'];
	$ucontact = $_POST['ucontact'];

 	$status = 1;

	 include "../connection.php";
	
	 $query = "UPDATE orderproduct SET deliverystatus = '$status' WHERE opid = '$opid'";
	 
	 if(mysqli_query($connection,$query))
	 {
		 $sqlUpdateProduct = "UPDATE shop LEFT JOIN orderproduct ON shop.pid = orderproduct.pid SET shop.pquantity = (shop.pquantity - orderproduct.pqty) 		         WHERE shop.pid = '$pid'";
						
		 $result = mysqli_query($connection, $sqlUpdateProduct);
						
		 if($result)
		 {
			$sql = "INSERT INTO transaction(orderid, ordername, orderaddress, ordercontact, orderemail, paydate, deliverdate, opid, pid, pname, pprice, pqty, pimage, ufirstname, ulastname, uemail, ucontact) VALUES ('$orderid','$ordername','$orderaddress','$ordercontact','$orderemail','$paydate', '$deliverdate', '$opid','$pid', '$pname', '$pprice', '$pqty', '$pimage', '$ufirstname', '$ulastname', '$uemail', '$ucontact')";
					
			$result1 = mysqli_query($connection, $sql);
			
			echo "Product will be send to the customer. Product quantity will be deducted from your shop.";
		 }
		 else
		 {
			echo "No product available.";
		 }
			
	 }
	 
	 else
	 {
		echo "Failed to approved";
	 }
	 
	 mysqli_close($connection);
?>