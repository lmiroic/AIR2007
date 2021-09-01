<?php
require '../izvor/Database.class.php';
require '../entiteti/Cilj.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sviCiljevi=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from ciljevi");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $c=new Cilj($redak,true);
        array_push($sviCiljevi,$c->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviCiljevi);
}  
if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNovogCilja())
{        
    $cilj = kreirajRedakCilja();
    $c = new Cilj($cilj,true);
    $upit = "INSERT INTO ciljevi(naziv,iznos,korisnik,datum,kategorija,ostvareniCilj) VALUES (TRIM(BOTH '\"' FROM '$c->naziv'),TRIM(BOTH '\"' FROM '$c->iznos'),TRIM(BOTH '\"' FROM '$c->korisnik'),TRIM(BOTH '\"' FROM '$c->datum'),TRIM(BOTH '\"' FROM '$c->kategorija'),TRIM(BOTH '\"' FROM '$c->ostvareniCilj'))";
    $rezultatObrade = $baza->updateDB($upit);
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($c);
}
if(isset($_GET["query"])&&$_GET["query"]=="selectCiljeveKorisnika"&&isset($_GET["korisnik"]))
{
    $sviCiljeviKorisnika=array();
    $korisnik=$_GET["korisnik"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from ciljevi where korisnik=$korisnik");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $c=new Cilj($redak,true);
        array_push($sviCiljeviKorisnika,$c->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviCiljeviKorisnika);
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
    $upit = "DELETE FROM ciljevi WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>