<?php

 $eid = $_GET['eid'];
 $rid = $_GET['rid'];
 $uid = $_GET['uid'];

 
 include "../../connection.php";


 $sql = "UPDATE rate SET comment = 'Inappropriate comment' WHERE rid = $rid";

 if(mysqli_query($connection,$sql))
 {
	 echo ("<SCRIPT LANGUAGE='JavaScript'>
		window.alert('Previous comment has been deleted.')
		window.location.href='detailspreviousevent.php?uid=".$uid."&eid=".$eid."';
		</SCRIPT>");
 }
 else
 {
 echo 'Please try again';
 }

 mysqli_close($connection);
?>