<?php
    
    require 'SlusacKorisnika.php';

    
    function provjeriID(){
        return (isset($_POST["id"]))?true:false;
    }
    function provjeriAtribut(){
        return (isset($_POST["atribut"]))?true:false;
    }
    function provjeriVrijednost(){
        return (isset($_POST["vrijednost"]))?true:false;
    }
    function provjeriIme(){
        return (isset($_POST["ime"]))?true:false;
    }
    function provjeriPrezime(){
        return (isset($_POST["prezime"]))?true:false;
    }
    function provjeriEmail(){
        return (isset($_POST["email"]))?true:false;
    }
    function provjeriLozinku(){
        return (isset($_POST["lozinka"]))?true:false;
    }
    function provjeriGoogleId(){
        return (isset($_POST["google_ID"]))?true:false;
    }
    function provjeriPostojanostPodatakaNovogKorisnika(){
        return (provjeriIme()&&provjeriPrezime()&&provjeriEmail()&&provjeriLozinku()&&provjeriGoogleId())?true:false;
    }
    function provjeriPostojanostPodatakaAzuriranogKorisnika(){
        return (provjeriID()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
    }
    function kreirajKorisnikovRedak(){
        $korisnikovRedak;
        $korisnikovRedak["ime"]=$_POST["ime"];
        $korisnikovRedak["prezime"]=$_POST["prezime"];
        $korisnikovRedak["email"]=$_POST["email"];
        $korisnikovRedak["lozinka"]=$_POST["lozinka"];
        $korisnikovRedak["google_ID"]=$_POST["google_ID"];
        return $korisnikovRedak;
    }
    
    
?>