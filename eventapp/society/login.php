<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

 include '../admin/connection.php';
 

 $email = $_POST['email'];
 $password = base64_encode($_POST['password']);
 
 $sql = "select * from society where semail = '$email' and spassword = '$password'";
 
 $check = mysqli_fetch_array(mysqli_query($connection, $sql));
 
 if(isset($check))
 {
	 echo "You have been successfully logged in";
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
