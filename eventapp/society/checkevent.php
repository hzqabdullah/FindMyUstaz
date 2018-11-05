<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

include '../admin/connection.php';
 

$id = $_POST['id'];
$eid = $_POST['eid'];
 
 $sql = "select * from attend where sid = '$id' and eid = '$eid'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
 if(isset($check))
 {
	 echo "You already joined the event";
 }
 else
 {
	 echo "Thank you for joining the event. Please check your profile";
 }
 
 }

 
 mysqli_close($connection);

?>
