<?php
require '../izvor/Database.class.php';
require '../entiteti/Racun.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sviRacuni=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from racun");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $r=new Racun($redak,true);
        array_push($sviRacuni,$r->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviRacuni);
}  
if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNovogRacuna())
{        
    $racun = kreirajRedakRacuna();
    $r = new Racun($racun);
    $upit = "INSERT INTO racun(naziv,pocetno_stanje,valuta,boja,ikona,korisnik_id) VALUES (TRIM(BOTH '\"' FROM '$r->naziv'),TRIM(BOTH '\"' FROM '$r->pocetno_stanje'),TRIM(BOTH '\"' FROM '$r->valuta'),TRIM(BOTH '\"' FROM '$r->boja'),TRIM(BOTH '\"' FROM '$r->ikona'),TRIM(BOTH '\"' FROM '$k->korisnik_id'))";
    $rezultatObrade = $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="selectOneRacun"&&isset($_GET["id"]))
{
    $redakRacuna=array();
    $id=$_GET["id"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from racun where id=$id");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $r=new Racun($redak,true);
        array_push($redakRacuna,$r->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($redakRacuna);
}
if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriranogRacuna()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE racun SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM racun WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>