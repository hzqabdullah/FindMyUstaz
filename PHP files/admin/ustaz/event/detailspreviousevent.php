<?php
session_start();

include "../../connection.php";

$eid = $_GET['eid'];
$uid = $GET_['uid'];
  
$sql = "SELECT * FROM event WHERE eid = $eid";
$display = mysqli_query($connection, $sql);
$data = mysqli_fetch_array($display);

$commentpage = $_GET['commentpage'];

if($commentpage == "" || $commentpage =="1")
{
	$commentpage1 = 0;
}
else
{
	$commentpage1 = ($commentpage * 3)-3;
}

					 

?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>Admin || Display Lecture</title>
    <link href="../../../css/bootstrap.min.css" rel="stylesheet">
     <link href="../../../css/style.css" rel="stylesheet">
    
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
            <li><a href="../../logout.php">Logout</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <header id="header">
     <div class="container">
     <div class="row">
     <div class="col-md-10">
     <h2><a class="href" href="../../dashboard.php"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>   Admin Control Panel</a></h2>
     </div>
     <div class="col-md-2">
     <div class="dropdown create">
          <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" 
          aria-haspopup="true" aria-expanded="true">
            Options
            <span class="caret"></span>
          </button>
          <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
            <li><a href="../../viewadminprofile.php?username=<?php echo $_SESSION['username']; ?>">View Profile</a></li>
            <li><a href="../../register.php">Register Admin</a></li>
            <li><a href="../../adminlist.php">View Admin</a></li>
            <li><a href="../../logout.php">Logout</a></li>
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
	
	$sql5 = "SELECT count(*) FROM rate WHERE eid = '$eid'";
	$result5 = mysqli_query($connection, $sql5);
	$row5 = mysqli_fetch_array($result5);
	$num5 = $row5[0];
  
  ?>
  
  <section id="main">
      <div class="container">
          <div class="row">
          	<div class="col-md-3">
            <div class="list-group">
              <a href="../../dashboard.php" class="list-group-item active main-color-bg">
                Dashboard
              </a>
              <a href="../displayustaz.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   Ustaz<span class="badge"><?php echo $num1; ?></a>
              <a href="../../society/displaysociety.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   User<span class="badge">  <?php echo $num2; ?></a>
              <a href="../../waitingustaz/displaywaitingustaz.php" class="list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>   Waiting Ustaz<span class="badge">  <?php echo $num3; ?></a>
              <a href="../../order/displayorder.php" class="list-group-item"><span class="glyphicon glyphicon-tags" aria-hidden="true"></span>   Customer Order<span class="badge">  <?php echo $num4; ?></a>
              <a href="../../transaction/displaytransaction.php" class="list-group-item"><span class="glyphicon glyphicon-usd" aria-hidden="true"></span>   Transaction History<span class="badge"></a>
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
                    <h3 class="panel-title"> Event Details</h3>
                  </div>
                  <table border="0" >
                    <tr>
                      <td width="170" rowspan="17" align="center"><p>&nbsp;</p></td>
                      <td height="20" colspan="3" align="center">&nbsp;</td>
                      <td width="110" rowspan="3">&nbsp;</td>
                      <td width="110" rowspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                      <td height="170" colspan="3" align="center"><?php echo '<img src="'.$data["eimage"].'" width="550px" height="200px"/>'; ?></td>
                    </tr>
                    <tr>
                      <td height="15" colspan="3" align="center">&nbsp;</td>
                    </tr>
                    <tr>
                      <td width="128" height="30"><p>Category</p></td>
                      <td width="8">:</td>
                      <td width="230"><?php echo $data ['ecategory']; ?></td>
                      <td colspan="2" rowspan="7" align="center">
                      </a><a onClick="return confirm('Are you sure you want to delete Ustaz past event?')" href="deletepreviousevent.php?uid=<?php echo $data ['uid']?>&eid=<?php echo $data ['eid'];?>"><img src="../../images/trash.png" alt="" width="35px" height="35px" />
                        <p><h5><b>Delete</b></h5></p>
                      </a></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Title</p></td>
                      <td>:</td>
                      <td><?php echo $data ['etitle']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Date</p></td>
                      <td>:</td>
                      <td><?php echo $data ['edate']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Day</p></td>
                      <td>:</td>
                      <td><?php echo $data ['eday']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Time</p></td>
                      <td>:</td>
                      <td><?php echo $data ['etime']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Venue</p></td>
                      <td>:</td>
                      <td><?php echo $data ['evenue']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Address</p></td>
                      <td>:</td>
                      <td height="60"><?php echo $data ['eplace']; ?></td>
                    </tr>
                    <tr>
                      <td height="30"><p>Insert Date</p></td>
                      <td>:</td>
                      <td><?php echo $data ['einsert']; ?></td>
                      <td colspan="2" rowspan="7">&nbsp;</td>
                    </tr>
                    <tr>
                      <td height="30"><p>Last Update</p></td>
                      <td>:</td>
                      <td><?php echo $data ['eupdate']; ?></td>
                    </tr>
                    <tr>
                      <td height="30">No of  Society</td>
                      <td>&nbsp;</td>
                      <td><?php echo $num5; ?></td>
                    </tr> 
                  </table>
                  
                  <p>&nbsp;</p>
                  <table width="672" class="table-striped table-hover" >
                  <tr>
                  	<th width="105" align="center"><div align="center"></div></th>
                  	<th width="165" bgcolor="#09a73f"><div align="center">Profile Photo</div></th>
                    <th width="301" bgcolor="#09a73f"><div align="center">Comment</div></th>
                    <th width="81" bgcolor="#09a73f"><div align="center">Delete</div></th>
                  </tr>
                  
                   <?php 
  
					  $sql = "SELECT rate.rid, rate.comment, society.sprofilename, society.sphoto FROM rate JOIN society ON rate.sid = society.sid WHERE rate.eid = '$eid' LIMIT $commentpage1, 3";
						$display = mysqli_query($connection, $sql);
					  while ($data1 = mysqli_fetch_array($display))
					  {
					  ?>
                  
                  <tr>
                  <td height="57">&nbsp;</td>
                  <td><div align="center">
                    <div align="center"><?php echo '<img src="'.$data1["sphoto"].'" width="45px" height="60px"/>'; ?></div>
                  </div>
                    <p align="center"><?php echo $data1 ['sprofilename']; ?></p></td>
                  <td><div align="center"><?php echo $data1 ['comment']; ?></div></td>
                  <td><div align="center"></a><a onClick="return confirm('Are you sure you want to delete Society comment? This not will affect the event rate')" href="deletecomment.php?uid=<?php echo $data ['uid']; ?>&eid=<?php echo $eid; ?>&rid=<?php echo $data1 ['rid'];?>"><img src="../../images/trash.png" alt="" width="30px" height="30px" />
                    </a></div></td>
                </tr>
                <?php } ?>
                  <tr>
                    <td height="22">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><span class="panel"><b>Page :
                          <?php 
				  
				  		$a = $num5 / 3;
						$a = ceil($a);
				  
					  for($b = 1; $b <= $a; $b++)
						{
							?>
                          <a href="detailspreviousevent.php?uid=<?php echo $data['uid']; ?>&eid=<?php echo $eid; ?>&commentpage=<?php echo $b; ?>" class="panel-title"><?php echo $b."  "; ?></a>
                          <?php
						}
					  
					  
					  ?>
                    </b></span></td>
                    <td>&nbsp;</td>
                  </tr>
                 
              </table>
           
                  
            	</div>    
             
            </div>
          </div>
     </section>
  
  <!--Footer-->
  <footer id="footer">
  <p>Copyright <italic>FindMyUstaz</italic>, &copy; 2017</p>
  </footer>
  

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="../../../js/bootstrap.min.js"></script>
  </body>
</html>
