<?php

	require_once('../../admin/connection.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{

		$target_dir = "banner/";
		$path = $target_dir."".rand()."_".time().".jpeg";
		$actualpath = "http://www.findmyustaz.com/ustaz/event/$path";
		
		
		$category = $_POST['category'];
		$title = $_POST['title'];
		$date = $_POST['date'];
		$dateend = $_POST['dateend'];
		$day = $_POST['day'];
		$time = $_POST['time'];
		$venue = $_POST['venue'];
		$place = $_POST['place'];
		$latitude = $_POST['latitude'];
		$longitude = $_POST['longitude'];
		$image = $_POST['image'];
		$insert = $_POST['insert'];
		$update = $_POST['update'];
		$uid = $_POST['uid'];
		
		
		$sql = "SELECT * FROM event WHERE uid = '$uid' AND estatus = 0 AND edate = '$date'";
		
		$check = mysqli_fetch_array(mysqli_query($connection, $sql));
		
		if(isset($check))
		{
			 echo "Existing event on that date. Please insert the new date.";
		}

		else
		{
		
			 if(!empty($image))
			 {
			
				$sql1 = "INSERT INTO event (ecategory, etitle, edate, edateend, eday, etime, evenue, eplace, elatitude, elongitude, eimage, einsert, eupdate, uid) VALUES('$category', '$title', '$date', '$dateend', '$day', '$time', '$venue' , '$place', '$latitude', '$longitude', '$actualpath', '$insert', '$update', '$uid')";
				
			
					if(mysqli_query($connection,$sql1))
					{
						
						file_put_contents($path, base64_decode($image));
						echo 'New event has been created';
					}
					else
					{
						echo 'Please try again';
					}
			
			 }
			 
			 if(empty($image))
			 {
				 $sql2 = "INSERT INTO event (ecategory, etitle, edate, edateend, eday, etime, evenue, eplace, elatitude, elongitude, einsert, eupdate, uid) 
				VALUES('$category', '$title', '$date', '$dateend', '$day', '$time', '$venue' , '$place', '$latitude', '$longitude', '$insert', '$update', '$uid')";
				
					if(mysqli_query($connection,$sql2))
					{
						
						echo 'New event has been created';
					}
					else
					{
						echo 'Please try again';
					}
			 }
		}
 
	}
	
	mysqli_close($connection);	
	
?>

