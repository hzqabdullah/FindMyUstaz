<?php

require "../admin/connection.php";

$target_dir = "photo/";
$path = $target_dir."".rand()."_".time().".jpeg";
$actualpath = "http://www.findmyustaz.com/ustaz/$path";

$firstname = $_POST["firstname"];
$lastname = $_POST["lastname"];
$profilename = $_POST["profilename"];
$icno = $_POST["icno"];
$dob = $_POST['dob'];
$place = $_POST['place'];
$contact = $_POST["contact"];
$email = $_POST["email"];
$photo = $_POST['photo'];
$password = $_POST["password"];

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
   echo "Invalid email format"; 
}

else if((strlen($icno) != 12) or !is_numeric ($icno))
{
	echo "IC No must have 12 digits.";
}

else
{

	$getInfo = "SELECT * FROM ustaz WHERE uemail = '$email' OR uicno = '$icno'";
	$check = mysqli_fetch_array(mysqli_query($connection, $getInfo));
			
	if(isset($check))
	{
		 echo "IC No or Email are taken";
	}
	else
	{
		 $password1 = base64_encode($password);	
		 
		 if(!empty($photo))
		 {
			 $sql = "INSERT INTO tmpustaz(ufirstname, ulastname, uprofilename, uicno, udob, uplace, ucontact, uemail, upassword, uphoto) 
				VALUES ('$firstname','$lastname','$profilename','$icno','$dob','$place','$contact','$email','$password1','$actualpath')";
				
				$result = mysqli_query($connection, $sql);
			
				if($result)
				{
					file_put_contents($path, base64_decode($photo));
					echo "Successfully registered. Account approval required before login. Please check your email";
				}
				else
				{
					echo "Error" .$query. "<br>" . $connection->error;
				}
		 }

		if(empty($photo))
		{
			$sql1 = "INSERT INTO tmpustaz(ufirstname, ulastname, uprofilename, uicno, udob, uplace, ucontact, uemail, upassword) 
				VALUES ('$firstname','$lastname','$profilename','$icno','$dob','$place','$contact','$email','$password1')";
				
				$result1 = mysqli_query($connection, $sql1);
			
				if($result1)
				{
					echo "Successfully registered. Account approval required before login. Please check your email";
				}
				else
				{
					echo "Error" .$query. "<br>" . $connection->error;
				}
		}

	}
}
 

$connection->close();

?>