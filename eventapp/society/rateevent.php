<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $aid = $_POST['aid'];
 $eid = $_POST['eid'];
 $id = $_POST['id'];
 $rate = $_POST['rate'];
 $comment = $_POST['comment'];
 
 
	 $sql = "UPDATE attend SET astatus = 1 WHERE aid ='$aid'";
				
	 $result = mysqli_query($connection, $sql);
			
	 if($result)
	 {
		 $finalrate = $rate / 5;
		
		$sql1 = "INSERT INTO rate (aid, eid, sid, rate, comment) VALUES ('$aid','$eid','$id','$finalrate','$comment')";		
	 	$result1 = mysqli_query($connection, $sql1);
		
		if($result1){
			echo "Thank you for rating the event. The event will be remove from your profile";
			}

	 }
	 else
	 {
		echo "Please put your rate" . $connection->error;
	 }
   }

 
 mysqli_close($connection);

?>
