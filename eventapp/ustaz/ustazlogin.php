<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $uemail = $_POST['email'];
 $upassword = base64_encode($_POST['password']);
 $uicno = $_POST['icno'];
 
 
 $sql = "SELECT * FROM ustaz WHERE uemail = '$uemail' and upassword = '$upassword' and uicno = '$uicno'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
	 if(isset($check))
	 {
		 echo "Login success";
	 }
	 else
	 {
		 echo "Invalid Email or Password. Please Try Again";
	 }
 }
 else
 {
	 echo "Check Again";
 }
 
 mysqli_close($connection);

?>
