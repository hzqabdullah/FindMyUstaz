<?php

 require_once('../admin/connection.php');
 
 $id = $_GET['id'];

 $sql = "DELETE FROM usercart WHERE cid='$id';";

 if(mysqli_query($connection,$sql))
 {
 echo 'Product has been remove from your cart';
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
 ?>