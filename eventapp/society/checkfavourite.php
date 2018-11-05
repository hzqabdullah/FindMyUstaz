<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

include '../admin/connection.php';
 

$id = $_POST['id'];
$uid = $_POST['uid'];
 
 $sql = "select * from favorite where sid = '$id' and uid = '$uid'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
 if(isset($check))
 {
	 echo "You already followed";
 }
 else
 {
	 echo "Follow";
 }
 
 }

 
 mysqli_close($connection);

?>
