<?php

require ("/home/isafeuser/ws.instrumentsafe.com/config.php");


$s = trim($_REQUEST['s']);
$c = trim($_REQUEST['c']);
$t = trim($_REQUEST['t']);



if(!isValidWholeNumber($s) || !isValidWholeNumber($t) || !isValidAlphaString($c)){

	     $errorMsg = "Invalid ".(!isValidWholeNumber($s) ? " state code" : "");
	     $errorMsg .=(!isValidWholeNumber($t) ? " type code" : "");
	     $errorMsg .=(!isValidAlphaString($c) ? " county string" : "");
	    
		$errorResultArr = array("error"=>1,
				  "error_message"=>$errorMsg); 		                            

	header('Content-type: application/json');
	echo json_encode($errorResultArr);
	exit();
}



getIncidenceRankingsByCountyWithinState($s,$c,$t);




function getIncidenceRankingsByCountyWithinState($state,$county,$type){

	$cancerDescription = "";
	$userState="";
	$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);


	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}




	$sql = "SELECT
			cs.state_code,
			cs.county,
			(SELECT state_description from state_map WHERE state_id=cs.state_code) as state_description,
			(SELECT state_code from state_map WHERE state_id=cs.state_code) as state_abbrev,
			cs.cancer_type_code,
			(SELECT type_description from cancer_type_map WHERE type_code=cs.cancer_type_code) as cancer_description,
			cs.annual_incidence_rate,
			cs.rate_period,
			cs.recent_trend,
			@curRank := @curRank + 1 AS rank 
			FROM cancer_stats AS cs,(SELECT @curRank := 0) r  
			WHERE annual_incidence_rate IS NOT NULL
			AND cs.cancer_type_code = '".mysqli_real_escape_string($mysqli,$type)."'
			AND cs.state_code = '".mysqli_real_escape_string($mysqli,$state)."'
			ORDER BY  cs.annual_incidence_rate DESC;";


	if (!$result = $mysqli->query($sql)) {

    	die ('There was an error running query[' . $connection->error . ']');
    
	}

	$ctr=0;
 	while ($row = $result->fetch_array()) {
 
 		
 		
 		if($cancerDescription ==""){
 			
 			$cancerDescription = $row["cancer_description"];
 			
 		}
 		
 		if($userState ==""){
 			
 			$userState = $row["state_description"];
 			
 		}
 		
 		
 		
 		$resultsArray[$ctr] = array("state"=>$row["state_description"],
 									"state_code"=>$row["state_code"],
 		                            "county"=>$row["county"],
 		                            "cancer_type"=>$row["cancer_type_code"],
 		                            "cancer_type_desc"=>$row["cancer_description"],
 		                            "rate"=>$row["annual_incidence_rate"],
 		                            "trend"=>($row["recent_trend"] !="" ? $row["recent_trend"] : "unknown"),
 		                            "ranking"=>$row["rank"],
 		                            "rank_status"=>$status);
 		                            
 		                            
 		if($resultsArray[$ctr]["state_code"] == $state && strtolower($resultsArray[$ctr]["county"]) == strtolower($county)){
				
				$currUserState = $resultsArray[$ctr]["state"];
				$currUserCounty = $resultsArray[$ctr]["county"];
				$currUserRank = $resultsArray[$ctr]["ranking"];
				$currUserRate = $resultsArray[$ctr]["rate"];
				
		}
			
		$ctr++;
    }

	$worstState = $resultsArray[0]["state"];
	$worstCounty = $resultsArray[0]["county"];
	$worstRank = $resultsArray[0]["ranking"];
	$worstIncidenceRate = $resultsArray[0]["rate"];



	$bestState = $resultsArray[$result->num_rows -1]["state"];
	$bestCounty = $resultsArray[$result->num_rows -1]["county"];
	$bestRank = ($resultsArray[$result->num_rows -1]["ranking"]);
	$bestIncidenceRate = $resultsArray[$result->num_rows -1]["rate"];



	$countryWideStatsArr = getIncidenceRankingWithinCountry($state,$county,$type);



	$statsArr = array("cancer_type"=>$cancerDescription,
				  	  "user_state"=>$userState,
				  	  "user_county"=>$currUserCounty,
				  	  "user_incidence_rate"=>$currUserRate,
				  	  "worst_incidence_rate_in_state"=>$worstIncidenceRate,
				      "best_incidence_rate_in_state"=>$bestIncidenceRate,
				      "user_rank"=>$currUserRank,
				      "worst_rank_in_state"=>$worstRank,
				  	  "best_rank_in_state"=>$bestRank,
				  	  "state_star_ranking"=>getRanking($bestRank,$currUserRank),
				      "worst_county_in_state"=>$worstCounty,
				      "best_county_in_state"=>$bestCounty);


	$countyStateStatsArray = array_merge($statsArr, $countryWideStatsArr);

	$mysqli->close();
		
	header('Content-type: application/json');
	echo json_encode($countyStateStatsArray);

}


function getIncidenceRankingWithinCountry($state,$county,$type){


	$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

	if (mysqli_connect_errno()) {
    	printf("Connect failed: %s\n", mysqli_connect_error());
    	exit();
	}


	$cancerDescription = "";
	
	$sql = "SELECT
		cs.state_code,
		cs.county,
		(SELECT state_description from state_map WHERE state_id=cs.state_code) as state_description,
		(SELECT state_code from state_map WHERE state_id=cs.state_code) as state_abbrev,
		cs.cancer_type_code,
		(SELECT type_description from cancer_type_map WHERE type_code=cs.cancer_type_code) as cancer_description,
		cs.annual_incidence_rate,
		cs.rate_period,
		cs.recent_trend,
		@curRank := @curRank + 1 AS rank 
		FROM cancer_stats AS cs,(SELECT @curRank := 0) r  
		WHERE annual_incidence_rate IS NOT NULL
		AND cs.cancer_type_code = '".mysqli_real_escape_string($mysqli,$type)."'
		ORDER BY  cs.annual_incidence_rate DESC;";



	if (!$result = $mysqli->query($sql)) {

    	die ('There was an error running query[' . $connection->error . ']');
    
	}

	$ctr=0;
 	while ($row = $result->fetch_array()) {
 
		if($cancerDescription ==""){
 			
 			$cancerDescription = $row["cancer_description"];
 			
 		}
 		
 		$resultsArray[$ctr] = array("country_state"=>$row["state_description"],
 									"country_state_code"=>$row["state_code"],
 		                            "country_county"=>$row["county"],
 		                            "cancer_type"=>$row["cancer_type_code"],
 		                            "country_cancer_type_desc"=>$row["cancer_description"],
 		                            "country_rate"=>$row["annual_incidence_rate"],
 		                            "country_trend"=>($row["recent_trend"] !="" ? $row["recent_trend"] : "unknown"),
 		                            "country_ranking"=>$row["rank"],
 		                            "country_rank_status"=>$status
 		                            
 		                            );
 		                            
 		                            
 		
 	                           
 		if($resultsArray[$ctr]["country_state_code"] == $state && strtolower($resultsArray[$ctr]["country_county"]) == strtolower($county)){
				
				$currUserState = $resultsArray[$ctr]["country_state"];
				$currUserCounty = $resultsArray[$ctr]["country_county"];
				$currUserRank = $resultsArray[$ctr]["country_ranking"];
				$currUserRate = $resultsArray[$ctr]["country_rate"];
				
								
				}
			
		$ctr++;
    }

	$worstState = $resultsArray[0]["country_state"];
	$worstCounty = $resultsArray[0]["country_county"];
	$worstRank = $resultsArray[0]["country_ranking"];
	$worstIncidenceRate = $resultsArray[0]["country_rate"];



	$bestState = $resultsArray[$result->num_rows -1]["country_state"];
	$bestCounty = $resultsArray[$result->num_rows -1]["country_county"];
	$bestRank = ($resultsArray[$result->num_rows -1]["country_ranking"]);
	$bestIncidenceRate = $resultsArray[$result->num_rows -1]["country_rate"];



	$statsArr = array("country_worst_state"=>$worstState,
				  "country_worst_county"=>$worstCounty,
				  "country_curr_user_rank"=>$currUserRank,
				  "country_worst_worst_rank"=>$worstRank,
				  "country_best_rank"=>$bestRank,
				  "country_worst_worst_incidence_rate"=>$worstIncidenceRate,
				  "country_best_state"=>$bestState,
				  "country_best_county"=>$bestCounty,
				  "country_best_incidence_rate"=>$bestIncidenceRate,
				  "country_star_ranking"=>getRanking($bestRank,$currUserRank),
				  "country_curr_user_incidence_rate"=>$currUserRate);


	$mysqli->close();


	return $statsArr;

}

function isValidWholeNumber($code){

	return preg_match('/^[0-9]+$/', $code);
}

function isValidAlphaString($county){

	return preg_match('/^[A-Z]+$/i', $county);
}

function getRanking($bestRank,$userRank){

	$segment = ($bestRank / 4.0);
	
	
	if($userRank > $segment * 3)
			   return 4;


	else if($userRank > $segment * 2)
				return 3;
			
			
	else if($userRank > $segment)
				return 2;
			

	else 
		return 1;
}


?>



