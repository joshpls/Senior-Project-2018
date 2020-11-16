<?php
    // Set your timezone!
    date_default_timezone_set('America/New_York');

    if (isset($_GET['ym'])){
        $ym = $_GET['ym'];
    } else {
        $ym = date('Y-m');
    }

    // Check Format
    $timestamp = strtotime($ym,"-01");
    if($timestamp == false){
        $timestamp = time();
    }

    // Today
    $today = date('Y-m-d', time());

    $year = date('Y', $timestamp);
    $month = date('m', $timestamp);

    // For H3 title
    $html_title = date('m / Y', $timestamp);

    // Create prev & next month link   mktime(hour,minute,second,month,day,year)
    $prev = date('Y-m', mktime(0, 0, 0, date('m', $timestamp)-1, 1, date('Y', $timestamp)));
    $next = date('Y-m', mktime(0, 0, 0, date('m', $timestamp)+1, 1, date('Y', $timestamp)));

    // Number of days in the month
    $day_count = date('t', $timestamp);

    // 0:Sun 1:Mon 2:Tue...
    $str = date('w', mktime(0,0,0, date('m', $timestamp), 1, date('Y', $timestamp)));

    // Create Calendar!!
    $weeks = array();
    $week = '';

    // Add empty cell
    $week .= str_repeat('<td></td>', $str);

    for ($day=1; $day <= $day_count; $day++, $str++) {
        $date = $ym.'-'.$day;

        if($today == $date){
            $week .= '<td class="today">'.$day;
        } else {
            $week .= '<td id="calendarTD" onclick="fn(this.innerHTML)">'.$day;
        }
        $week .= '</td>';

        if ($str % 7 == 6 || $day == $day_count) {
            if($day == $day_count){
                // Add empty cell
                $week .= str_repeat('<td></td>', 6 - ($str % 7));
            }

            $weeks[] = '<tr class="calendarTR">' .$week.'</tr>';

            // Prepare for the new week
            $week = '';
        }
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Title -->
    <title>Let's Meet</title>

    <!-- Custom CSS -->
    <link href="jstyle.css" rel="stylesheet">

    <!-- Logo Font -->
    <link href='https://fonts.googleapis.com/css?family=Play:700' rel='stylesheet' type='text/css'>
    <!-- <link href='https://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>  -->
    <link href='https://fonts.googleapis.com/css?family=Nunito:400,300' rel='stylesheet' type='text/css'>
    <link rel="icon" type="image/png" sizes="32x32" href="icons/favicon-32x32.png">

    <link href="https://fonts.googleapis.com/css?family=Noto+Sans:400,700" rel="stylesheet">



    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

    <script>
        // remove fragment as much as it can go without adding an entry in browser history:
        window.location.replace("#");

        // slice off the remaining '#' in HTML5:
        if (typeof window.history.replaceState == 'function') {
            history.replaceState({}, '', window.location.href.slice(0, -1));
        }
    </script>

	<script type="text/javascript">

    var nmonth = "<?php echo $month; ?>";
    var nyear = "<?php echo $year; ?>";

    function fn(theDate){
  			//alert($dateSelected);
  			var myDateFormat = nyear + "-" + nmonth + "-" + theDate;
  			//month + "/" + theDate + "/" + year
  			$("#spanDate").text(myDateFormat);

        $(document).ready(function () {
          createCookie("yourDate", myDateFormat, "10");
        });
    }

    function createCookie(name, value, days) {
      var expires;
      if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toGMTString();
      } else {
        expires = "";
      }
      document.cookie = escape(name) + "=" + escape(value) + expires + "; path=/";
    }

	</script>

	<script type="text/javascript">
	//auto expand textarea
	function adjust_textarea(h) {
		h.style.height = "20px";
		h.style.height = (h.scrollHeight)+"px";
	}
	</script>

	<style>
	.calendar {
	  font-family: 'Noto Sans', sans-serif;
	}

	.calendarTable {
	  margin: 1em 0;
	  min-width: 300px;
	  background: #34495E;
    align-items:center;
	  color: #fff;
	  border-radius: .4em;
	  overflow: hidden;
	}

	.today {
		color: orange;
	}

	.calendarTR {
		border-top: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		border-color: #ffffff;
	}

	.calendarTH {
		height: 30px;
		text-align: center;
		font-weight: 700;
		border-top: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		border-color: #ffffff;
	}
	#calendarTD {
		height: 50px;
		text-align: center;
		border-top: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		border-left: 1px solid #ddd;
		border-right: 1px solid #ddd;
		border-color: #46637f;
		color: #ffffff;
	}

	#calendarTD:hover, #calendarTD:active {
		box-shadow: 2px 2px 2px 1px #000;
		background: orange;
	}

	.calendarTH:nth-of-type(7),.calendarTD:nth-of-type(7) {
		color:blue;
	}
	.calendarTH:nth-of-type(1),.calendarTD:nth-of-type(1) {
		color:red;
	}

  .spanCalendar {
    display:flex;
    justify-content:center;
    align-self: center;
  }

	#spanDate {
		color:orange;
	}
	</style>


</head>

<body>
    <!-- PHP -->
    <?php
        function test($arr)
        {
            echo "<pre>";
            print_r($arr);
            echo "</pre>";
        }
        require_once("../hidden_uploads/josh_connect_web.php");

        $db = new PDO("mysql:host=".$server.";dbname=".$database.";", $username, $password);

        echo "Connected to Database.";
    ?>
    <?php
        $user_id = $_GET['id'];
        $contact_id = $_GET['c'];
    ?>
    <!-- Page Content -->
    <div class="container">
        <div class="section group">
            <div class="col span_2_of_8">
            </div>
            <div class="col span_4_of_8">
                <h1 class="page-header"><b><span class="cimr">[ Let's Meet ]</span></b>
                    <small> :: connect with parents.</small>

                    <div class="nav navbar-nav align-right">
                        <div class="dropdown" id="myDropMenu">
                            <button onclick="myFunction()" class="dropbtn">
                                <span class="glyphicon glyphicon-chevron-down">
                                </span>
                            </button>
                                <div id="myDropdown" class="dropdown-content">
                                    <a href="index.html">Home</a>
                                </div>
                        </div>
                    </div>
                </h1>
            </div>
            <div class="col span_2_of_8">
            </div>
        </div>

        <div class="section group">
            <div class="col span_2_of_8">
            </div>
            <div class="col span_4_of_8">
                <span id="jscript">
                    <center>
                        Welcome to Lets Meet.
                        <br>
                        Fill out your meeting date and time below.
                        <br>
                        Click Confirm Meeting, to register with the teacher your appointment.
                    </center>
                </span>
            </div>
            <div class="col span_2_of_8">
            </div>
        </div>

		<div class="section group">
            <div class="col span_2_of_8">
            </div>
            <div class="col span_4_of_8"">
				<span class="spanCalendar">
					<div class="calendar">
						<h3> <a href="?id=<?php echo $user_id; ?>&c=<?php echo $contact_id; ?>&ym=<?php echo $prev; ?>">&lt;</a><?php echo $html_title; ?><a href="?id=<?php echo $user_id; ?>&c=<?php echo $contact_id; ?>&ym=<?php echo $next; ?>">&gt;</a></h3>
						<table class="calendarTable">
							<tr class="calendarTR">
								<th class="calendarTH">S</th>
								<th class="calendarTH">M</th>
								<th class="calendarTH">T</th>
								<th class="calendarTH">W</th>
								<th class="calendarTH">T</th>
								<th class="calendarTH">F</th>
								<th class="calendarTH">S</th>
							</tr>
							<?php
								foreach ($weeks as $week) {
									echo $week;
								}
							?>
						</table>
					</div>
				</span>
            </div>
            <div class="col span_2_of_8">
            </div>
        </div>

		  <div class="section group">
            <div class="col span_2_of_8">
            </div>
            <div class="col span_4_of_8"">
                <h3 class="page-header">Meeting Confirmation:</h3>
            </div>
            <div class="col span_2_of_8">
            </div>
        </div>

        <div class="section group">
            <div class="col span_3_of_8">
            </div>
            <div class="col span_2_of_8">
                <span>
					<span>
						<form class="form-style-4" action="" method="post">
						<label>
						<?php
							if($user_id != null | $contact_id != null){
								$IDQuery = $db->query("SELECT * FROM `contact_list` WHERE userID = '$user_id' and contactID = '$contact_id';");

								while($row = $IDQuery->fetch(PDO::FETCH_BOTH))
								{
									echo "Parent: <div style='color:#adb7bd;''>".$row['contactName']."</div><br>";
									echo "Parent Number: <div style='color:#adb7bd;''>".$row['contactNumber']."</div><br>";
								}

                echo "<div class='seperate'></div>";
                $IDQuery = $db->query("SELECT * FROM `user_list` WHERE userID = '$user_id';");

								while($row = $IDQuery->fetch(PDO::FETCH_BOTH))
								{
									echo "Teacher: <div style='color:orange;''>".$row['userFirstName']." ".$row['userLastName']."</div><br>";
									echo "Contact Number: <div style='color:orange;''>".$row['userNumber']."</div><br>";
                  echo "Contact Email: <div style='color:orange;''>".$row['userEmail']."</div><br>";

								}
							}
						?>
							<span>Date:
							<span id="spanDate"></span></span><br>
						<label>
						<br><br>
						<label for="note">
						<span>Note</span><br><textarea name="note" onkeyup="adjust_textarea(this)"></textarea>
						</label>
						<label>
						<span>&nbsp;</span><input type="submit" name="submit" value="Confirm" />
						</label>
						</form>

						<?php
            if(isset($_POST["submit"])){
                $mysqlDate = $_COOKIE["yourDate"];
                if($mysqlDate != null){
    							if($user_id != null | $contact_id != null){
    								$insert = $db->prepare("UPDATE contact_list SET contactDate = ? WHERE userID = ? AND contactID = ?");
    								$insert->bindParam(1, $mysqlDate);
    								$insert->bindParam(2, $user_id);
    								$insert->bindParam(3, $contact_id);
    								$insert->execute();
    							}
                }
            }
						?>
					</span>
                </span>
            </div>
            <div class="col span_3_of_8">
            </div>
        </div>


    <script>document.getElementById("year").innerHTML = new Date().getFullYear();</script>
</body>
</html>
