<?php

$msg = 'Your account has been made, <br /> please verify it by clicking the activation link that has been send to your email.';
$hash = md5( rand(0,1000) );

require "../admin/connection.php";

$target_dir = "photo/";
$path = $target_dir."".rand()."_".time().".jpeg";
$actualpath = "http://www.findmyustaz.com.com/society/$path";

$profilename = $_POST["profilename"];
$details = $_POST["details"];
$location = $_POST["location"];
$latitude = $_POST["latitude"];
$longitude = $_POST["longitude"];
$contact = $_POST["contact"];
$email = $_POST["email"];
$password = $_POST["password"];
$photo = $_POST["photo"];


if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
   echo "Invalid email format"; 
}

else
{

	$getInfo = "SELECT * FROM society WHERE semail = '$email'";
	$check = mysqli_fetch_array(mysqli_query($connection, $getInfo));
			
	if(isset($check))
	{
		 echo "Email address already taken";
	}
	else
	{
		$password1 = base64_encode($password);
		
		if(!empty($photo))
		 {
			 $sql = "INSERT INTO society(sprofilename, sdetails, slocation, slatitude, slongitude, scontact, sphoto, semail, spassword, shash) VALUES ('$profilename','$details','$location','$latitude','$longitude','$contact', '$actualpath', '$email','$password1', '$hash')";
					
					$result = mysqli_query($connection, $sql);
				
					if($result)
					{
						file_put_contents($path, base64_decode($photo));
						echo "You have been successfully registered. Please check your email for verification";
						
						$to      = $email; // Send email to our user
						$subject = 'Signup | Verification'; // Give the email a subject 
						$message = '
						 
						Thanks for signing up!
						Your account has been created, you can login with the following credentials after you have activated your account by pressing the url below.
						 
						------------------------
						Profile name: '.$profilename.'
						Password: '.$password.'
						------------------------
						 
						Please click this link to activate your account: 
						http://www.findmyustaz.com/society/verify.php?email='.$email.'&hash='.$hash.'
						 
						'; // Our message above including the link
											 
						$headers = 'From:noreply@findmyustaz.com' . "\r\n"; // Set from headers
						mail($to, $subject, $message, $headers); 
						
					}
					else
					{
						echo "Error" .$query. "<br>" . $connection->error;
					}
		}
		
		if(empty($photo))
		{
			$sql1 = "INSERT INTO society(sprofilename, sdetails, slocation, slatitude, slongitude, scontact, semail, spassword, shash) VALUES ('$profilename','$details','$location','$latitude','$longitude','$contact', '$email','$password1', '$hash')";
					
					$result1 = mysqli_query($connection, $sql1);
				
					if($result1)
					{
						echo "You have been successfully registered. Please check your email for verification";
						
						$to      = $email; // Send email to our user
						$subject = 'Signup | Verification'; // Give the email a subject 
						$message = '
						 
						Thanks for signing up!
						Your account has been created, you can login with the following credentials after you have activated your account by pressing the url below.
						 
						------------------------
						Profile name: '.$profilename.'
						Password: '.$password.'
						------------------------
						 
						Please click this link to activate your account:
						http://www.findmyustaz.com/society/verify.php?email='.$email.'&hash='.$hash.'
						 
						'; // Our message above including the link
											 
						$headers = 'From:noreply@findmyustaz.com' . "\r\n"; // Set from headers
						mail($to, $subject, $message, $headers); 
						
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