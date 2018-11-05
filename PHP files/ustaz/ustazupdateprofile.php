<?php

require_once('../admin/connection.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$target_dir = "photo/";
		$path = $target_dir."".rand()."_".time().".jpeg";
		$actualpath = "http://fmu.000webhostapp.com/ustaz/$path";
	
		$id = $_POST['id'];
		$firstname = $_POST['firstname'];
		$lastname = $_POST['lastname'];
		$profilename = $_POST['profilename'];
        $dob= $_POST['dob'];
        $place = $_POST['place'];
        $contact = $_POST['contact'];
		$photo = $_POST['photo'];
		$password = base64_encode($_POST['password']);
		
		$sql = "SELECT uphoto FROM ustaz WHERE uid = '$id'";
		
		$r = mysqli_query($connection, $sql);
		$row = mysqli_fetch_array($r);
		$currentphoto = $row['uphoto'];
      
	  	if(!empty($photo))
		{
			$sql1 = "UPDATE ustaz SET ufirstname = '$firstname', ulastname = '$lastname', uprofilename = '$profilename' , udob = '$dob' , uplace = '$place' , ucontact = '$contact' , upassword = '$password' , uphoto = '$actualpath' WHERE uid = $id;";


			if(mysqli_query($connection,$sql1))
			{
				file_put_contents($path, base64_decode($photo));
				echo 'Ustaz profile information has been updated ';
			}
			else
			{
				echo 'Please try again';
			}
		}
		
		if(empty($photo))
		{
			$sql2 = "UPDATE ustaz SET ufirstname = '$firstname', ulastname = '$lastname', uprofilename = '$profilename' , udob = '$dob' , uplace = '$place' , ucontact = '$contact' , upassword = '$password' , uphoto = '$currentphoto' WHERE uid = $id;";


			if(mysqli_query($connection,$sql2))
			{
				echo 'Ustaz profile information has been updated ';
			}
			else
			{
				echo 'Please try again';
			}
		}

		mysqli_close($connection);
	}
?>
