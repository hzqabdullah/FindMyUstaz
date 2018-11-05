<?php

session_start(); 
include('connection.php');

$error = "";

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="" href="css/style.css" />
</head>

<body>
<div class="header">
<h2>Login</h2>
</div>
<form method="post" action="adminlogin.php">
<div id="error-message"> <?php echo $error; ?></div>
  <div class="input-group">
    <label>Username</label>
    <input type="text" name="username" id="textfield">
  </div>
  <div class="input-group">
  	<label>Email</label>
    <input type="text" name="email" id="textfield2" />
  </div>
  <div class="input-group">
  	<label>Password</label>
    <input type="password" name="password" id="textfield3" />
  </div>
  <div class="input-group">
  <button type="submit" name="login_btn" class="btn">Login</button>
  </div>
</form>
</body>
</html>


<?php 

if(isset($_POST['login_btn']))
{
	if(isset($_POST['username']) and isset($_POST['email']) and isset($_POST['password']) and !empty($_POST['username']) and !empty($_POST['email']) and !empty($_POST['password']))
	{
		$username = ($_POST['username']);
		$email = ($_POST['email']);
		$password = ($_POST['password']);
		
		$query = "SELECT * FROM admin WHERE email = '$email' LIMIT 1";
		$result = mysqli_query($connection, $query);
		$row = mysqli_fetch_array($result);
		
		if(mysqli_num_rows($result) > 0 )
		{
			$newpassword = $row['password'];
			$password = base64_encode($password);
			
			if($username == $row['username'] and $email == $row['email'] and $password == $newpassword)
			{
				$_SESSION['username'] = $username;
				echo ("<SCRIPT LANGUAGE='JavaScript'>
				window.alert('You have been successfully logged in. Welcome ".$_SESSION['username']."')
				window.location.href='dashboard.php';
				</SCRIPT>");
				exit();
				
			}
			else
			{
				$error = "Invalid username, email or password";
			}
		}
		else
		{
			$error = "Invalid username, email or password";
		}
	}
}
?>
