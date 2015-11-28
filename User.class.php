<?php

class User {

    public $UserName;
    public $Password;
    private $_db;
    private $_status;
    private $_dbErrorMsg;
    private $_dbErrorStatus;


    function User($username,$password) {

        $this->UserName = str_replace("/","",trim($username));
        $this->Password = trim($password);

        if(!$this->ValidateUserNameAndPassword()){

            $resultArr = array("status"=>0,"error_status"=>1,"error_message"=>"invalid username or password");
            header('Content-type: application/json');
            echo json_encode($resultArr);
            exit();

        }

        if(!$this->Connect()){

            $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);
            header('Content-type: application/json');
            echo json_encode($resultArr);
            exit();
        }




    }

    public function Login(){

        $sql="SELECT COUNT(id) AS validUser,id FROM users WHERE username= '".mysqli_real_escape_string($this->_db,$this->UserName)."' AND password= '".mysqli_real_escape_string($this->_db,$this->Password)."'";

        if (!$result = $this->_db->query($sql)) {

            $this->_dbErrorMsg = "sql error: " . $this->_db->error;
            $this->_dbErrorStatus = 1;
        }



        $row = $result->fetch_assoc();


        if($row['validUser']==1){

            $sql2 = "UPDATE users SET session_expires_on=DATE_ADD(`session_expires_on`, INTERVAL 2 DAY) WHERE id=".mysqli_real_escape_string($this->_db,$row['id']);

            if ($this->_db->query($sql2) === TRUE) {

                $this->_dbErrorMsg= "";
                $this->_dbErrorStatus = 0;
                $this->_status = 1;

            } else {

                $this->_dbErrorMsg= "Error updating record: " . $this->_db->error;
                $this->_dbErrorStatus = 1;
                $this->_status = 0;

            }

        } else {

            $this->_status = 0;
            $this->_dbErrorStatus = 1;
            $this->_dbErrorMsg="user not found";



        }

        $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);

        header('Content-type: application/json');
        echo json_encode($resultArr);
        exit();

    }

    public function Delete(){

        $sql="SELECT COUNT(id) AS userCount,id FROM users WHERE username= '".mysqli_real_escape_string($this->_db,$this->UserName)."' AND password= '".mysqli_real_escape_string($this->_db,$this->Password)."'";

        if (!$result = $this->_db->query($sql)) {

            $this->_dbErrorMsg = "sql error: " . $this->_db->error;
            $this->_dbErrorStatus = 1;
        }

        $row = $result->fetch_assoc();


        if($row['userCount']==1){

            $sql2 = "DELETE FROM users WHERE id=".mysqli_real_escape_string($this->_db,$row['id']);

            if ($this->_db->query($sql2) === TRUE) {

                $this->_status = 1;
                $this->_dbErrorMsg="";
                $this->_dbErrorStatus = 0;


            } else {

                $this->_status=0;
                $this->_dbErrorMsg= "Error updating record: " . $this->_db->error;
                $this->_dbErrorStatus=1;

            }

            $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);
            header('Content-type: application/json');
            echo json_encode($resultArr);
            exit();
        }
        $this->_status = 0;
        $this->_dbErrorStatus = 1;
        $this->_dbErrorMsg="user not found";

        $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);
        header('Content-type: application/json');
        echo json_encode($resultArr);
        exit();
    }

    public function Add(){



        $sql="SELECT COUNT(id) AS userCount,id FROM users WHERE username= '".mysqli_real_escape_string($this->_db,$this->UserName)."'";

        if (!$result = $this->_db->query($sql)) {

            $this->_dbErrorMsg = "sql error: " . $this->_db->error;
            $this->_dbErrorStatus=1;
            $this->_status=0;

        }

        $row = $result->fetch_assoc();

        if($row['userCount'] > 0){

            $this->_dbErrorMsg="user already exists";
            $this->_dbErrorStatus=1;
            $this->_status=0;
            $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);

            header('Content-type: application/json');
            echo json_encode($resultArr);
            exit();

        }


        $insert = "INSERT INTO users(username,password,session_expires_on)VALUES('".mysqli_real_escape_string($this->_db,$this->UserName)."','".mysqli_real_escape_string($this->_db,$this->Password)."',NOW())";

        if (!$result = $this->_db->query($insert)) {

            $this->_dbErrorMsg = "sql insert: " . $this->_db->error;
            $this->_dbErrorStatus=1;
            $this->_dbErrorMsg="new user  insert failed";
            $this->_status=0;


        }

        $this->_status=1;

        $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);

        header('Content-type: application/json');
        echo json_encode($resultArr);
        exit();


    }

    public function UpdatePassword(){



        $sql="SELECT COUNT(id) AS userCount,id FROM users WHERE username= '".mysqli_real_escape_string($this->_db,$this->UserName)."'";


        if (!$result = $this->_db->query($sql)) {

            $this->_dbErrorMsg = "sql error: " . $this->_db->error;
            $this->_dbErrorStatus=1;
            $this->_status = 0;


        }



        $row = $result->fetch_assoc();

        if($row['userCount'] ==1){

            $sql2 = "UPDATE users SET password='".mysqli_real_escape_string($this->_db,$this->Password)."',session_expires_on=DATE_ADD(session_expires_on, INTERVAL 2 DAY) WHERE id=".mysqli_real_escape_string($this->_db,$row['id']);

            if ($this->_db->query($sql2) === TRUE) {

                $this->_status = 1;
                $this->_dbErrorStatus=0;

            } else {

                $this->_dbErrorMsg= "Error updating record: " . $this->_db->error;
                $this->_dbErrorStatus=1;
                $this->_status = 0;


            }

        } else {

            $this->_dbErrorStatus=1;
            $this->_dbErrorMsg="user not found";
            $this->_status = 0;
        }

        $resultArr = array("status"=>$this->_status,"error_status"=>$this->_dbErrorStatus,"error_message"=>$this->_dbErrorMsg);

        header('Content-type: application/json');
        echo json_encode($resultArr);
        exit();

    }

    private function Connect(){

        $this->_db = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

        if (mysqli_connect_errno()) {

            $this->_dbErrorMsg = "sql connect error: " . mysqli_connect_error();
            $this->_dbErrorStatus=1;
            $this->_status = 0;


            return false;
        }

        return true;
    }

    private function ValidateUserNameAndPassword(){

        return $this->UserName !="" && $this->Password !="";
    }
}
