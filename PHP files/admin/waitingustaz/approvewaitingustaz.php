<?php

 $uid = $_GET['uid'];
 
 $status = 1;

 
 include "../connection.php";

 $sql = "INSERT INTO ustaz SELECT uid, ufirstname, ulastname, uprofilename, uicno, udob, uplace, ucontact, uphoto, uemail, upassword FROM tmpustaz WHERE uid = '$uid'";
 $query = "UPDATE tmpustaz SET ustatus = '$status' WHERE uid='$uid'";
 
 if(mysqli_query($connection,$sql))
 {
	 if(mysqli_query($connection,$query))
	 
	 echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Ustaz account is approved. New ustaz is added into the list')
				window.location.href='displaywaitingustaz.php';
				</SCRIPT>");
 }
 else
 {
 	echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('Failed to approved');
				</SCRIPT>");
 }
 mysqli_close($connection);
?>