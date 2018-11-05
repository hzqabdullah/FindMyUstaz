<?php
session_start();

include "../connection.php";

$societypage = $_GET['societypage'];

if($societypage == "" || $societypage =="1")
{
	$societypage1 = 0;
}
else
{
	$societypage1 = ($societypage * 7)-7;
}


?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>Admin || Display Society</title>
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
     <link href="../../css/style.css" rel="stylesheet">
    
  </head>

  <body>

    <nav class="navbar navbar-default">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand">FMU Management System</a>
        </div>
          <ul class="nav navbar-nav navbar-right">
            <li><a>Welcome, <?php echo $_SESSION['username']; ?></a></li>
            <li><a href="../logout.php">Logout</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <header id="header">
     <div class="container">
     <div class="row">
     <div class="col-md-10">
     <h2><a class="href" href="../dashboard.php"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>   Admin Control Panel</a></h2>
     </div>
     <div class="col-md-2">
     <div class="dropdown create">
          <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" 
          aria-haspopup="true" aria-expanded="true">
            Options
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
           	<li><a href="../viewadminprofile.php?username=<?php echo $_SESSION['username']; ?>">View Profile</a></li>
            <li><a href="../register.php">Register Admin</a></li>
            <li><a href="../adminlist.php">View Admin</a></li>
            <li><a href="../logout.php">Logout</a></li>
          </ul>
        </div>
     </div>
    
     </div>
    </header>
  
    <?php
  
  	$sql1 = "SELECT count(*) FROM ustaz";
	$result1 = mysqli_query($connection, $sql1);
	$row1 = mysqli_fetch_array($result1);
	$num1 = $row1[0];
	
	$sql2 = "SELECT count(*) FROM society";
	$result2 = mysqli_query($connection, $sql2);
	$row2 = mysqli_fetch_array($result2);
	$num2 = $row2[0];
	
	$sql3 = "SELECT count(*) FROM tmpustaz WHERE ustatus = 0";
	$result3 = mysqli_query($connection, $sql3);
	$row3 = mysqli_fetch_array($result3);
	$num3 = $row3[0];

  	$sql4 = "SELECT count(*) FROM orderdetails WHERE paystatus = 1 AND orderstatus = 0";
	$result4 = mysqli_query($connection, $sql4);
	$row4 = mysqli_fetch_array($result4);
	$num4 = $row4[0];
	
	
	
  ?>
  
  <section id="main">
      <div class="container">
          <div class="row">
          	<div class="col-md-3">
            <div class="list-group">
              <a href="../dashboard.php" class="list-group-item active main-color-bg">
                Dashboard
              </a>
              <a href="../ustaz/displayustaz.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   Ustaz<span class="badge"><?php echo $num1; ?></a>
              <a href="displaysociety.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   User<span class="badge">  <?php echo $num2; ?></a>
              <a href="../waitingustaz/displaywaitingustaz.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   Waiting Ustaz<span class="badge">  <?php echo $num3; ?></a>
              <a href="../order/displayorder.php" class="list-group-item"><span class="glyphicon glyphicon-tags" aria-hidden="true"></span>   Customer Order<span class="badge">  <?php echo $num4; ?></a>
              <a href="../transaction/displaytransaction.php" class="list-group-item"><span class="glyphicon glyphicon-usd" aria-hidden="true"></span>   Transaction History<span class="badge"></a>
            </div>
            
            <div class="well">
            <h4>Bandwidth Used</h4>
            <div class="progress">
              <div class="progress-bar" role="progressbar" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100" 
              style="width: 60%;">69%
			</div>
            </div>
            <h4>Disk Space Used</h4>
            <div class="progress">
              <div class="progress-bar" role="progressbar" aria-valuenow="69" aria-valuemin="0" aria-valuemax="100" 
              style="width: 40%;">12%
			</div>
            </div>
            </div>
            </div>
            
            <!--Website Overview-->
            
            <div class="col-md-9">
            	<div class="panel panel-default">
                  <div class="panel-heading main-color-bg">
                    <h3 class="panel-title">List of Users</h3>
                    <h3 class="panel-title" align="right" ><b>Page : <?php 
				  
				  		$a = $num2 / 7;
						$a = ceil($a);
				  
					  for($b = 1; $b <= $a; $b++)
						{
							?><a href="displaysociety.php?societypage=<?php echo $b; ?>" class="panel-title"><?php echo $b."  "; ?></a> <?php
						}
					  
					  
					  ?></b></h3>
                  </div>
                  
                  
                  
                  <table class="table table-striped table-hover" align="center" datapagesize="10" id="societyTable">
                  <tr>
                  	<th width="150" align="center"><div align="center">Profile photo</div></th>
                  	<th width="150">Profile name</th>
                    <th width="190">Location</th>
                    <th width="150">Contact</th>
                    <th width="150">Email</th>
                    <th width="50">Terminate</th>
                  </tr>
                  
                   <?php 
  
					  $sql = "SELECT * FROM society LIMIT $societypage1, 7";
						$display = mysqli_query($connection, $sql);
					  while ($data = mysqli_fetch_array($display))
					  {
					  ?>
                  
                  <tr>
                  <td align="center"><?php echo '<img src="'.$data["sphoto"].'" width="70px" height="80px"/>'; ?></td>
                  <td><?php echo $data ['sprofilename']; ?></td>
                  <td><?php echo $data ['slocation']; ?></td>
                  <td><?php echo $data ['scontact']; ?></td>
                  <td><?php echo $data ['semail']; ?></td>
                  <td align="center"><a onClick="return confirm('Are you sure you want terminate User?')" href="deletesociety.php?sid=<?php echo $data ['sid'];?>"><img src="../images/user3.png" width="30px" height="30px" /></a></td>
                </tr>
                 <?php } ?>
              </table>
                </div>    
             
            </div>
          </div>
      </div>
  </section>
  
  <!--Footer-->
  <footer id="footer">
  <p>Copyright <italic>FindMyUstaz</italic>, &copy; 2017</p>
  </footer>
  

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="../../js/bootstrap.min.js"></script>
  </body>
</html>
