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
?>

<table border="1" cellpadding="3" style="width:90%;">
<tr>
	<th>State</th>
	<th>County</th>
	<th>Cancer Type</th>
	<th>Annual Incidence Rate</th>
	<th>Trending</th>
</tr>

<?php

 while ($row = $result->fetch_array()) {
        echo "<tr><td>".$row["state_description"]."</td><td>".$row["county"]."</td><td>".$row["type_description"]."</td><td>".$row["annual_incidence_rate"]."</td><td>".$row["recent_trend"]."</td></tr>";
    }

$mysqli->close();
?>

</table>




