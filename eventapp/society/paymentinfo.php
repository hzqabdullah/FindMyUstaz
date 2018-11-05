<?php


require "../admin/connection.php";

$id = $_POST["id"];
$fullname = $_POST["fullname"];
$address = $_POST["address"];
$email = $_POST["email"];
$contact = $_POST["contact"];
$totalprice = $_POST["totalprice"];
$created = $_POST["created"];

$sqlSUMQty = "SELECT SUM(pqty) FROM usercart WHERE sid = '$id'";
		
$r = mysqli_query($connection, $sqlSUMQty);
$row = mysqli_fetch_array($r);
$quantity = $row[0];


if ($totalprice == 0) 
{
   echo "Your cart cannot be empty. Please insert product into your cart."; 
}

else
{
	if ($quantity == 0) 
	{
	   echo "Your product quantity cannot be zero"; 
	}
	else
	{
		$sqlInsertPay = "INSERT INTO payment(sid, payfullname, payaddress, paycontact, payemail, payqty, payprice, paycreated) VALUES('$id','$fullname','$address','$contact','$email','$quantity','$totalprice', '$created')";
		$result = mysqli_query($connection, $sqlInsertPay);
		
		$payment_id = $connection->insert_id;
		
		if($result)
		{
			$sqlInsertOrder = "INSERT INTO orderdetails(orderid, sid, ordername, orderaddress, ordercontact, orderemail, orderqty, orderprice) VALUES('$payment_id','$id','$fullname','$address','$contact','$email','$quantity','$totalprice')";
		$resultOrder = mysqli_query($connection, $sqlInsertOrder);
					
			if($resultOrder)
			{
				$sqlSelectCart = "SELECT * FROM usercart WHERE sid = '".$id."'";
				$result1 = mysqli_query($connection, $sqlSelectCart);
				
				while($row1 = mysqli_fetch_array($result1))
				{
				
					$pid = $row1['pid'];
					$pname = $row1['pname'];
					$pprice = $row1['pprice'];
					$pqty = $row1['pqty'];
					$pimage = $row1['pimage'];
					$ustaz = $row1['ustazshop'];
					
					$sqlInsertSale = "INSERT INTO sales(payid, pid, pname, pprice, pqty, pimage, ustazshop) VALUES
					('$payment_id','$pid','$pname','$pprice','$pqty','$pimage','$ustaz')";
					
					$result2 = mysqli_query($connection, $sqlInsertSale);
					
					if($result2)
					{
						$sqlOP = "INSERT INTO orderproduct(orderid, pid, pname, pprice, pqty, pimage, ustazshop) VALUES
						('$payment_id','$pid','$pname','$pprice','$pqty','$pimage','$ustaz')";
						
						$result3 = mysqli_query($connection, $sqlOP);
						
						if($result3)
						{
							$sqlDelete = "DELETE FROM usercart WHERE sid='$id';";
							$result4 = mysqli_query($connection, $sqlDelete);
			
							if($result4);
						}
					}
	 
				}
				
				echo "The order has been made. Check your order list. Please make a payment for product shipping";  
			}
			
		}
		else
		{
			echo "Error" .$query. "<br>" . $connection->error;
		}				
	}
}

 
$connection->close();

?>