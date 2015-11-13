<?php


// db = mdb.connect(host="mysql.dev.tomcastonzo.com", user="cancer_db_user", passwd="&E@^5bXJ", db="cancer_db")

$mysqli = new mysqli("mysql.dev.tomcastonzo.com", "cancer_db_user", "&E@^5bXJ", "cancer_db");

if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

/* change db to world db */
$mysqli->select_db("cancer_db");


$s = $_REQUEST['s'];
$c = $_REQUEST['c'];
$t = $_REQUEST['t'];

$resultsArray;

$sql = "SELECT sm.state_description,
               cs.county, 
               ctm.type_description,
               annual_incidence_rate,
               cs.rate_period,
               cs.recent_trend  
               FROM cancer_stats cs  
               JOIN state_map sm ON sm.state_id = cs.state_code 
               JOIN cancer_type_map ctm ON ctm.type_code = cs.cancer_type_code 
               where annual_incidence_rate IS NOT NULL 
               AND cs.state_code=".$s;
               
               
               if($c !=""){
               	
               	$sql .= " AND cs.county='".$c."'";
               }
               
               $sql .=" AND cs.cancer_type_code=".$t." ORDER BY  annual_incidence_rate DESC";  

if (!$result = $mysqli->query($sql)) {
    die ('There was an error running query[' . $connection->error . ']');
}


 while ($row = $result->fetch_array()) {
 
 		$resultsArray[$ctr] = array("state"=>$row["state_description"],"county"=>$row["county"],"cancer_type"=>$row["type_description"],"rate"=>$row["annual_incidence_rate"],"trend"=>$row["recent_trend"]);
        

       
 }

$mysqli->close();

header('Content-type: application/json');
echo json_encode($resultsArray);
?>



