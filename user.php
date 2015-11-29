<?php
require ("/home/isafeuser/ws.instrumentsafe.com/config.php");
require("/home/isafeuser/ws.instrumentsafe.com/public/classes/User.php");


$op = trim($_REQUEST['o']);
$username = trim($_REQUEST['u']);
$password = trim($_REQUEST['p']);
$county = trim($_REQUEST['c']);
$state_code = trim($_REQUEST['s']);
$disease_code = trim($_REQUEST['d']);

validateInputData($username,$password,$op);

$user = new User($username,$password);


if($op =="add"){
	
	$user->Add();

}else if($op=="add_bookmark"){
	
	$user->AddBookmark($county,$state_code,$disease_code);

}else if($op=="get_bookmarks"){
	
	$user->GetBookmarks();

}else if($op=="delete_bookmark"){
	
	$user->DeleteBookmark($county,$state_code,$disease_code);
}

else if($op=="update"){
	
	$user->UpdatePassword();

}else if($op=="delete"){
	
	$user->Delete();

}else{
	
	$errorMsg = "Invalid operation: ".$op;
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