<?php

include("connection.php");

$uid = $_POST['uid'];
$name = $_POST['name'];
$number = $_POST['number'];
$email = $_POST['email'];
$username = $_POST['username'];


$sql = "UPDATE register_tbl SET name='$name',email='$email',number='$number',username='$username' WHERE id='$uid'";

//echo $sql;
        
 if(mysqli_query($con,$sql)){

    echo "Successfully Updated";

}else{

	echo"Failed to Update Profile";

}

?>