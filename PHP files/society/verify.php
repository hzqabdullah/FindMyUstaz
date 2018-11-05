<?php 

require "register.php";
require "../admin/connection.php";

if(isset($_GET['email']) && !empty($_GET['email']) AND isset($_GET['hash']) && !empty($_GET['hash']))
{
   $email = mysql_escape_string($_GET['email']); // Set email variable
   $hash = mysql_escape_string($_GET['hash']); // Set hash variable
   
   $search = "SELECT semail, shash, status FROM society WHERE semail='".$email."' AND shash='".$hash."' AND status='0'";
   $check = mysqli_num_rows(mysqli_query($connection, $search));
   
   if($check > 0){
        // We have a match, activate the account
        mysqli_query("UPDATE society SET status='1' WHERE semail='".$email."' AND shash='".$hash."' AND status='0'") or die(mysql_error());
        echo '<div class="statusmsg">Your account has been activated, you can now login</div>';
    }
	else
	{
        // No match -> invalid url or account has already been activated.
        echo '<div class="statusmsg">The url is either invalid or you already have activated your account.</div>';
    }
       
}
else
{
    echo '<div class="statusmsg">Invalid approach, please use the link that has been send to your email.</div>';
}

?>