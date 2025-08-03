<?php

include("connection.php");

$email = $_POST['email'];

$sql ="INSERT INTO address_tbl (usermail,email) VALUES ('shahala@studentsinnovations.com','$email')";

if(mysqli_query($con,$sql)){
	
	echo"success";
}
else{
	
	echo"failed";
}

?>