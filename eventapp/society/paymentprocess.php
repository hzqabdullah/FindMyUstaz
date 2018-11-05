<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $pay_id = $_POST['pay_id'];
 $paydate = $_POST['paydate'];

 $sql = "UPDATE payment SET paystatus = 1 WHERE payid = '$pay_id'";
				
	 $result = mysqli_query($connection, $sql);
			
	 if($result)
	 {
		 $sql1 = "UPDATE orderdetails SET paystatus = 1, paydate = '$paydate' WHERE orderid = '$pay_id'";	
		 $result1 = mysqli_query($connection, $sql1);		
		 if($result1)
		 
		 echo "Thank you for your payment. Your products are ready to be shipping";
	 }
	 else
	 {
		echo "No product available" . $connection->error;
	 }
   }

 
 mysqli_close($connection);

?>
