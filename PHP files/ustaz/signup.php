<?php

require "../admin/connection.php";

$firstname = $_POST["firstname"];
$lastname = $_POST["lastname"];
$profilename = $_POST["profilename"];
$icno = $_POST["icno"];
$dob = $_POST['dob'];
$place = $_POST['place'];
$contact = $_POST["contact"];
$email = $_POST["email"];
$password = $_POST["password"];
$password2 = $_POST["password2"];


if($profilename =='' or $email =='' or $password =='' or $password2 =='' or $icno ='')
	{
		echo "Please complete all the fields";
	}
	
	else if($password !== $password2)
	{
		echo "Password doesnt not match";
	}
	
	else
	{
		$getInfo = "SELECT * FROM ustaz WHERE uemail = '$email' or uicno = '$icno'";
		$res = mysqli_query($connection, $getInfo);
		$row = mysqli_fetch_assoc($res);
		
		
		if(mysqli_num_rows($res) > 0)
		{
			echo "Invalid IC No or Email";
		}
		else 
		{

			$sql = "INSERT INTO ustaz(ufirstname, ulastname, uprofilename, uicno, udob, uplace, ucontact, uemail, upassword) 
			VALUES ('$firstname','$lastname','$profilename','$icno','$dob','$place','$contact','$email','$password');";
			
			$result = mysqli_query($connection, $sql);
		
			if($result)
			{
				echo "Successfully registered";
			}
			else
			{
				echo "Error" .$query. "<br>" . $connection->error;
			}
		}	
	}	


$connection->close();

?>