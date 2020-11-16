<?php
require "../../hidden_uploads/josh_connect.php";
$user_id = $_POST["user_id"];
$contact_name = $_POST["contact_name"];
$contact_number = $_POST["contact_number"];

$mysql_qry = "INSERT INTO contact_list (userID, contactName, contactNumber) VALUES ('$user_id', '$contact_name', '$contact_number')";
if(mysqli_query($conn ,$mysql_qry)){
	$last_id = mysqli_insert_id($conn);
	echo "ID=" . $last_id;
}
else{
	echo "Error:" . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
