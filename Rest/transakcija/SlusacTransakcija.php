<?php
require '../izvor/Database.class.php';
require '../entiteti/Transakcija.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sveTransakcije=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($sveTransakcije,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveTransakcije);
}  
if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNoveTransakcije())
{        
    $transakcija = kreirajRedakTransakcije();
    $t = new Transakcija($transakcija,true);
    $upit = "INSERT INTO transakcija(iznos,datum,racunTerecenja,racunPrijenosa,tipTransakcije,memo,opis,ponavljajuciTrosak,ikona,korisnik,intervalPonavljanja,kategorijaTransakcije,placenTrosak) VALUES (TRIM(BOTH '\"' FROM '$t->iznos'),TRIM(BOTH '\"' FROM '$t->datum'),TRIM(BOTH '\"' FROM '$t->racunTerecenja'),TRIM(BOTH '\"' FROM '$t->racunPrijenosa'),TRIM(BOTH '\"' FROM '$t->tipTransakcije'),TRIM(BOTH '\"' FROM '$t->memo'),TRIM(BOTH '\"' FROM '$t->opis'),TRIM(BOTH '\"' FROM '$t->ponavljajuciTrosak'),TRIM(BOTH '\"' FROM '$t->ikona'),TRIM(BOTH '\"' FROM '$t->korisnik'),TRIM(BOTH '\"' FROM '$t->intervalPonavljanja'),TRIM(BOTH '\"' FROM '$t->kategorijaTransakcije'),TRIM(BOTH '\"' FROM '$t->placenTrosak'))";
    $rezultatObrade = $baza->updateDB($upit);
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($t);
}
if(isset($_GET["query"])&&$_GET["query"]=="selectOneTransakcija"&&isset($_GET["id"]))
{
    $redakTransakcije=array();
    $id=$_GET["id"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija where id=$id");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($redakTransakcije,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($redakTransakcije);
}
if(isset($_GET["query"])&&$_GET["query"]=="selectTransakcijeKorisnika"&&isset($_GET["korisnik"]))
{
    $sveTransakcijeKorisnika=array();
    $korisnik=$_GET["korisnik"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija where korisnik=$korisnik");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($sveTransakcijeKorisnika,$r->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveTransakcijeKorisnika);
}
if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriraneTransakcije()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE transakcija SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM transakcija WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>