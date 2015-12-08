<?php

require("/home/isafeuser/ws.instrumentsafe.com/public/classes/User.php");

$username = trim($_REQUEST['u']);
$password = trim($_REQUEST['p']);
$deviceid = trim($_REQUEST['d']);// unimplemented


$user = new User($username,$password);

try {

		$user->Login();

}catch (Exception $e) {
   		
   		$errorMsg="Login exception: ".$e->getMessage();
		$errorStatus=1;
		$status=0;
		$resultArr = array("status"=>$status,"error_status"=>$errorStatus,"error_message"=>$errorMsg); 		                            
		$user->SendJson($resultArr);

}

