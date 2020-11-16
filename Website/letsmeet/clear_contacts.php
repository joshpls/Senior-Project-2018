<?php
require "../../hidden_uploads/josh_connect.php";
$user_id = $_POST["user_id"];

$mysql_qry = "DELETE FROM contact_list WHERE userID = '$user_id';";
if($conn->query($mysql_qry) === TRUE){
	echo "Contacts Deleted";
}
else{
	echo "Error:" . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
