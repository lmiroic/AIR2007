<?php
require 'SlusacTransakcija.php';

function provjeriID(){
    return (isset($_POST["id"]))?true:false;
}
function provjeriAtribut(){
    return (isset($_POST["atribut"]))?true:false;
}
function provjeriVrijednost(){
    return (isset($_POST["vrijednost"]))?true:false;
}
function provjeriIznos(){
    return (isset($_POST["iznos"]))?true:false;
}
function provjeriDatum(){
    return (isset($_POST["datum"]))?true:false;
}
function provjeriRacunTerecenja(){
    return (isset($_POST["racunTerecenja"]))?true:false;
}
function provjeriRacunPrijenosa(){
    return (isset($_POST["racunPrijenosa"]))?true:false;
}
function provjeriTipTransakcije(){
    return (isset($_POST["tipTransakcije"]))?true:false;
}
function provjeriMemo(){
    return (isset($_POST["memo"]))?true:false;
}
function provjeriOpis(){
    return (isset($_POST["opis"]))?true:false;
}
function provjeriPonavljajuciTrosak(){
    return (isset($_POST["ponavljajuciTrosak"]))?true:false;
}
function provjeriIkonu(){
    return (isset($_POST["ikona"]))?true:false;
}
function provjeriKorisnika(){
    return (isset($_POST["korisnik"]))?true:false;
}
function provjeriIntervalPonavljanja(){
    return (isset($_POST["intervalPonavljanja"]))?true:false;
}
function provjeriKategorijuTransakcije(){
    return (isset($_POST["kategorijaTransakcije"]))?true:false;
}
function provjeriPlacenTrosak(){
    return (isset($_POST["placenTrosak"]))?true:false;
}

function provjeriPostojanostPodatakaNoveTransakcije(){
    return (provjeriIznos()&&provjeriDatum()&&provjeriRacunTerecenja()&&provjeriRacunPrijenosa()&&provjeriTipTransakcije()&&provjeriMemo()&&provjeriOpis()&&provjeriPonavljajuciTrosak()&&provjeriIkonu()&&provjeriKorisnika()&&provjeriIntervalPonavljanja()&&provjeriKategorijuTransakcije()&&provjeriPlacenTrosak())?true:false;
}
function provjeriPostojanostPodatakaAzuriraneTransakcije(){
    return (provjeriID()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
}
function kreirajRedakTransakcije(){
    $redakTransakcije;
    $redakTransakcije["iznos"]=$_POST["iznos"];
    $redakTransakcije["datum"]=$_POST["datum"];
    $redakTransakcije["racunTerecenja"]=$_POST["racunTerecenja"];
    $redakTransakcije["racunPrijenosa"]=$_POST["racunPrijenosa"];
    $redakTransakcije["tipTransakcije"]=$_POST["tipTransakcije"];
    $redakTransakcije["memo"]=$_POST["memo"];
    $redakTransakcije["opis"]=$_POST["opis"];
    $redakTransakcije["ponavljajuciTrosak"]=$_POST["ponavljajuciTrosak"];
    $redakTransakcije["ikona"]=$_POST["ikona"];
    $redakTransakcije["korisnik"]=$_POST["korisnik"];
    $redakTransakcije["intervalPonavljanja"]=$_POST["intervalPonavljanja"];
    $redakTransakcije["kategorijaTransakcije"]=$_POST["kategorijaTransakcije"];
    $redakTransakcije["placenTrosak"]=$_POST["placenTrosak"];
    return $redakTransakcije;
}
?>