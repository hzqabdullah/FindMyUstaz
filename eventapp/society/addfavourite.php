<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $id = $_POST['id'];
 $uid = $_POST['uid'];
 
 $sql = "SELECT * FROM favorite WHERE sid = '$id' and uid = '$uid'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
 if(isset($check))
 {
	 $query = "DELETE FROM favorite WHERE sid = '$id' and uid = '$uid'";
	 $result = mysqli_query($connection, $query);
	 
	 if($result)
	 {
		echo "Unfollowed!"; 
	 }
 }
 else
 {
	 $sql = "INSERT INTO favorite (sid, uid) VALUES ('$id','$uid')";
				
	 $result = mysqli_query($connection, $sql);
			
	 if($result)
	 {
		echo "Followed";
	 }
	 else
	 {
		echo "Error" .$query. "<br>" . $connection->error;
	 }
   }
}
 
 mysqli_close($connection);

?>
