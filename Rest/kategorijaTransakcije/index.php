<?php
require '../izvor/Database.class.php';
require '../entiteti/KategorijaTransakcije.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sveKategorije=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from kategorija_transakcije");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $kt=new KategorijaTransakcije($redak);
        array_push($sveKategorije,$kt->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveKategorije);
}  
?>