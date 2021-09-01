<?php
require 'SlusacValuta.php';

function provjeriNaziv(){
    return (isset($_POST["naziv"]))?true:false;
}
function provjeriTecaj(){
    return (isset($_POST["tecaj"]))?true:false;
}
function provjeriDatum(){
    return (isset($_POST["datum"]))?true:false;
}
function provjeriAtribut(){
    return (isset($_POST["atribut"]))?true:false;
}
function provjeriVrijednost(){
    return (isset($_POST["vrijednost"]))?true:false;
}
function provjeriPostojanostPodatakaNoveValute(){
    return (provjeriNaziv()&&provjeriTecaj()&&provjeriDatum())?true:false;
}
function provjeriPostojanostPodatakaAzuriraneValute(){
    return (provjeriNaziv()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
}
function kreirajRedakValute(){
    $redakValute;
    $redakValute["naziv"]=$_POST["naziv"];
    $redakValute["tecaj"]=$_POST["tecaj"];
    $redakValute["datum"]=$_POST["datum"];
    return $redakValute;
}
?>