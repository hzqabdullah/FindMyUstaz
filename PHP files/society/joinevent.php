<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $id = $_POST['id'];
 $eid = $_POST['eid'];
 
 
	 $sql = "INSERT INTO attend (sid, eid) VALUES ('$id','$eid')";
				
	 $result = mysqli_query($connection, $sql);
			
	 if($result)
	 {
		//$aid = mysqli_insert_id($connection);
		//$sql1 = "INSERT INTO RATE (aid, eid, sid) VALUES ('$aid','$eid','$id')";
				
	 	//$result1 = mysqli_query($connection, $sql1);
		
			echo "Thank you for joining the event. Please check your profile";
		
	 }
	 else
	 {
		echo "Error" .$query. "<br>" . $connection->error;
	 }
   }

 
 mysqli_close($connection);

?>
