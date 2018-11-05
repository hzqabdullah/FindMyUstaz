<?php
require_once('../admin/connection.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$target_dir = "photo/";
        $path = $target_dir."".rand()."_".time().".jpeg";
        $actualpath = "http://www.findmyustaz.com/society/$path";
		
	
		$id = $_POST['id'];
		$profilename = $_POST['profilename'];
        $details= $_POST['details'];
        $location = $_POST['location'];
		$latitude = $_POST['latitude'];
		$longitude = $_POST['longitude'];
        $contact = $_POST['contact'];
		$photo = $_POST['photo'];
		$password = base64_encode($_POST['password']);
      
		$sql = "SELECT sphoto FROM society WHERE sid = '$id'";
		
		$r = mysqli_query($connection, $sql);
		$row = mysqli_fetch_array($r);
		$currentphoto = $row['sphoto'];
		
		if(!empty($photo))
		{
	
			$sql1 = "UPDATE society SET sprofilename = '$profilename' , sdetails = '$details' , slocation = '$location' , slatitude = '$latitude', slongitude = '$longitude', scontact = '$contact', sphoto = '$actualpath', spassword = '$password' WHERE sid = $id;";
	
			if(mysqli_query($connection,$sql1))
			{
				file_put_contents($path, base64_decode($photo));
				echo 'User profile information has been updated ';
			}
			else
			{
				echo 'Please try again';
			}
		}
		
		if(empty($photo))
		{
	
			$sql2 = "UPDATE society SET sprofilename = '$profilename' , sdetails = '$details' , slocation = '$location' , slatitude = '$latitude', slongitude = '$longitude', scontact = '$contact', sphoto = '$currentphoto', spassword = '$password' WHERE sid = $id;";
	
			if(mysqli_query($connection,$sql2))
			{
				echo 'User profile information has been updated ';
			}
			else
			{
				echo 'Please try again';
			}
		}

		mysqli_close($connection);
	}
?>
