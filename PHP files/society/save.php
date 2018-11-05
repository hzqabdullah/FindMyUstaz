<?php


require "../admin/connection.php";

$id = $_POST["id"];
$fullname = $_POST["fullname"];
$address = $_POST["address"];
$email = $_POST["email"];
$contact = $_POST["contact"];
$totalprice = $_POST["totalprice"];

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
		$sqlInsertPay = "INSERT INTO payment(sid, payfullname, payaddress, paycontact, payemail, payqty, payprice) VALUES('$id','$fullname','$address','$contact','$email','$quantity','$totalprice')";
		$result = mysqli_query($connection, $sqlInsertPay);
					
		if($result)
		{
			$payment_id = $connection->insert_id;
			
			$sqlSelectCart = "SELECT * FROM usercart WHERE sid = '$id'";
			$result1 = mysqli_query($connection, $sqlSelectCart);
			
			while($row1 = mysqli_fetch_array($result1))
			{
			
				$pid = $row1['pid'];
				$pprice = $row1['pprice'];
				$pqty = $row1['pqty'];
				
				$sqlInsertSale = "INSERT INTO sales(payid, pid, pprice, pqty, saleaddress) VALUES
    			('$payment_id','$pid','$pprice','$pqty','$address')";
    			
    			$result2 = mysqli_query($connection, $sqlInsertSale);
				
				if($result2)
    			{
					$sqlUpdateProduct = "UPDATE shop LEFT JOIN sales ON shop.pid = sales.pid SET shop.pquantity = (shop.pquantity - sales.pqty) 
					WHERE shop.pid = '$pid'";
					
					$result3 = mysqli_query($connection, $sqlUpdateProduct);
				}
 
    		}
			
			echo "The order has been made. Please make a payment for product shipping";  
			
		}
		else
		{
			echo "Error" .$query. "<br>" . $connection->error;
		}				
	}
}

 
$connection->close();

?>