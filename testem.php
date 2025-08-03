<!-- 
<html> 

<head> 
	<link rel="stylesheet" href= 
"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> 
	<link rel="stylesheet" href="style.css"> 

	
</head> 

<body>  -->
	<!-- <h2>List Emails from Gmail using PHP and IMAP</h2>  -->
<!--https://myaccount.google.com/u/3/apppasswords?rapt=AEjHL4MBDXSjZPR_uCh1TIGNqI5KvstAuMcB8i0d8FhUDCHQQ_-jOR1Uk9O81CK5Xkf-WOZcpwFbp6qMCdnYLyEAWykegUz3NMfiX-bl27w0tQRnriunIys-->
		<?php

		error_reporting(0);

		include("connection.php");
		set_time_limit(4000); 


$hostname = '{imap.gmail.com:993/imap/ssl}INBOX'; // Correct format
$username = 'voicemailllll12345678@gmail.com'; // Use your Gmail
$password = 'qmoftjiijoeuhddk'; // Use App Password

$inbox = imap_open($hostname, $username, $password) 
    or die('Unable to connect to Gmail: ' . imap_last_error());

$emails = imap_search($inbox, 'ALL');

if ($emails) {
    // Sort emails by newest first
    rsort($emails);

    foreach ($emails as $email_number) {
        // Fetch email overview
        $overview = imap_fetch_overview($inbox, $email_number, 0);
        $message = imap_fetchbody($inbox, $email_number, 1); // Get email body

        // echo "<h3>Subject: " . htmlspecialchars($overview[0]->subject) . "</h3>";
        // echo "<p>From: " . htmlspecialchars($overview[0]->from) . "</p>";
        // echo "<p>Date: " . htmlspecialchars($overview[0]->date) . "</p>";
        // echo "<pre>" . htmlspecialchars($message) . "</pre>";
        // echo "<hr>";

         $subject = htmlspecialchars($overview[0]->subject);
         $sender =  htmlspecialchars($overview[0]->from);

        // echo $subject ." ".$sender." ".$message ;

         $sql = "INSERT INTO inbox_tbl(content,sender,recipient,subject) VALUES ('$message','$sender','$username','$subject')";

         mysqli_query($con,$sql);



    }

    $res = mysqli_query($con,"select * from inbox_tbl where recipient = '$username' ORDER BY id DESC limit 0,5");
	
	if(mysqli_num_rows($res)>0){
		while($row = mysqli_fetch_array($res))
		{
			$result[] = $row;
		}
		
	echo json_encode($result);
	}
	else{
	echo "failed";
	}
} else {
    echo "No emails found.";
}


			?> 
 
<!-- </body> 

</html> -->
