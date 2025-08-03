<?php

include("connection.php");

$email = $_POST['email'];

//$email = "shahala@studentsinnovations.com";

$sql = "SELECT * FROM address_tbl WHERE usermail='$email'";
$res = mysqli_query($con,$sql);

if(mysqli_num_rows($res) > 0){

	while ($row = mysqli_fetch_array($res)) {
		
		$result[] = $row;
	}

	echo json_encode($result);
}else {

	echo "failed";
}

?>