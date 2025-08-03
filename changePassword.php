<?php 

include("connection.php");

$uid = $_POST['uid'];
$currentpassword = $_POST['currentPass'];
$newpassword = $_POST['newPass'];  


$sql = "SELECT * FROM register_tbl where password = '$currentpassword' and id='$uid'";

$result = mysqli_query($con,$sql);

if(mysqli_num_rows($result)>0){
	
	$update = "UPDATE register_tbl SET password = '$newpassword' WHERE id = '$uid'";
	mysqli_query($con,$update);
	echo "success";
}
else{
	echo "failed";
}
?>