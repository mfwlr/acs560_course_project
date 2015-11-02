<?php 

$count = 0;
foreach (glob("*.txt") as $filename) {
    echo "$filename size " . filesize($filename) . "<br />\n";
    $count++;
}
echo "Total file count: ".$count;

?>