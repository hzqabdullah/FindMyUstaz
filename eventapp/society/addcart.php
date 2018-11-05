<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $id = $_POST['id'];
 $pid = $_POST['pid'];
 $price = $_POST['price'];
 $added = $_POST['added'];
 $image = $_POST['image'];
 $profilename = $_POST['profilename'];
 $name = $_POST['name'];
 
 $sql = "select * from usercart where sid = '$id' and pid = '$pid'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
 if(isset($check))
 {
	 $query = "UPDATE usercart SET cadded = '$added', pqty = pqty + 1 WHERE sid = '$id' AND pid = '$pid'";
	 $result = mysqli_query($connection, $query);
	 
	 if($result)
	 {
		echo "Product quantity is added by one"; 
	 }
 }
 else
 {
	 $sql = "INSERT INTO usercart (pid, pname, pprice, pqty, pimage, ustazshop, sid, cadded) VALUES ('$pid', '$name','$price', 1 ,'$image','$profilename', '$id','$added')";
				
	 $result = mysqli_query($connection, $sql);
			
	 if($result)
	 {
		echo "Product is added into your cart list";
	 }
	 else
	 {
		echo "Error" .$query. "<br>" . $connection->error;
	 }
   }
}
 
 mysqli_close($connection);

?>
