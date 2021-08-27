<?php
require '../izvor/Database.class.php';
require '../entiteti/TipTransakcije.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sviTipovi=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from tip_transakcije");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new TipTransakcije($redak);
        array_push($sviTipovi,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviTipovi);
}  
?>