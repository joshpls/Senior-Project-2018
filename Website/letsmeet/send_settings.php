<?php
require "../../hidden_uploads/josh_connect.php";
$user_id = $_POST["user_id"];
$default_message = $_POST["default_message"];
$user_school = $_POST["user_school"];

$checkSettings = "select * from setting_list where userID like '$user_id';";
$insertSettings =  "INSERT INTO setting_list (userID, default_message, user_school) VALUES ('$user_id', '$default_message', '$user_school')";
$updateSettings = "UPDATE setting_list SET default_message = '$default_message', user_school = '$user_school' WHERE userID = '$user_id';";
$checkResult = mysqli_query($conn ,$checkSettings);
$rowcount = "";
if($rowcount=mysqli_num_rows($checkResult) > 0){
	if($conn->query($updateSettings) === TRUE){
		echo "Settings Updated";
	}
	else{
		echo "Error:" . $updateSettings . "<br>" . $conn->error;
	}

}
else
{
	if($conn->query($insertSettings) === TRUE){
		echo "Settings Updated";
	}
	else
	{
		echo "Error:" . $insertSettings . "<br>" . $conn->error;
	}
}

$conn->close();

?>