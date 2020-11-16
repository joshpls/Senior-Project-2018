<?php
require "../../hidden_uploads/josh_connect.php";
$user_name = $_POST["user_name"];
$user_pass = $_POST["password"];
$user_email = $_POST["user_email"];
$user_number = $_POST["user_number"];
$user_firstName = $_POST["user_firstName"];
$user_lastName = $_POST["user_lastName"];

$seed = 'jbkind';
$passwordHash = sha1($user_pass.$seed);

$mysql_qry = "INSERT INTO user_list (userName, userPass, userEmail, userNumber, userFirstName, userLastName) VALUES ('$user_name', '$passwordHash', '$user_email', '$user_number', '$user_firstName', '$user_lastName')";
if($conn->query($mysql_qry) === TRUE){
	echo "Register Success";
}
else{
	echo "Error:" . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
