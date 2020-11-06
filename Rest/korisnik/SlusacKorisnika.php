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
    $upit = "INSERT INTO korisnik(ime,prezime,email,lozinka,google_ID) VALUES (TRIM(BOTH '\"' FROM '$k->ime'),TRIM(BOTH '\"' FROM '$k->prezime'),TRIM(BOTH '\"' FROM '$k->email'),TRIM(BOTH '\"' FROM '$k->lozinka'),TRIM(BOTH '\"' FROM '$k->google_ID'))";
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
if(isset($_GET["query"])&&$_GET["query"]=="selectOneUserLogin"&&provjeriPostojanostPodatakaKorisnikaZaLogin())
{
    $korisnikovRedak=array();
    $email=$_POST["email"];
    $lozinka=$_POST["lozinka"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from korisnik where email='$email' && lozinka='$lozinka'");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $k=new Korisnik($redak,true);
        array_push($korisnikovRedak,$k->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($korisnikovRedak);
} 
?>