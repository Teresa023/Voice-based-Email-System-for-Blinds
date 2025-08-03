<?php

include("connection.php");

$name = $_POST['name'];
$number = $_POST['number'];
$email = $_POST['email'];
$username = $_POST['username'];
$password = $_POST['password'];

$sql ="INSERT INTO register_tbl (name,email,number,username,password) VALUES ('$name','shahala@studentsinnovations.com','$number','$username','$password')";

if(mysqli_query($con,$sql)){
	
	echo"success";
}
else{
	
	echo"failed";
}


?>