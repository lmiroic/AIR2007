<?php
require '../izvor/Database.class.php';
require '../entiteti/Korisnik.class.php';
$baza=new Database();
$baza->spojiDB();
if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sviKorisnici=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from korisnik");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $k=new Korisnik($redak,true);
        array_push($sviKorisnici,$k->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sviKorisnici);

}  
if(isset($_GET["query"])&&$_GET["query"]=="selectOneUser"&&isset($_GET["id"]))
{
    $korisnikovRedak=array();
    $id=$_GET["id"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from korisnik where id=$id");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $k=new Korisnik($redak,true);
        array_push($korisnikovRedak,$k->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($korisnikovRedak);
} 

if(isset($_GET["query"])&&$_GET["query"]=="insert"&&provjeriPostojanostPodatakaNovogKorisnika())
{        
    $korisnik = kreirajKorisnikovRedak();
    $k = new Korisnik($korisnik);
    $upit = "INSERT INTO korisnik(ime,prezime,email,lozinka,google_ID) VALUES ('$k->ime','$k->prezime','$k->email','$k->lozinka','$k->google_ID')";
    $rezultatObrade = $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriranogKorisnika()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE korisnik SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}
if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM korisnik WHERE id=$identifikator";
    $baza->updateDB($upit);
}
?>