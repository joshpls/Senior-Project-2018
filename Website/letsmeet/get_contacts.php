<?php
require "../../hidden_uploads/josh_connect.php";
$user_id = $_POST["user_id"];

$mysql_qry = "select * from contact_list where userID like '$user_id' and contactDate is not null order by contactName asc;";
$result = mysqli_query($conn ,$mysql_qry);
$data = array();
foreach ($result as $row) {
	$data[] = $row['contactName'] . ': ' .$row['contactDate'];
}

echo json_encode($data);

$conn->close();

?>
