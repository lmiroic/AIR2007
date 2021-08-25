<?php
require '../izvor/Database.class.php';
require '../entiteti/Valuta.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sveValute=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from valuta");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $v=new Valuta($redak,true);
        array_push($sveValute,$v->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveValute);
}  
if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNoveValute())
{        
    $valuta = kreirajRedakValute();
    $v = new Valuta($valuta,true);
    $upit = "INSERT INTO valuta(naziv,tecaj,datum) VALUES (TRIM(BOTH '\"' FROM '$v->naziv'),TRIM(BOTH '\"' FROM '$v->tecaj'),TRIM(BOTH '\"' FROM '$v->datum'))";
    $rezultatObrade = $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriraneValute()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["naziv"];
    $upit = "UPDATE valuta SET $atribut='{$vrijednost}' WHERE naziv='{$identifikator}'";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriNaziv()){
    $identifikator = $_POST["naziv"];
    $upit = "DELETE FROM valuta WHERE naziv='{$identifikator}'";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="selectValutuPoNazivuIDatumu")
{
    $redakValute=array();
    $naziv=$_POST["naziv"];
    $datum=$_POST["datum"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from valuta where naziv='$naziv' && datum='$datum'");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $v=new Valuta($redak,true);
        array_push($redakValute,$v->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($redakValute);
} 
?>