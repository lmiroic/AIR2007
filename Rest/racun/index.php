<?php
require 'SlusacRacuna.php';

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
function provjeriKorisnika(){
    return (isset($_POST["korisnik_id"]))?true:false;
}
function provjeriPocetnoStanje(){
    return (isset($_POST["pocetno_stanje"]))?true:false;
}
function provjeriValutu(){
    return (isset($_POST["valuta"]))?true:false;
}
function provjeriIkonu(){
    return (isset($_POST["ikona"]))?true:false;
}
function provjeriPostojanostPodatakaNovogRacuna(){
    return (provjeriNaziv()&&provjeriPocetnoStanje()&&provjeriValutu()&&provjeriIkonu()&&provjeriKorisnika())?true:false;
}
function provjeriPostojanostPodatakaAzuriranogRacuna(){
    return (provjeriID()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
}
function provjeriPostojanostPodatakaAzuriranogRacunaKorisnika(){
    return (provjeriNaziv()&&provjeriPocetnoStanje()&&provjeriValutu()&&provjeriIkonu()&&provjeriKorisnika()&&provjeriID())?true:false;
}
function kreirajRedakRacuna(){
    $redakRacuna;
    $redakRacuna["naziv"]=$_POST["naziv"];
    $redakRacuna["pocetno_stanje"]=$_POST["pocetno_stanje"];
    $redakRacuna["valuta"]=$_POST["valuta"];
    $redakRacuna["ikona"]=$_POST["ikona"];
    $redakRacuna["korisnik_id"]=$_POST["korisnik_id"];
    return $redakRacuna;
}


?>