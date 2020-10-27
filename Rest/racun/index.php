<?php
require '../izvor/Database.class.php';
require '../entiteti/Racun.class.php';
require '../entiteti/Korisnik.class.php';
$baza=new Database();
$baza->spojiDB();
function provjeriID(){
    return (isset($_POST["id"]))?true:false;
}
function provjeriAtribut(){
    return (isset($_POST["atribut"]))?true:false;
}
function provjeriVrijednost(){
    return (isset($_POST["vrijednost"]))?true:false;
}
function provjeriNaziv(){
    return (isset($_POST["naziv"]))?true:false;
}
function provjeriPocetnoStanje(){
    return (isset($_POST["pocetno_stanje"]))?true:false;
}
function provjeriValutu(){
    return (isset($_POST["valuta"]))?true:false;
}
function provjeriBoju(){
    return (isset($_POST["boja"]))?true:false;
}
function provjeriIkonu(){
    return (isset($_POST["ikona"]))?true:false;
}
function provjeriKorisnika(){
    return (isset($_POST["korisnik_id"]))?true:false;
}
function provjeriPostojanostPodatakaNovogRacuna(){
    return (provjeriNaziv()&&provjeriPocetnoStanje()&&provjeriValutu()&&provjeriBoju()&&provjeriIkonu()&&provjeriKorisnika())?true:false;
}
function kreirajRedakRacuna(){
    $redakRacuna;
    $redakRacuna["naziv"]=$_POST["naziv"];
    $redakRacuna["pocetno_stanje"]=$_POST["pocetno_stanje"];
    $redakRacuna["valuta"]=$_POST["valuta"];
    $redakRacuna["boja"]=$_POST["boja"];
    $redakRacuna["ikona"]=$_POST["ikona"];
    $redakRacuna["korisnik_id"]=$_POST["korisnik_id"];
    return $redakRacuna;
}

if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sviRacuni=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from racun");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $r=new Racun($redak);
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
    $upit = "INSERT INTO racun(naziv,pocetno_stanje,valuta,boja,ikona,korisnik_id) VALUES ('$r->naziv','$r->pocetno_stanje','$r->valuta','$r->boja','$r->ikona','$r->korisnik_id')";
    $rezultatObrade = $baza->updateDB($upit);
}
?>