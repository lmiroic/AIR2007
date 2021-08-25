<?php
require 'SlusacKorisnikoveValute.php';

function provjeriID(){
    return (isset($_POST["id"]))?true:false;
}
function provjeriAtribut(){
    return (isset($_POST["atribut"]))?true:false;
}
function provjeriVrijednost(){
    return (isset($_POST["vrijednost"]))?true:false;
}
function provjeriValutu(){
    return (isset($_POST["valuta"]))?true:false;
}
function provjeriKorisnika(){
    return (isset($_POST["korisnik"]))?true:false;
}
function provjeriPostojanostPodatakaNoveKorisnikoveValute(){
    return (provjeriValutu()&&provjeriKorisnika())?true:false;
}
function provjeriPostojanostPodatakaAzuriraneKorisnikoveValute(){
    return (provjeriID()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
}
function kreirajRedakKorisnikoveValute(){
    $redakKorisnikoveValute;
    $redakKorisnikoveValute["valuta"]=$_POST["valuta"];
    $redakKorisnikoveValute["korisnik"]=$_POST["korisnik"];
    return $redakKorisnikoveValute;
}
?>