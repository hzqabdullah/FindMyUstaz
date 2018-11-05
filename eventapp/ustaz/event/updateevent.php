<?php

require_once('../../admin/connection.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$target_dir = "banner/";
		$path = $target_dir."".rand()."_".time().".jpeg";
		$actualpath = "http://fmu.000webhostapp.com/ustaz/event/$path";
		
		$id = $_POST['id'];
		$category = $_POST['category'];
		$title = $_POST['title'];
		$date = $_POST['date'];
		$dateend = $_POST['dateend'];
		$day = $_POST['day'];
		$time = $_POST['time'];
		$venue = $_POST['venue'];
		$place = $_POST['place'];
		$image = ($_POST['image']);
		$latitude = $_POST['latitude'];
		$longitude = $_POST['longitude'];
		$update = $_POST['update'];
		
		$sql = "SELECT eimage FROM event WHERE eid = '$id'";
		
		$r = mysqli_query($connection, $sql);
		$row = mysqli_fetch_array($r);
		$currentphoto = $row['eimage'];
		
		if(!empty($image))
		{
			$sql1 = "UPDATE event SET ecategory = '$category', etitle = '$title', edate = '$date', edateend = '$dateend', eday = '$day', etime = '$time' , evenue = '$venue', eplace = '$place', elatitude = '$latitude', elongitude = '$longitude', eimage = '$actualpath', eupdate = '$update' WHERE eid = $id;";

			if(mysqli_query($connection,$sql1))
			{
				
				file_put_contents($path, base64_decode($image));
				echo 'Event information has been newly updated';
				
			}
			else
			{
				echo 'Please try again';
			}
		}
		
		if(empty($image))
		{
			$sql2 = "UPDATE event SET ecategory = '$category', etitle = '$title', edate = '$date', edateend = '$dateend', eday = '$day', etime = '$time' , evenue = '$venue', eplace = '$place', elatitude = '$latitude', elongitude = '$longitude', eimage = '$currentphoto', eupdate = '$update' WHERE eid = $id;";

			if(mysqli_query($connection,$sql2))
			{
		
				echo 'Event information has been newly updated';
				
			}
			else
			{
				echo 'Please try again';
			}
		}

		mysqli_close($connection);
	}
?>
