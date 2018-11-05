<?php


 $id = $_GET['id'];

 //Import File Koneksi Database
 require_once('../../admin/connection.php');

 //Membuat SQL Query
 "DELETE shop, usercart FROM shop LEFT JOIN usercart ON shop.pid = usercart.pid WHERE shop.pid = '$id'";


 //Menghapus Nilai pada Database
 if(mysqli_query($connection,$sql))
 {
 echo 'Product has been remove from the shop';
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
 ?>