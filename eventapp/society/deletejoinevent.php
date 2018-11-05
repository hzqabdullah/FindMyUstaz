<?php

 require_once('../admin/connection.php');
 
 $id = $_GET['id'];

 $sql = "DELETE FROM attend WHERE aid = '$id'";

 if(mysqli_query($connection,$sql))
 {
 echo 'Join event has been remove from your profile';
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
 ?>