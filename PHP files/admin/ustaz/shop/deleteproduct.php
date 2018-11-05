<?php

 $pid = $_GET['pid'];
 $uid = $_GET['uid'];

 
 include "../../connection.php";


 $sql = "DELETE shop, usercart FROM shop LEFT JOIN usercart ON shop.pid = usercart.pid WHERE shop.pid = '$pid'";

 if(mysqli_query($connection,$sql))
 {
  echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Ustaz product has been delete')
				window.location.href='displayproduct.php?uid=".$uid."';
				</SCRIPT>");
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
?>