<?php
require '../izvor/Database.class.php';
require '../entiteti/KorisnikovaValuta.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sveKorisnikoveValute=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from korisnikoveValute");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $kv=new KorisnikovaValuta($redak,true);
        array_push($sveKorisnikoveValute,$kv->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveKorisnikoveValute);
}  
if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNoveKorisnikoveValute())
{        
    $korisnikovaValuta = kreirajRedakKorisnikoveValute();
    $kv = new KorisnikovaValuta($korisnikovaValuta);
    $upit = "INSERT INTO korisnikoveValute(valuta,korisnik) VALUES (TRIM(BOTH '\"' FROM '$kv->valuta'),TRIM(BOTH '\"' FROM '$kv->korisnik'))";
    $rezultatObrade = $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="update")
{
    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE korisnikoveValute SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM korisnikoveValute WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>