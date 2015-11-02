<?php


$states = array(
"01" => "AL",
"02" => "AK",
"04" => "AZ",
"05" => "AR",
"06" => "CA",
"08" => "CO",
"09" => "CT",
"10" => "DE",
"11" => "DC",
"12" => "FL",
"13" => "GA",
"15" => "HI",
"16" => "ID",
"17" => "IL",
"18" => "IN",
"19" => "IA",
"20" => "KS",
"21" => "KY",
"22" => "LA",
"23" => "ME",
"24" => "MD",
"25" => "MA",
"26" => "MI",
"27" => "MN",
"28" => "MS",
"29" => "MO",
"30" => "MT",
"31" => "NE",
"32" => "NV",
"33" => "NH",
"34" => "NJ",
"35" => "NM",
"36" => "NY",
"37" => "NC",
"38" => "ND",
"39" => "OH",
"40" => "OK",
"41" => "OR",
"42" => "PA",
"44" => "RI",
"45" => "SC",
"46" => "SD",
"47" => "TN",
"48" => "TX",
"49" => "UT",
"50" => "VT",
"51" => "VA",
"53" => "WA",
"54" => "WV",
"55" => "WI",
"56" => "WY"
);
$types = array(
"001" => "All Cancer Sites",
"071" => "Bladder",
"076" => "Brain & ONS",
"055" => "Breast Female",
"400" => "Breast Female in situ",
"057" => "Cervix",
"516" => "Childhood Ages Less Than 15, All Sites",
"515" => "Childhood Ages Less Than 20, All Sites",
"020" => "Colon & Rectum",
"017" => "Esophagus",
"072" => "Kidney & Renal Pelvis",
"090" => "Leukemia",
"035" => "Liver & Bile Duct",
"047" => "Lung & Bronchus",
"053" => "Melanoma of the Skin",
"086" => "Non-Hodgkin Lymphoma",
"003" => "Oral Cavity & Pharynx",
"061" => "Ovary",
"040" => "Pancreas",
"066" => "Prostate",
"018" => "Stomach",
"080" => "Thyroid",
"058" => "Uterus");


foreach($states as $key => $val){

	foreach($types as $k => $v){
	$sex="0";
	
	if($k =="400" || $k=="057" || $k=="055" || $k=="061" || $k=="058"){
			
			$sex="2";
	}
	else if($k =="066"){
		$sex="1";
	
	} else {
		$sex="0";
	}
      

$new_v = str_replace(
    array("&","  "," "),
    array("-&-", "-","-"),
    $v
);

$new_val = str_replace(
    array("&","  "," "),
    array("--", " ","-"),
    $val
);

$fileName = $key."_".$k."-".$new_v."--".$new_val;   


	   
$cmd = "curl --request POST 'http://statecancerprofiles.cancer.gov/incidencerates/index.php?stateFIPS=".$key."&cancer=".$k."&race=00&sex=".$sex.($k !="515" && $k !="516" ?"&age=001" : "")."&type=incd&sortVariableName=rate&sortOrder=desc&output=1' -o \"".$fileName.".txt\"";
	   shell_exec($cmd);
	   sleep(1);
	   
	}
	
}

echo "all done";


/*
$cancerType="040";
$state="18";
$cmd = "curl --request POST 'http://statecancerprofiles.cancer.gov/incidencerates/index.php?stateFIPS=".$state."&cancer=".$cancerType."&race=00&sex=0&age=001&type=incd&sortVariableName=rate&sortOrder=desc&output=1' -o \"2_indiana_pancreatic.txt\"";
shell_exec($cmd);

*/


?>