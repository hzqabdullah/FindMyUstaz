<?php

 $uid = $_GET['uid'];

 //Import File Koneksi Database
 include "../connection.php";

 //Membuat SQL Query
 $sql = "DELETE FROM tmpustaz WHERE uid=$uid;";


 //Menghapus Nilai pada Database
 if(mysqli_query($connection,$sql))
 {
	 echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Successfully delete')
				</SCRIPT>");
	 header('location: displaywaitingustaz.php');
 }
 else
 {
 	echo 'Please try again';
 }

 mysqli_close($connection);
?>