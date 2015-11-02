<?php 

$count = 0;
foreach (glob("*.txt") as $filename) {

	/***********************************
	  RegEx: ([0-9]+)_([0-9]+)\-(.*)
	  
	  state:\1 
	  disease code:\2 
	  
	  -------------------------------------------------------------
	  Fields in datafile
	  -------------------------------------------------------------
	  1.County	
	  2.Annual Incidence Rate(ﾆ) over rate period - cases per 100,000
	  3.Annual Lower 95% Confidence Interval
	  4.Annual Upper 95% Confidence Interval
	  5.Average Annual Count over rate period
	  6.Rate Period
	  7.Recent Trend	
	  8.Recent 5-Year Trend (ﾇ) in Incidence Rates
	  9.Recent Five Year Lower 95% Confidence Interval	
	  10.Recent Five Year Upper 95% Confidence Interval
	  11.State code
	  12.Cancer Type code
	  


		
		"County" => ""
		¶¶ => "" or 0
		¶  => "" or 0
		*  => "" or 0
		 § => ""
	



	
	*************************************/
	
	
	
	
	$pattern = '/^([0-9]+)_([0-9]+)\-(.*)/';
	preg_match($pattern, $filename, $matches);
	
	$stateCode = $matches[1];
	$diseaseCode = $matches[2];
	
	echo "File: ".$filename. "<br />\nState: ".$stateCode."<br />\nDisease Code: "
	.$diseaseCode."<br />\n*****************************************<br />\n";

	
		
		$handle = fopen($filename, "r");
		if ($handle) {
    		
    		while (($line = fgets($handle)) !== false) {
    		 
    		 echo $line."<br />";
    		 
    		 
    		    		 
    	
    			
    			/*
    				1. string County	
	  				2. decimal Annual Incidence Rate(ﾆ) over rate period - cases per 100,000
	  				3. decimal Annual Lower 95% Confidence Interval
	  				4. decimal Annual Upper 95% Confidence Interval
	  				5. decimal Average Annual Count over rate period
	  				6. string Rate Period
	  				7. string Recent Trend	
	  				8. decimal Recent 5-Year Trend (ﾇ) in Incidence Rates
	  				9. decimal Recent Five Year Lower 95% Confidence Interval	
	  				10.decimal Recent Five Year Upper 95% Confidence Interval
	  				
	  				1. "Monroe County"
	  				2. 515.3
	  				3. 457.2
	  				4. 579.5
	  				5. 61
	  				6. 2008-2012
	  				7. falling
	  				8. -6.6
	  				9. -9.2
	  				10.-3.9 
	  				
	  				1."Nevada County",
	  				2. *
	  				3. * 
	  				4. *
	  				5. 3 or fewer,
	  				6. 2008-2012,
	  				7. *
	  				8. *
	  				9. *
	  				10.* 
	  				
	  				1."Allen County",
	  				2.¶
	  				3.¶
	  				4.¶
	  				5.¶
	  				6.2008-2012
	  				7.¶
	  				8.¶
	  				9.¶
	  				10.¶ 
    			
    			*/
    			list($county, 
    				 $annualIncidenceRate, 
    				 $annualLower95ConfidenceInterval, 
    				 $annualUpper95ConfidenceInterval, 
    				 $avgAnnualCountOverRatePeriod, 
    				 $ratePeriod,
    				 $recentTrend,
    				 $recent5YearTrendInIncidence,
    				 $recent5YearLower95ConfidenceInterval,
    				 $recent5YearUpper95ConfidenceInterval) = explode(",", $line);
    		
        			
        			/*****************************************************
        			 Clean up data
        			******************************************************/
        			$county = str_replace(array('"',' County'),'',$county);
        		 	

        		 	
        		 	$pattern2 = '/^([0-9]+)/';
					preg_match($pattern2, $avgAnnualCountOverRatePeriod, $matches2);
					$avgAnnualCountOverRatePeriod = $matches2[1];
					

 					
    		        
					
    		        $trendData = "TRUE";
    		        if (!preg_match('/^([A-Za-z])/',trim($recentTrend))
    						 && !isValidNumber(trim($recent5YearTrendInIncidence))
    						 && !isValidNumber(trim($recent5YearLower95ConfidenceInterval))
    						 && !isValidNumber(trim($recent5YearUpper95ConfidenceInterval)) ){
    							
    							$trendData = "FALSE";
    				}


					echo "Trend Data:".$trendData."<br />";		
					echo $stateCode."<br />";
        			if(!preg_match('/^([A-Za-z])/', trim($county)) ){
        		 		
        		 		echo " <span style=\"color:#B82009; font-weight:bold;\">COUNTY IS NULL</span><br />";
        		 	}
        		 	
        			echo $county."<br />";
					echo $diseaseCode."<br />";
					
					
					echo $annualIncidenceRate.(isValidNumber($annualIncidenceRate) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";
        			
        			
        		  echo $annualLower95ConfidenceInterval.(isValidNumber($annualLower95ConfidenceInterval) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";
        			
        		
        		 echo $annualUpper95ConfidenceInterval.(isValidNumber($annualUpper95ConfidenceInterval) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";
        		
        			

        		 echo $avgAnnualCountOverRatePeriod.(isValidNumber($avgAnnualCountOverRatePeriod) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";        			
        			
        			echo $ratePeriod."<br />";
        			echo $recentTrend."<br />";
        			
        			
        		 echo $recent5YearTrendInIncidence.(isValidNumber($recent5YearTrendInIncidence) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";           			



        		 echo $recent5YearLower95ConfidenceInterval.(isValidNumber($recent5YearLower95ConfidenceInterval) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";   



        		 echo $recent5YearUpper95ConfidenceInterval.(isValidNumber($recent5YearUpper95ConfidenceInterval) ? 
				" <span style=\"color:#49C507; font-weight:bold;\">VALID</span>" : 
				" <span style=\"color:#B82009; font-weight:bold;\">INVALID</span>")."<br />";   
        			
        			

$sql = "INSERT INTO `cancer_stats`(state_code,county,cancer_type_code,annual_incidence_rate,lower_95_confidence_interval_annual,upper_95_confidence_interval_annual,avg_annual_count_over_rate_period,rate_period,recent_trend,recent_5_year_trend_in_incidence_rates,lower_95_confidence_interval_5_year,upper_95_confidence_interval_5_year)
VALUES('".trim($stateCode)."','".trim($county)."','".trim($diseaseCode)."','".number_format(trim($annualIncidenceRate), 2, '.', '') ."','".number_format(trim($annualLower95ConfidenceInterval),2,'.','')."','".number_format(trim($annualUpper95ConfidenceInterval),2,'.','')."','".number_format(trim($avgAnnualCountOverRatePeriod), 2, '.', '')."','".trim($ratePeriod)."','".trim($recentTrend)."','".number_format(trim($recent5YearTrendInIncidence),2,'.','')."','".number_format(trim($recent5YearLower95ConfidenceInterval),2,'.','')."','".number_format(trim($recent5YearUpper95ConfidenceInterval),2,'.','')."');";        			

echo $sql."<br />\n";     
        			echo "*****************************************<br />\n";

     		
   		}

    		fclose($handle);
	
		} else {
   	
   			
   			echo "Error opening file ".$filename."<br />";
		} 

	
	
		
	
	
	
	
	
	
	$count++;
}
echo "Total file count: ".$count;


function isValidNumber($input){


	return preg_match("/\-?(\d+)(\.\d+)?/", $input);

}


?>