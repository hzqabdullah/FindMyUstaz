<?php
if($_SERVER['REQUEST_METHOD']=='POST')
	{

 $uid = $_POST['uid'];


 require_once('../../admin/connection.php');
 

	 $query = "UPDATE event SET estatus = 1 WHERE uid = '$uid' AND STR_TO_DATE(edateend, '%d-%m-%Y') < CURDATE() AND estatus = 0";

	 if(mysqli_query($connection,$query))
			{
			
				echo 'Expired event has been insert into the archive';
			}
			else
			{
				echo 'Please try again';
			}
	
			mysqli_close($connection);
	}

 ?>