<?php
require 'SlusacCiljeva.php';

function provjeriID(){
    return (isset($_POST["id"]))?true:false;
}
function provjeriNaziv(){
    return (isset($_POST["naziv"]))?true:false;
}
function provjeriIznos(){
    return (isset($_POST["iznos"]))?true:false;
}
function provjeriKorisnik(){
    return (isset($_POST["korisnik"]))?true:false;
}
function provjeriDatum(){
    return (isset($_POST["datum"]))?true:false;
}
function provjeriKategorija(){
    return (isset($_POST["kategorija"]))?true:false;
}
function provjeriOstvareniCilj(){
    return (isset($_POST["ostvareniCilj"]))?true:false;
}

function provjeriPostojanostPodatakaNovogCilja(){
    return (provjeriNaziv()&&provjeriIznos()&&provjeriKorisnik()&&provjeriDatum()&&provjeriKategorija()&&provjeriOstvareniCilj())?true:false;
}
function provjeriPostojanostPodatakaAzuriranogCilja(){
    return (provjeriID()&&provjeriNaziv()&&provjeriIznos()&&provjeriKorisnik()&&provjeriDatum()&&provjeriKategorija()&&provjeriOstvareniCilj())?true:false;
}
function kreirajRedakCilja(){
    $redakCilja;
    $redakCilja["naziv"]=$_POST["naziv"];
    $redakCilja["iznos"]=$_POST["iznos"];
    $redakCilja["korisnik"]=$_POST["korisnik"];
    $redakCilja["datum"]=$_POST["datum"];
    $redakCilja["kategorija"]=$_POST["kategorija"];
    $redakCilja["ostvareniCilj"]=$_POST["ostvareniCilj"];
    return $redakCilja;
}
?>