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
    $upit = "INSERT INTO racun(naziv,pocetno_stanje,valuta,ikona,korisnik_id) VALUES (TRIM(BOTH '\"' FROM '$r->naziv'),TRIM(BOTH '\"' FROM '$r->pocetno_stanje'),TRIM(BOTH '\"' FROM '$r->valuta'),TRIM(BOTH '\"' FROM '$r->ikona'),TRIM(BOTH '\"' FROM '$r->korisnik_id'))";
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
if(isset($_GET["query"])&&$_GET["query"]=="selectRacuneKorisnika"&&isset($_GET["korisnik_id"]))
{
    $sviRacuniKorisnika=array();
    $korisnik_id=$_GET["korisnik_id"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from racun where korisnik_id=$korisnik_id");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $r=new Racun($redak,true);
        array_push($sviRacuniKorisnika,$r->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviRacuniKorisnika);
}
if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriranogRacuna()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE racun SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="updateRacun"&&provjeriPostojanostPodatakaAzuriranogRacunaKorisnika()){

    $id = $_POST["id"];
    $naziv = $_POST["naziv"];
    $pocetno_stanje = $_POST["pocetno_stanje"];
    $valuta = $_POST["valuta"];
    $ikona = $_POST["ikona"];
    $korisnik_id = $_POST["korisnik_id"];
    $upit = "UPDATE racun SET naziv='$naziv',pocetno_stanje=$pocetno_stanje,valuta='$valuta',ikona='$ikona',korisnik_id=$korisnik_id WHERE id=$id";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM racun WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>