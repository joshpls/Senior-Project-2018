<?php
require "../../hidden_uploads/josh_connect.php";
$user_id = $_POST["user_id"];

$mysql_qry = "select * from setting_list where userID like '$user_id';";
$mysql_qry2 = "select userFirstName, userLastName from user_list where userID like '$user_id';";
$result = mysqli_query($conn ,$mysql_qry);
$result2 = mysqli_query($conn, $mysql_qry2);
$data = "";
$rowcount = 0;
foreach ($result as $row) {
	$rowcount=mysqli_num_rows($result);
	$data = $row['default_message'] . ':' .$row['user_school'];
	if($rowcount > 0)
		foreach ($result2 as $row) {
			$data = $data . ':' . $row['userFirstName'] . ' ' . $row['userLastName'];
		}
}

if($rowcount > 0){
	echo json_encode($data);
}
else{
	echo "settings_not_setup";
}

$conn->close();

?>