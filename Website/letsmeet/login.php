<?php
require "../../hidden_uploads/josh_connect.php";
$user_name = $_POST["user_name"];
$user_pass = $_POST["password"];
$seed = 'jbkind';
$passwordHash = sha1($user_pass.$seed);

$mysql_qry = "select userID from user_list where userName like '$user_name' and userPass like '$passwordHash';";
$result = mysqli_query($conn ,$mysql_qry);
if($row=mysqli_fetch_row($result)){
	echo $row[0];
}
else{
	echo "Login Unsuccessful";
}
$conn->close();
?>
