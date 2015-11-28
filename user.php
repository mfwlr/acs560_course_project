<?php
require ("/home/isafeuser/ws.instrumentsafe.com/config.php");
require("/home/isafeuser/ws.instrumentsafe.com/public/classes/User.php");

$username = trim($_REQUEST['u']);
$password = trim($_REQUEST['p']);
$op = trim($_REQUEST['o']);

validateInputData($username,$password,$op);

$user = new User($username,$password);


if($op =="add"){
	
	$user->Add($username,$password);

}else if($op=="update"){
	
	$user->UpdatePassword($username,$password);

}else if($op=="delete"){
	
	$user->Delete($username,$password);

}else{
	
	$errorMsg = "Invalid operation";
	$errorResultArr = array("error"=>1,"error_message"=>$errorMsg); 		                            
	
	header('Content-type: application/json');
	echo json_encode($errorResultArr);
	exit();
}



function validateInputData($u,$p,$o){

	if($u =="" || $p =="" || $o=="" ){

	     $errorMsg = "Invalid username or password";
	     $errorResultArr = array("error"=>1,"error_message"=>$errorMsg); 		                            

		header('Content-type: application/json');
		echo json_encode($errorResultArr);
		exit();
	}
	
}


?>